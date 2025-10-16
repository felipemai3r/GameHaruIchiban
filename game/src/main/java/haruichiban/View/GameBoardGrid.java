package haruichiban.View;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import haruichiban.Model.Tabuleiro;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * Grid 5x5 que renderiza o Tabuleiro (int[5][5]) usando a SpriteFactory.
 * Suporta "setas" sobre a célula para escolher direção (C,B,E,D).
 */
public class GameBoardGrid {
    private final GridPane grid = new GridPane();
    private final StackPane[][] cells = new StackPane[5][5];

    private BiConsumer<Integer, Integer> onCellClick = (r, c) -> {};
    private BiConsumer<Integer, Integer> onCellEnter = null;
    private Runnable onCellExit = null;

    private final List<Node> activeArrows = new ArrayList<>();

    public GameBoardGrid() {
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setAlignment(Pos.CENTER);
    }

    public Node getNode() {
        return grid;
    }

    public void setOnCellClick(BiConsumer<Integer, Integer> handler) {
        this.onCellClick = (handler != null ? handler : (r, c) -> {});
    }

    /** Define handlers de hover (para mostrar/ocultar setas). */
    public void setOnCellHover(BiConsumer<Integer, Integer> onEnter, Runnable onExit) {
        this.onCellEnter = onEnter;
        this.onCellExit = onExit;
    }

    public void render(Tabuleiro tabuleiro) {
        grid.getChildren().clear();
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                StackPane tile = SpriteFactory.get().tileFor(tabuleiro.getCelula(r, c));
                final int rr = r, cc = c;
                tile.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) onCellClick.accept(rr, cc);
                });
                tile.setOnMouseEntered(e -> {
                    if (onCellEnter != null) onCellEnter.accept(rr, cc);
                });
                tile.setOnMouseExited(e -> {
                    if (onCellExit != null) onCellExit.run();
                });
                cells[r][c] = tile;
                grid.add(tile, c, r);
            }
        }
    }

    public void clearArrows() {
        for (Node n : new ArrayList<>(activeArrows)) {
            if (n.getParent() instanceof StackPane p) p.getChildren().remove(n);
        }
        activeArrows.clear();
    }

    /**
     * Mostra up/down/left/right como botões arredondados nas bordas da célula.
     * Ao clicar, dispara onPick com 'C', 'B', 'E', 'D'.
     */
    public void showArrows(int r, int c, boolean up, boolean down, boolean left, boolean right, Consumer<Character> onPick) {
        clearArrows();
        StackPane cell = cells[r][c];
        if (cell == null) return;

        if (up) activeArrows.add(addArrow(cell, 'C', "↑", Pos.TOP_CENTER, onPick));
        if (down) activeArrows.add(addArrow(cell, 'B', "↓", Pos.BOTTOM_CENTER, onPick));
        if (left) activeArrows.add(addArrow(cell, 'E', "←", Pos.CENTER_LEFT, onPick));
        if (right) activeArrows.add(addArrow(cell, 'D', "→", Pos.CENTER_RIGHT, onPick));
    }

    private Node addArrow(StackPane cell, char dir, String label, Pos pos, Consumer<Character> onPick) {
        Button b = new Button(label);
        b.setFocusTraversable(false);
        b.setStyle("""
            -fx-background-color: rgba(255,255,255,0.92);
            -fx-background-radius: 999;
            -fx-border-radius: 999;
            -fx-border-color: #7a7a7a;
            -fx-font-weight: bold;
            -fx-padding: 2 8 2 8;
            """);
        b.setOnAction(e -> onPick.accept(dir));
        StackPane.setAlignment(b, pos);
        StackPane.setMargin(b, new Insets(2));
        cell.getChildren().add(b);
        return b;
    }
}
