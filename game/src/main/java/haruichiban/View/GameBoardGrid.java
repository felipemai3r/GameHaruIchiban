package haruichiban.View;

import java.util.function.BiConsumer;

import haruichiban.Model.Tabuleiro;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/** Grid 5x5 que renderiza o Tabuleiro usando a SpriteFactory. */
public class GameBoardGrid {

    private final GridPane grid = new GridPane();
    private BiConsumer<Integer, Integer> onCellClick = (r, c) -> {};

    public GameBoardGrid() {
        grid.setHgap(4);
        grid.setVgap(4);
        grid.setAlignment(Pos.CENTER);
    }

    public Node getNode() { return grid; }

    public void setOnCellClick(BiConsumer<Integer, Integer> handler) {
        this.onCellClick = (handler != null) ? handler : (r, c) -> {};
    }

    /** Recria os 25 nós com base no estado atual do Tabuleiro */
    public void render(Tabuleiro tab) {
        grid.getChildren().clear();
        for (int l = 0; l < 5; l++) {
            for (int c = 0; c < 5; c++) {
                int code = tab.getCelula(l, c);
                StackPane cell = SpriteFactory.get().tileFor(code);
                final int rl = l, rc = c;
                cell.setOnMouseClicked(ev -> onCellClick.accept(rl, rc));
                grid.add(cell, c, l);
            }
        }
    }
}
