package haruichiban.View;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * SpriteFactory = Abstract Factory + Singleton:
 * - cria nós (tiles) a partir do "código" da célula (0..8)
 * - carrega imagens uma única vez (cache) a partir de /haruichiban/img/
 */
public class SpriteFactory {

    private static final SpriteFactory INSTANCE = new SpriteFactory();
    public static SpriteFactory get() { return INSTANCE; }

    private static final String BASE = "/haruichiban/img/";
    private final Map<String, Image> cache = new HashMap<>();

    private SpriteFactory() {}

    /** Monta um tile para o código do Tabuleiro. */
    public StackPane tileFor(int code) {
        StackPane s = new StackPane();
        s.setPrefSize(96, 96);
        s.setAlignment(Pos.CENTER);

        switch (code) {
            case 0 -> s.getChildren().add(img("agua"));
            case 3 -> s.getChildren().add(img("nenufar_claro"));
            case 4 -> s.getChildren().add(img("nenufar_escuro"));
            case 5 -> { s.getChildren().addAll(img("nenufar_claro"), img("sapo_vermelho")); }
            case 6 -> { s.getChildren().addAll(img("nenufar_claro"), img("sapo_amarelo")); }
            case 7 -> { s.getChildren().addAll(img("nenufar_claro"), img("flor_vermelha")); }
            case 8 -> { s.getChildren().addAll(img("nenufar_claro"), img("flor_amarela")); }
            default -> s.getChildren().add(img("agua"));
        }
        return s;
    }

    /** Tenta a flor numerada (ex.: "flor_vermelha_5"), senão cai na genérica. */
    public Node flowerNumberedOrDefault(String base, int valor) {
        String name = base + valor;
        ImageView v = imgOrNull(name);
        if (v == null) {
            return img(base.startsWith("flor_vermelha") ? "flor_vermelha" : "flor_amarela");
        }
        return v;
    }

    // ------------- helpers de imagem/caching -------------

    private ImageView img(String name) {
        Image image = cache.computeIfAbsent(name, this::load);
        ImageView iv = new ImageView(image);
        iv.setPreserveRatio(true);
        iv.setFitWidth(84);
        return iv;
    }

    private ImageView imgOrNull(String name) {
        Image image = cache.computeIfAbsent(name, this::loadNullable);
        if (image == null) return null;
        ImageView iv = new ImageView(image);
        iv.setPreserveRatio(true);
        iv.setFitWidth(84);
        return iv;
    }

    private Image load(String name) {
        Image i = loadNullable(name);
        if (i == null) throw new IllegalStateException("Imagem não encontrada: " + name);
        return i;
    }

    private Image loadNullable(String name) {
        // tenta com e sem .png
        String[] candidates = new String[] { BASE + name, BASE + name + ".png" };
        for (String path : candidates) {
            try (InputStream in = getClass().getResourceAsStream(path)) {
                if (in != null) return new Image(in);
            } catch (Exception ignored) {}
        }
        return null;
    }
}
