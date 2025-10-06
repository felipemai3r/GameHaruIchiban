package haruichiban;

import haruichiban.Controller.GameController;
import haruichiban.View.FxGameView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Ponto de entrada da interface JavaFX.
 * Mantém o Main.java antigo para os testes de console (se quiser).
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        FxGameView view = new FxGameView(stage, getHostServices());
        GameController controller = new GameController(view);
        view.attachController(controller);

        Scene scene = new Scene(view.getRoot(), 1000, 720);
        stage.setTitle("Haru Ichiban – UI (MVC)");
        stage.setScene(scene);
        stage.show();

        controller.iniciarNovoJogo("Vermelho", "Amarelo");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
