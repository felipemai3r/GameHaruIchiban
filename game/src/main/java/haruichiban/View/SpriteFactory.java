package haruichiban.View;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import haruichiban.Model.enums.CorJogador;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * SpriteFactory = Abstract Factory + Singleton:
 * - cria nós (tiles) a partir do código da célula (0..8)
 * - cria flores genéricas e numeradas para as mãos
 * - carrega imagens 1x (cache) de /haruichiban/img/
 */
public class SpriteFactory {
    private static final SpriteFactory INSTANCE = new SpriteFactory();
    public static SpriteFactory get() { return INSTANCE; }

    private static final String BASE = "/haruichiban/img/";
    private final Map<String, Image> cache = new HashMap<>();

    // tamanho base (ajustado p/ caber sem scroll)
    private static final double TILE_SIZE = 88;
    private static final double IMAGE_FIT = 0.88 * TILE_SIZE;

    private SpriteFactory() {}

    /** Monta um tile para o código do Tabuleiro. */
    public StackPane tileFor(int code) {
        StackPane s = new StackPane();
        s.setPrefSize(TILE_SIZE, TILE_SIZE);
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

    // ---------- API para as mãos ----------
    /** Flor sem número (lado “virado”). */
    public Node flowerGeneric(CorJogador cor) {
        return img(cor == CorJogador.VERMELHO ? "flor_vermelha" : "flor_amarela");
    }

    /** Flor numerada (se não houver asset numerado, cai na genérica). */
    public Node flowerNumbered(CorJogador cor, int valor) {
        String base = (cor == CorJogador.VERMELHO) ? "flor_vermelha_" : "flor_amarela_";
        ImageView v = imgOrNull(base + valor);
        if (v != null) return v;
        return flowerGeneric(cor);
    }

    // ---------- helpers de imagem/caching ----------
    private ImageView img(String name) {
        Image image = cache.computeIfAbsent(name, this::load);
        ImageView iv = new ImageView(image);
        iv.setPreserveRatio(true);
        iv.setFitWidth(IMAGE_FIT);
        return iv;
    }

    private ImageView imgOrNull(String name) {
        Image image = cache.computeIfAbsent(name, this::loadNullable);
        if (image == null) return null;
        ImageView iv = new ImageView(image);
        iv.setPreserveRatio(true);
        iv.setFitWidth(IMAGE_FIT);
        return iv;
    }

    private Image load(String name) {
        Image i = loadNullable(name);
        if (i == null) throw new IllegalStateException("Imagem não encontrada: " + name);
        return i;
    }

    private Image loadNullable(String name) {
        String[] candidates = new String[] { BASE + name, BASE + name + ".png" };
        for (String path : candidates) {
            try (InputStream in = getClass().getResourceAsStream(path)) {
                if (in != null) return new Image(in);
            } catch (Exception ignored) {}
        }
        return null;
    }
}
