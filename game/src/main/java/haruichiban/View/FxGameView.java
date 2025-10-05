package haruichiban.View;

import java.util.List;
import java.util.Optional;

import haruichiban.Controller.GameController;
import haruichiban.Model.Flor;
import haruichiban.Model.Jogador;
import haruichiban.Model.JogoHaruIchiban;
import haruichiban.Model.enums.CorJogador;
import haruichiban.Model.enums.FaseJogo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
    private final BorderPane root = new BorderPane();
    private final VBox header = new VBox();
    private final HBox scoreboard = new HBox(20);
    private final Label lblTurno = new Label();
    private final Label lblFase = new Label();
    private final Label lblMsg = new Label();

    private final VBox maoVermelhoBox = new VBox(6);
    private final VBox maoAmareloBox = new VBox(6);
    private int idxFlorVermelho = -1;
    private int idxFlorAmarelo = -1;
    private final Button btnConfirmarSelecao = new Button("Confirmar seleção");

    private final GameBoardGrid boardGrid = new GameBoardGrid();

    private final Button btnNovoJogo = new Button("Novo Jogo");
    private final Button btnFloracaoJunior = new Button("Floração Júnior");
    private final ToggleButton tglMoverSapo = new ToggleButton("Modo Mover Sapo");

    private Integer origemL = null, origemC = null;

    private GameController controller;
    private JogoHaruIchiban jogo;

    public FxGameView(Stage stage) {
        this.stage = stage;
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

        scoreboard.getChildren().setAll(
                tagScore(jogo.getJogadorVermelho()),
                tagScore(jogo.getJogadorAmarelo())
        );

        boardGrid.render(jogo.getTabuleiro());
        renderMaos(jogo);
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

        // Cabeçalho (título + placar + info)
        Label titulo = new Label("Haru Ichiban – Protótipo MVC");
        titulo.setFont(Font.font(null, FontWeight.BOLD, 20));

        lblTurno.setFont(Font.font(14));
        lblFase.setFont(Font.font(14));
        lblMsg.setFont(Font.font(13));
        scoreboard.setAlignment(Pos.CENTER_LEFT);

        HBox linhaInfo = new HBox(20, lblTurno, lblFase);
        linhaInfo.setAlignment(Pos.CENTER_LEFT);

        HBox linhaTopo = new HBox(20, titulo, btnNovoJogo);
        linhaTopo.setAlignment(Pos.CENTER_LEFT);

        header.setSpacing(8);
        header.getChildren().addAll(linhaTopo, scoreboard, linhaInfo, lblMsg);
        root.setTop(header);

        // CENTRO com SCROLL: tabuleiro
        ScrollPane centerScroll = new ScrollPane(boardGrid.getNode());
        centerScroll.setFitToWidth(true);
        centerScroll.setFitToHeight(true);
        centerScroll.setPannable(true);
        centerScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        centerScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        root.setCenter(centerScroll);

        // LADO DIREITO com SCROLL: mãos e ações
        TitledPane tpV = new TitledPane("Mão Vermelho", maoVermelhoBox);
        TitledPane tpA = new TitledPane("Mão Amarelo", maoAmareloBox);
        tpV.setCollapsible(false);
        tpA.setCollapsible(false);

        VBox right = new VBox(10, tpV, tpA, btnConfirmarSelecao, new Separator(),
                btnFloracaoJunior, tglMoverSapo);
        right.setPadding(new Insets(8));
        right.setFillWidth(true);
        right.setPrefWidth(220); // largura confortável para as mãos

        ScrollPane rightScroll = new ScrollPane(right);
        rightScroll.setFitToWidth(true);
        rightScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rightScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        root.setRight(rightScroll);

        // Estilo simples
        btnConfirmarSelecao.setMaxWidth(Double.MAX_VALUE);
        btnFloracaoJunior.setMaxWidth(Double.MAX_VALUE);
        tglMoverSapo.setMaxWidth(Double.MAX_VALUE);
    }

    private void configurarHandlersFixos() {
        btnNovoJogo.setOnAction(e -> {
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
        });

        btnFloracaoJunior.setOnAction(e -> controller.executarFloracaoJunior());

        boardGrid.setOnCellClick((l, c) -> {
            if (jogo == null) return;
            FaseJogo fase = jogo.getFaseAtual();

            if (tglMoverSapo.isSelected()) {
                handleMoverSapo(l, c);
                return;
            }

            switch (fase) {
                case SELECAO_FLOR -> mostrarMensagem(
                        "Selecione 1 flor de cada mão e clique em 'Confirmar seleção'.");
                case FLORACAO_JUNIOR -> mostrarMensagem(
                        "Use 'Floração Júnior'. Se houver sapo no escuro, ative 'Modo Mover Sapo'.");
                case FLORACAO_SENIOR -> controller.executarFloracaoSenior(l, c);
                case MOVIMENTO_NENUFARES -> handleMoverNenufar(l, c);
                case SELECAO_NENUFAR_ESCURO -> controller.selecionarNenufarEscuro(l, c);
                case FIM_RODADA -> mostrarMensagem("Fim da rodada. Clique em 'Novo Jogo' ou aguarde o backend.");
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
        maoVermelhoBox.getChildren().setAll(renderMao(jogo.getJogadorVermelho(), true));
        maoAmareloBox.getChildren().setAll(renderMao(jogo.getJogadorAmarelo(), false));
    }

    private Node[] renderMao(Jogador jogador, boolean vermelho) {
        List<Flor> mao = jogador.getMao();
        Node[] nos = new Node[mao.size()];
        ToggleGroup group = new ToggleGroup();

        for (int i = 0; i < mao.size(); i++) {
            Flor f = mao.get(i);
            String base = (f.getCor() == CorJogador.VERMELHO) ? "flor_vermelha_" : "flor_amarela_";
            Node img = SpriteFactory.get().flowerNumberedOrDefault(base, f.getValor());

            ToggleButton tb = new ToggleButton("", img);
            tb.setToggleGroup(group);
            tb.setMaxWidth(Double.MAX_VALUE);
            tb.setUserData(i);

            tb.setOnAction(e -> {
                int idx = (int) tb.getUserData();
                if (vermelho) idxFlorVermelho = idx; else idxFlorAmarelo = idx;
            });
            nos[i] = tb;
        }
        return nos;
    }

    private void handleMoverNenufar(int l, int c) {
        if (origemL == null) {
            origemL = l; origemC = c;
            lblMsg.setText("Origem: (" + l + "," + c + "). Selecione um destino adjacente.");
        } else {
            controller.moverNenufar(origemL, origemC, 'D');
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

    private Node tagScore(Jogador j) {
        Label nome = new Label(j.getNome());
        Label pontos = new Label("Pontos: " + j.getPontuacao());
        VBox v = new VBox(2, nome, pontos);
        v.setPadding(new Insets(6));
        v.setStyle("-fx-background-color: #f3f3f3; -fx-border-color: #ddd; -fx-border-radius: 8; -fx-background-radius: 8;");
        return v;
    }
}
