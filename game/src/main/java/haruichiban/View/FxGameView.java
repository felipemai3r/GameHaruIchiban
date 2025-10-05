package haruichiban.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import haruichiban.Controller.GameController;
import haruichiban.Model.Flor;
import haruichiban.Model.Jogador;
import haruichiban.Model.JogoHaruIchiban;
import haruichiban.Model.enums.CorJogador;
import haruichiban.Model.enums.FaseJogo;
import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * VIEW do MVC: desenha a tela e conversa apenas com o Controller.
 * Não contém regra de negócio do jogo (isso está no Model).
 */
public class FxGameView implements GameView {

    private final Stage stage;
    private final HostServices hostServices;
    private final BorderPane root = new BorderPane();

    // Topo (centralizado)
    private final VBox header = new VBox();
    private final Label lblTitulo = new Label("Haru Ichiban");
    private final Button btnNovoJogo = new Button("Novo Jogo");
    private final Button btnRegras = new Button("Regras");
    private final Label lblTurno = new Label();
    private final Label lblFase = new Label();
    private final Label lblMsg = new Label();

    // Centro (tabuleiro)
    private final GameBoardGrid boardGrid = new GameBoardGrid();

    // Laterais: mãos
    private static final double SIDE_WIDTH = 240;
    private final VBox maoVermelhoBox = new VBox(10);
    private final VBox maoAmareloBox = new VBox(10);

    // Pontuação por lado
    private final VBox scoreVermelhoBox = new VBox(6);
    private final VBox scoreAmareloBox = new VBox(6);

    // Barra inferior: ações
    private final Button btnConfirmarSelecao = new Button("Confirmar seleção");
    private final Button btnFloracaoJunior = new Button("Floração Júnior");
    private final ToggleButton tglMoverSapo = new ToggleButton("Modo Mover Sapo");

    // Estado efêmero para cliques de origem/destino
    private Integer origemL = null, origemC = null;

    // Seleção de flor (por índice)
    private int idxFlorVermelho = -1;
    private int idxFlorAmarelo = -1;

    // Para destacar visualmente a seleção
    private final List<Region> cardsVermelho = new ArrayList<>();
    private final List<Region> cardsAmarelo = new ArrayList<>();

    // containers laterais
    private VBox leftSide, rightSide;

    private GameController controller;
    private JogoHaruIchiban jogo;

    public FxGameView(Stage stage, HostServices hostServices) {
        this.stage = stage;
        this.hostServices = hostServices;
        montarLayoutBase();
        configurarHandlersFixos();
    }

    public void attachController(GameController controller) { this.controller = controller; }

    public BorderPane getRoot() { return root; }

    // ----------------- GameView -----------------

    @Override
    public void atualizarTela(JogoHaruIchiban jogo) {
        this.jogo = jogo;

        lblTurno.setText("Turno: " + jogo.getTurnoAtual());
        lblFase.setText("Fase: " + jogo.getFaseAtual().getDescricao());
        lblMsg.setText(Optional.ofNullable(jogo.getMensagemEstado()).orElse(""));

        boardGrid.render(jogo.getTabuleiro());
        renderMaos(jogo);

        // pontuação por jogador embaixo de cada mão
        atualizarScore(scoreVermelhoBox, jogo.getJogadorVermelho());
        atualizarScore(scoreAmareloBox, jogo.getJogadorAmarelo());

        habilitarAcoesPorFase(jogo.getFaseAtual());
    }

    @Override
    public void mostrarErro(String mensagem) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Erro");
        a.setHeaderText(null);
        a.setContentText(mensagem);
        a.initOwner(stage);
        a.showAndWait();
    }

    @Override
    public void mostrarMensagem(String mensagem) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Informação");
        a.setHeaderText(null);
        a.setContentText(mensagem);
        a.initOwner(stage);
        a.showAndWait();
    }

    // ----------------- Layout/UX -----------------

    private void montarLayoutBase() {
        root.setPadding(new Insets(12));

        // Título + botões (esquerda), info de jogo (centro)
        lblTitulo.setFont(Font.font(null, FontWeight.BOLD, 22));

        // estilo do botão "Novo Jogo" (azul/água)
        btnNovoJogo.setStyle("""
            -fx-background-color: linear-gradient(#d7ecff,#9ac6f5);
            -fx-text-fill: #083b6b;
            -fx-font-weight: bold;
            -fx-background-radius: 8;
            -fx-padding: 6 12 6 12;
        """);

        btnRegras.setStyle("""
            -fx-background-color: #f2f2f2;
            -fx-background-radius: 8;
            -fx-padding: 6 12 6 12;
        """);

        HBox linhaTopo = new HBox(12, lblTitulo, btnNovoJogo, btnRegras);
        linhaTopo.setAlignment(Pos.CENTER_LEFT);

        lblTurno.setFont(Font.font(null, FontWeight.BOLD, 16));
        lblFase.setFont(Font.font(null, FontWeight.BOLD, 16));
        lblMsg.setFont(Font.font(14));

        HBox linhaInfo = new HBox(20, lblTurno, lblFase);
        linhaInfo.setAlignment(Pos.CENTER);

        HBox linhaMsg = new HBox(lblMsg);
        linhaMsg.setAlignment(Pos.CENTER);

        header.setSpacing(6);
        header.getChildren().addAll(linhaTopo, linhaInfo, linhaMsg);
        root.setTop(header);

        // Centro (tabuleiro)
        StackPane center = new StackPane(boardGrid.getNode());
        center.setPadding(new Insets(8));
        root.setCenter(center);

        // Lado esquerdo: mão VERMELHO + score
        TitledPane tpV = new TitledPane("Mão Vermelho", maoVermelhoBox);
        tpV.setCollapsible(false);
        scoreVermelhoBox.setPadding(new Insets(6,0,0,0));
        leftSide = new VBox(8, tpV, scoreVermelhoBox);
        leftSide.setPadding(new Insets(8));
        leftSide.setPrefWidth(SIDE_WIDTH);
        root.setLeft(leftSide);

        // Lado direito: mão AMARELO + score
        TitledPane tpA = new TitledPane("Mão Amarelo", maoAmareloBox);
        tpA.setCollapsible(false);
        scoreAmareloBox.setPadding(new Insets(6,0,0,0));
        rightSide = new VBox(8, tpA, scoreAmareloBox);
        rightSide.setPadding(new Insets(8));
        rightSide.setPrefWidth(SIDE_WIDTH);
        root.setRight(rightSide);

        // Barra inferior com ações
        HBox actions = new HBox(12, btnConfirmarSelecao, btnFloracaoJunior, tglMoverSapo);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(10, 0, 0, 0));
        root.setBottom(actions);

        btnConfirmarSelecao.setMaxWidth(220);
        btnFloracaoJunior.setMaxWidth(220);
        tglMoverSapo.setMaxWidth(220);
    }

    private void configurarHandlersFixos() {
        // Novo jogo
        btnNovoJogo.setOnAction(e -> solicitarNovoJogo(controller));

        // Regras (abre PDF das regras via HostServices; se necessário, extrai para temp)
        btnRegras.setOnAction(e -> abrirPdfRegras());

        // Floração Júnior
        btnFloracaoJunior.setOnAction(e -> controller.executarFloracaoJunior());

        // Clique no tabuleiro
        boardGrid.setOnCellClick((l, c) -> {
            if (jogo == null) return;
            FaseJogo fase = jogo.getFaseAtual();

            if (tglMoverSapo.isSelected()) {
                handleMoverSapo(l, c);
                return;
            }

            switch (fase) {
                case SELECAO_FLOR -> mostrarMensagem(
                        "Selecione uma flor de cada mão e clique em 'Confirmar seleção'.");
                case FLORACAO_JUNIOR -> mostrarMensagem(
                        "Use 'Floração Júnior'. Se houver sapo no escuro, ative 'Modo Mover Sapo'.");
                case FLORACAO_SENIOR -> controller.executarFloracaoSenior(l, c);
                case MOVIMENTO_NENUFARES -> handleMoverNenufar(l, c);
                case SELECAO_NENUFAR_ESCURO -> controller.selecionarNenufarEscuro(l, c);
                case FIM_RODADA -> mostrarMensagem("Fim da rodada. Clique em 'Novo Jogo' ou aguarde o backend.");
            }
        });

        // Confirmar seleção de flores
        btnConfirmarSelecao.setOnAction(e -> {
            if (idxFlorVermelho < 0 || idxFlorAmarelo < 0) {
                mostrarErro("Selecione 1 flor do Vermelho e 1 do Amarelo.");
                return;
            }
            controller.selecionarFlores(idxFlorVermelho, idxFlorAmarelo);
            idxFlorVermelho = idxFlorAmarelo = -1;
        });
    }

    /** Fluxo de "Novo Jogo": pergunta nomes e chama o controller. */
    public void solicitarNovoJogo(GameController controller) {
        TextInputDialog d1 = new TextInputDialog("Vermelho");
        d1.setHeaderText("Nome do Jogador Vermelho");
        String nV = d1.showAndWait().orElse("Vermelho");

        TextInputDialog d2 = new TextInputDialog("Amarelo");
        d2.setHeaderText("Nome do Jogador Amarelo");
        String nA = d2.showAndWait().orElse("Amarelo");

        controller.iniciarNovoJogo(nV, nA);
        idxFlorVermelho = idxFlorAmarelo = -1;
        origemL = origemC = null;
        tglMoverSapo.setSelected(false);
    }

    private void habilitarAcoesPorFase(FaseJogo fase) {
        boolean selecao = fase == FaseJogo.SELECAO_FLOR;
        maoVermelhoBox.setDisable(!selecao);
        maoAmareloBox.setDisable(!selecao);
        btnConfirmarSelecao.setDisable(!selecao);

        btnFloracaoJunior.setDisable(fase != FaseJogo.FLORACAO_JUNIOR);

        if (fase != FaseJogo.MOVIMENTO_NENUFARES) {
            origemL = origemC = null;
        }
    }

    private void renderMaos(JogoHaruIchiban jogo) {
        cardsVermelho.clear();
        cardsAmarelo.clear();
        maoVermelhoBox.getChildren().setAll(renderMao(jogo.getJogadorVermelho(), true));
        maoAmareloBox.getChildren().setAll(renderMao(jogo.getJogadorAmarelo(), false));
        updateSelectionStyles(true, idxFlorVermelho);
        updateSelectionStyles(false, idxFlorAmarelo);
    }

    private Node[] renderMao(Jogador jogador, boolean vermelho) {
        List<Flor> mao = jogador.getMao();
        Node[] nos = new Node[mao.size()];

        for (int i = 0; i < mao.size(); i++) {
            Flor f = mao.get(i);

            // Imagens: virada (sem número) e numerada
            Node imgPlain = SpriteFactory.get().flowerGeneric(f.getCor());
            Node imgNumber = SpriteFactory.get().flowerNumbered(f.getCor(), f.getValor());

            StackPane holder = new StackPane(imgPlain, imgNumber);
            holder.setPrefSize(100, 100);
            imgNumber.setVisible(false); // começa "virada"

            // Ações do cartão
            ToggleButton btnVer = new ToggleButton("Ver");
            btnVer.setOnAction(e -> {
                boolean show = btnVer.isSelected();
                imgNumber.setVisible(show);
                imgPlain.setVisible(!show);
            });

            Button btnSel = new Button("Selecionar");
            final int idx = i;
            btnSel.setOnAction(e -> {
                if (vermelho) {
                    idxFlorVermelho = idx;
                    updateSelectionStyles(true, idxFlorVermelho);
                } else {
                    idxFlorAmarelo = idx;
                    updateSelectionStyles(false, idxFlorAmarelo);
                }
            });

            HBox actions = new HBox(6, btnVer, btnSel);
            actions.setAlignment(Pos.CENTER);

            VBox card = new VBox(6, holder, actions);
            card.setPadding(new Insets(8));
            card.setAlignment(Pos.CENTER);
            card.setStyle(cardStyle(false));

            if (vermelho) cardsVermelho.add(card);
            else cardsAmarelo.add(card);

            nos[i] = card;
        }
        return nos;
    }

    private void updateSelectionStyles(boolean vermelho, int selectedIdx) {
        List<Region> cards = vermelho ? cardsVermelho : cardsAmarelo;
        for (int i = 0; i < cards.size(); i++) {
            Region r = cards.get(i);
            r.setStyle(cardStyle(i == selectedIdx));
        }
    }

    private String cardStyle(boolean selected) {
        if (selected) {
            return """
                   -fx-background-color: #ffffff;
                   -fx-border-color: #2b8a3e;
                   -fx-border-width: 2;
                   -fx-border-radius: 10;
                   -fx-background-radius: 10;
                   """;
        }
        return """
               -fx-background-color: #ffffff;
               -fx-border-color: #ddd;
               -fx-border-width: 1;
               -fx-border-radius: 10;
               -fx-background-radius: 10;
               """;
    }

    private void handleMoverNenufar(int l, int c) {
        if (origemL == null) {
            origemL = l; origemC = c;
            lblMsg.setText("Origem: (" + l + "," + c + "). Selecione um destino adjacente.");
        } else {
            controller.moverNenufar(origemL, origemC, l, c);
            origemL = origemC = null;
        }
    }

    private void handleMoverSapo(int l, int c) {
        if (origemL == null) {
            origemL = l; origemC = c;
            lblMsg.setText("Origem do sapo: (" + l + "," + c + "). Agora clique no destino.");
        } else {
            controller.moverSapo(origemL, origemC, l, c);
            origemL = origemC = null;
            tglMoverSapo.setSelected(false);
        }
    }

    // ---- Pontuação (bolinhas) ----
    private void atualizarScore(VBox box, Jogador j) {
        box.getChildren().setAll(scoreBox(j));
    }

    private Node scoreBox(Jogador j) {
        Label titulo = new Label(j.getNome() + " — Pontos: " + j.getPontuacao());

        HBox pips = new HBox(6);
        pips.setAlignment(Pos.CENTER_LEFT);

        for (int i = 1; i <= 5; i++) {
            Region pip = new Region();
            pip.setPrefSize(16, 16);
            pip.setMinSize(16, 16);
            pip.setMaxSize(16, 16);

            String colorOn  = (j.getCor() == CorJogador.VERMELHO) ? "#d93b53" : "#f2c200";
            String colorOff = "#e0e0e0";

            boolean filled = i <= j.getPontuacao();
            pip.setStyle("""
                -fx-background-color: %s;
                -fx-background-radius: 999;
                -fx-border-radius: 999;
                -fx-border-color: #ccc;
                """.formatted(filled ? colorOn : colorOff));

            pips.getChildren().add(pip);
        }

        VBox box = new VBox(4, titulo, pips);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(6, 0, 0, 0));
        return box;
    }

    // ---- Abrir PDF das regras ----
    private void abrirPdfRegras() {
        try {
            URL url = getClass().getResource("/haruichiban/regras/regras.pdf");
            if (url != null) {
                // Tenta abrir diretamente (browser/visualizador padrão)
                hostServices.showDocument(url.toExternalForm());
                return;
            }
            mostrarErro("Arquivo de regras não encontrado nos recursos.");
        } catch (Exception ex) {
            // Fallback: extrair para TEMP e abrir
            try (InputStream in = getClass().getResourceAsStream("/haruichiban/regras/regras.pdf")) {
                if (in == null) throw new IllegalStateException("Regras não localizadas.");
                File tmp = File.createTempFile("haru-ichiban-regras", ".pdf");
                tmp.deleteOnExit();
                try (FileOutputStream out = new FileOutputStream(tmp)) {
                    in.transferTo(out);
                }
                hostServices.showDocument(tmp.toURI().toString());
            } catch (Exception e2) {
                mostrarErro("Não foi possível abrir as regras: " + e2.getMessage());
            }
        }
    }
}
