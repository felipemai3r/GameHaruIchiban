package haruichiban.View;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import haruichiban.Controller.GameController;
import haruichiban.Model.Flor;
import haruichiban.Model.Jogador;
import haruichiban.Model.JogoHaruIchiban;
import haruichiban.Model.Tabuleiro;
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

public class FxGameView implements GameView {

    private final Stage stage;
    private final HostServices hostServices;
    private final BorderPane root = new BorderPane();

    // header
    private final VBox header = new VBox();
    private final Label lblTitulo = new Label("Haru Ichiban");
    private final Button btnNovoJogo = new Button("Novo Jogo");
    private final Button btnRegras = new Button("Regras");
    private final Label lblTurno = new Label();
    private final Label lblFase = new Label();
    private final Label lblMsg  = new Label();

    // EMPATE – botões para escolher quem coaxou
    private final Button btnCoaxV = new Button("Vermelho coaxou");
    private final Button btnCoaxA = new Button("Amarelo coaxou");
    private final HBox empateBox  = new HBox(8, btnCoaxV, btnCoaxA);

    // centro
    private final GameBoardGrid boardGrid = new GameBoardGrid();

    // laterais
    private final VBox maoVermelhoBox = new VBox(10);
    private final VBox maoAmareloBox  = new VBox(10);
    private final VBox scoreVermelhoBox = new VBox(6);
    private final VBox scoreAmareloBox  = new VBox(6);

    // bottom
    private final Button btnConfirmarSelecao = new Button("Confirmar seleção");
    private final Button btnFloracaoJunior   = new Button("Floração Júnior");
    private final ToggleButton tglMoverSapo  = new ToggleButton("Modo Mover Sapo");
    private final Button btnNovaRodada       = new Button("Nova Rodada"); // << NOVO

    // estado UI (escolha de flores)
    private int idxFlorVermelho = -1;
    private int idxFlorAmarelo  = -1;
    private final List<Region> cardsVermelho = new ArrayList<>();
    private final List<Region> cardsAmarelo  = new ArrayList<>();

    // estado UI (mover sapo manual)
    private Integer sapoOrigR = null, sapoOrigC = null;

    // estado UI (empate – próxima cor a posicionar)
    private CorJogador empateProximaCor = null;

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

    @Override public void atualizarTela(JogoHaruIchiban jogo) {
        this.jogo = jogo;

        lblTurno.setText("Turno: " + jogo.getTurnoAtual());
        lblFase.setText("Fase: " + jogo.getFaseAtual().getDescricao());
        lblMsg.setText(Optional.ofNullable(jogo.getMensagemEstado()).orElse(""));

        // Atualiza “próxima cor” no empate olhando o tabuleiro
        if (jogo.getFaseAtual() == FaseJogo.MOVER_SAPOS_EMPATE) {
            empateProximaCor = detectarProximaCorEmpate(jogo.getTabuleiro());
        } else {
            empateProximaCor = null;
        }

        boardGrid.render(jogo.getTabuleiro());
        renderMaos(jogo);
        atualizarScore(scoreVermelhoBox, jogo.getJogadorVermelho());
        atualizarScore(scoreAmareloBox,  jogo.getJogadorAmarelo());

        habilitarAcoesPorFase(jogo.getFaseAtual());
        configurarHoverDeMovimento(jogo.getFaseAtual());
    }

    @Override public void mostrarErro(String mensagem) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setTitle("Erro");
        a.setContentText(mensagem);
        a.initOwner(stage);
        a.showAndWait();
    }

    @Override public void mostrarMensagem(String mensagem) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setTitle("Informação");
        a.setContentText(mensagem);
        a.initOwner(stage);
        a.showAndWait();
    }

    // ----------------------- layout -----------------------
    private void montarLayoutBase() {
        stage.setTitle("Haru Ichiban - Primeiro Vento da Primavera");
        root.setPadding(new Insets(12));

        lblTitulo.setFont(Font.font(null, FontWeight.BOLD, 22));
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

        empateBox.setAlignment(Pos.CENTER);
        btnCoaxV.setOnAction(e -> controller.registrarCoaxar(CorJogador.VERMELHO));
        btnCoaxA.setOnAction(e -> controller.registrarCoaxar(CorJogador.AMARELO));

        VBox blocoCentralInfo = new VBox(6,
                new HBox(20, lblTurno, lblFase) {{ setAlignment(Pos.CENTER); }},
                new HBox(lblMsg)               {{ setAlignment(Pos.CENTER); }},
                empateBox
        );

        header.setSpacing(6);
        header.getChildren().addAll(linhaTopo, blocoCentralInfo);
        root.setTop(header);

        StackPane center = new StackPane(boardGrid.getNode());
        center.setPadding(new Insets(8));
        root.setCenter(center);

        TitledPane tpV = new TitledPane("Mão Vermelho", maoVermelhoBox);
        tpV.setCollapsible(false);
        VBox left = new VBox(8, tpV, scoreVermelhoBox);
        left.setPadding(new Insets(8));
        left.setPrefWidth(240);
        root.setLeft(left);

        TitledPane tpA = new TitledPane("Mão Amarelo", maoAmareloBox);
        tpA.setCollapsible(false);
        VBox right = new VBox(8, tpA, scoreAmareloBox);
        right.setPadding(new Insets(8));
        right.setPrefWidth(240);
        root.setRight(right);

        btnNovaRodada.setOnAction(e -> controller.iniciarNovaRodada());

        HBox actions = new HBox(12, btnConfirmarSelecao, btnFloracaoJunior, tglMoverSapo, btnNovaRodada);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(10, 0, 0, 0));
        root.setBottom(actions);
    }

    private void configurarHandlersFixos() {
        btnNovoJogo.setOnAction(e -> solicitarNovoJogo(controller));
        btnRegras.setOnAction(e -> abrirPdfRegras());
        btnFloracaoJunior.setOnAction(e -> controller.executarFloracaoJunior());

        // Clique no tabuleiro
        boardGrid.setOnCellClick((r, c) -> {
            if (jogo == null) return;
            FaseJogo fase = jogo.getFaseAtual();

            // 1) EMPATE: colocar sapos
            if (fase == FaseJogo.MOVER_SAPOS_EMPATE) {
                handleEmpateClick(r, c);
                return;
            }

            // 2) Mover sapo manual (quando necessário)
            if (tglMoverSapo.isSelected() && fase != FaseJogo.EMPATE_COAXAR) {
                handleMoverSapoClick(r, c);
                return;
            }

            // 3) Ações padrão por fase
            switch (fase) {
                case FLORACAO_SENIOR -> controller.executarFloracaoSenior(r, c);
                case SELECAO_NENUFAR_ESCURO -> controller.selecionarNenufarEscuro(r, c);
                default -> { /* setas / outros fluxos */ }
            }
        });

        btnConfirmarSelecao.setOnAction(e -> {
            if (idxFlorVermelho < 0 || idxFlorAmarelo < 0) {
                mostrarErro("Selecione 1 flor do Vermelho e 1 do Amarelo.");
                return;
            }
            controller.selecionarFlores(idxFlorVermelho, idxFlorAmarelo);
            idxFlorVermelho = idxFlorAmarelo = -1;
        });
    }

    // ------------------- setas e movimento por direção -------------------
    private void configurarHoverDeMovimento(FaseJogo fase) {
        if (fase == FaseJogo.MOVIMENTO_NENUFARES) {
            boardGrid.setOnCellHover(onEnterMostrarSetas, boardGrid::clearArrows);
        } else {
            boardGrid.setOnCellHover(null, boardGrid::clearArrows);
            boardGrid.clearArrows();
        }
    }

    private final BiConsumer<Integer, Integer> onEnterMostrarSetas = (r, c) -> {
        if (jogo == null) return;
        Tabuleiro t = jogo.getTabuleiro();
        int v = t.getCelula(r, c);
        if (!isNenufar(v)) { boardGrid.clearArrows(); return; }

        boolean up    = podeEmpurrar(t, r, c, -1, 0);
        boolean down  = podeEmpurrar(t, r, c, +1, 0);
        boolean left  = podeEmpurrar(t, r, c, 0, -1);
        boolean right = podeEmpurrar(t, r, c, 0, +1);

        if (!(up || down || left || right)) { boardGrid.clearArrows(); return; }

        boardGrid.showArrows(r, c, up, down, left, right, dir -> {
            controller.moverNenufarDirecao(r, c, dir);
            boardGrid.clearArrows();
        });
    };

    private boolean isNenufar(int v) {
        return v == 3 || v == 4 || v == 5 || v == 6 || v == 7 || v == 8;
    }

    /**
     * Habilita seta apenas quando o movimento de 1 passo é possível:
     * - vizinha imediata é água (0) -> OK
     * - senão, existe um bloco contíguo de não-água e logo APÓS esse bloco há água dentro do tabuleiro -> OK
     */
    private boolean podeEmpurrar(Tabuleiro t, int r, int c, int dr, int dc) {
        int nr = r + dr, nc = c + dc;
        if (!t.posicaoValida(nr, nc)) return false;

        // vizinha é água -> movimento simples de 1 casa
        if (t.getCelula(nr, nc) == 0) return true;

        // há bloco contíguo: varrer até a primeira água
        int rr = nr, cc = nc;
        while (t.posicaoValida(rr, cc) && t.getCelula(rr, cc) != 0) {
            rr += dr; cc += dc;
        }
        // rr,cc é a célula após o bloco
        if (!t.posicaoValida(rr, cc)) return false;      // sairia da grade
        return t.getCelula(rr, cc) == 0;                 // precisa haver água logo após o bloco
    }

    // ----------------------- mover sapo (toggle) -----------------------
    private void handleMoverSapoClick(int r, int c) {
        Tabuleiro t = jogo.getTabuleiro();
        if (sapoOrigR == null) {
            int v = t.getCelula(r, c);
            if (v == 5 || v == 6) {
                sapoOrigR = r; sapoOrigC = c;
                lblMsg.setText("Selecione um nenúfar claro vazio para destino.");
            } else {
                mostrarErro("Clique primeiro num sapo para escolher a origem.");
            }
            return;
        }
        // destino
        controller.moverSapo(sapoOrigR, sapoOrigC, r, c);
        sapoOrigR = sapoOrigC = null; // limpa seleção sempre (evita travar)
    }

    // ----------------------- empate: colocar sapos -----------------------
    private void handleEmpateClick(int r, int c) {
        if (empateProximaCor == null) {
            empateProximaCor = CorJogador.VERMELHO; // segurança
        }
        controller.moverSapoEmpate(r, c, empateProximaCor);
        // Após a atualização, atualizarTela calculará novamente a próxima cor
    }

    /** Inspeciona o tabuleiro para decidir quem é o próximo a ser colocado no empate. */
    private CorJogador detectarProximaCorEmpate(Tabuleiro t) {
        boolean temVermelho = false, temAmarelo = false;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int v = t.getCelula(i, j);
                if (v == 5) temVermelho = true;
                if (v == 6) temAmarelo = true;
            }
        }
        if (!temVermelho) return CorJogador.VERMELHO;
        if (!temAmarelo)   return CorJogador.AMARELO;
        return null; // os dois já estão no tabuleiro (fase deverá avançar)
    }

    // ----------------------- render mãos / score -----------------------
    private void renderMaos(JogoHaruIchiban jogo) {
        cardsVermelho.clear();
        cardsAmarelo.clear();
        maoVermelhoBox.getChildren().setAll(renderMao(jogo.getJogadorVermelho(), true));
        maoAmareloBox.getChildren().setAll(renderMao(jogo.getJogadorAmarelo(), false));
        updateSelectionStyles(true,  idxFlorVermelho);
        updateSelectionStyles(false, idxFlorAmarelo);
    }

    private Node[] renderMao(Jogador jogador, boolean vermelho) {
        List<Flor> mao = jogador.getMao();
        Node[] nos = new Node[mao.size()];
        for (int i = 0; i < mao.size(); i++) {
            Flor f = mao.get(i);

            Node imgPlain  = SpriteFactory.get().flowerGeneric(f.getCor());
            Node imgNumber = SpriteFactory.get().flowerNumbered(f.getCor(), f.getValor());
            StackPane holder = new StackPane(imgPlain, imgNumber);
            holder.setPrefSize(100, 100);
            imgNumber.setVisible(false);

            ToggleButton btnVer = new ToggleButton("Ver");
            btnVer.setOnAction(e -> {
                boolean show = btnVer.isSelected();
                imgNumber.setVisible(show);
                imgPlain.setVisible(!show);
            });

            Button btnSel = new Button("Selecionar");
            final int idx = i;
            btnSel.setOnAction(e -> {
                if (vermelho) { idxFlorVermelho = idx; updateSelectionStyles(true, idx); }
                else          { idxFlorAmarelo  = idx; updateSelectionStyles(false, idx); }
            });

            HBox actions = new HBox(6, btnVer, btnSel);
            actions.setAlignment(Pos.CENTER);

            VBox card = new VBox(6, holder, actions);
            card.setPadding(new Insets(8));
            card.setAlignment(Pos.CENTER);
            card.setStyle(cardStyle(false));

            if (vermelho) cardsVermelho.add(card); else cardsAmarelo.add(card);
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
        return selected ? """
            -fx-background-color: #ffffff;
            -fx-border-color: #2b8a3e;
            -fx-border-width: 2;
            -fx-border-radius: 10;
            -fx-background-radius: 10;
        """ : """
            -fx-background-color: #ffffff;
            -fx-border-color: #ddd;
            -fx-border-width: 1;
            -fx-border-radius: 10;
            -fx-background-radius: 10;
        """;
    }

    private void atualizarScore(VBox box, Jogador j) {
        Label titulo = new Label(j.getNome() + " — Pontos: " + j.getPontuacao());
        HBox pips = new HBox(6);
        pips.setAlignment(Pos.CENTER_LEFT);

        for (int i = 1; i <= 5; i++) {
            Region pip = new Region();
            pip.setPrefSize(16,16);
            String on  = (j.getCor()== CorJogador.VERMELHO) ? "#d93b53" : "#f2c200";
            String off = "#e0e0e0";
            pip.setStyle("-fx-background-color:" + (i<=j.getPontuacao()? on : off) +
                    ";-fx-background-radius:999;-fx-border-radius:999;-fx-border-color:#ccc;");
            pips.getChildren().add(pip);
        }
        box.getChildren().setAll(new VBox(4, titulo, pips));
    }

    // ---------- novo jogo + regras ----------
    public void solicitarNovoJogo(GameController controller) {
        TextInputDialog d1 = new TextInputDialog("Vermelho");
        d1.setHeaderText("Nome do Jogador Vermelho");
        String nV = d1.showAndWait().orElse("Vermelho");

        TextInputDialog d2 = new TextInputDialog("Amarelo");
        d2.setHeaderText("Nome do Jogador Amarelo");
        String nA = d2.showAndWait().orElse("Amarelo");

        controller.iniciarNovoJogo(nV, nA);
        idxFlorVermelho = idxFlorAmarelo = -1;
        tglMoverSapo.setSelected(false);
        sapoOrigR = sapoOrigC = null;
    }

    private void abrirPdfRegras() {
        try {
            URL url = getClass().getResource("/haruichiban/regras/regras.pdf");
            if (url != null) {
                hostServices.showDocument(url.toExternalForm());
                return;
            }
            mostrarErro("Regras não encontradas nos recursos.");
        } catch (Exception ex) {
            mostrarErro("Não foi possível abrir as regras: " + ex.getMessage());
        }
    }

    // ---------- util ----------
    private void habilitarAcoesPorFase(FaseJogo fase) {
        boolean selecaoFlor = (fase == FaseJogo.SELECAO_FLOR);
        btnConfirmarSelecao.setDisable(!selecaoFlor);

        btnFloracaoJunior.setDisable(fase != FaseJogo.FLORACAO_JUNIOR);

        // Mostrar/esconder UI do empate
        empateBox.setVisible(fase == FaseJogo.EMPATE_COAXAR);
        empateBox.setManaged(fase == FaseJogo.EMPATE_COAXAR);

        // Botão "Nova Rodada" só aparece no FIM_RODADA
        boolean mostrarNovaRodada = (fase == FaseJogo.FIM_RODADA);
        btnNovaRodada.setVisible(mostrarNovaRodada);
        btnNovaRodada.setManaged(mostrarNovaRodada);
        btnNovaRodada.setDisable(!mostrarNovaRodada);

        // Toggle mover sapo não faz sentido durante EMPATE_COAXAR ou MOVER_SAPOS_EMPATE
        boolean podeMoverSapo = (fase != FaseJogo.EMPATE_COAXAR && fase != FaseJogo.MOVER_SAPOS_EMPATE && fase != FaseJogo.FIM_RODADA);
        tglMoverSapo.setDisable(!podeMoverSapo);
        if (!podeMoverSapo) {
            tglMoverSapo.setSelected(false);
            sapoOrigR = sapoOrigC = null;
        }
    }
}
