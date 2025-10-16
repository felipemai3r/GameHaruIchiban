package haruichiban;

import haruichiban.Controller.GameController;
import haruichiban.View.FxGameView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        FxGameView view = new FxGameView(stage, getHostServices());
        GameController controller = new GameController(view);
        view.attachController(controller);

        Scene scene = new Scene(view.getRoot(), 1000, 720);
        stage.setScene(scene);
        stage.show();

        // Já inicia pedindo os nomes (e criando um jogo novo)
        view.solicitarNovoJogo(controller);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
