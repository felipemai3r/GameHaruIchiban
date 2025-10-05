package haruichiban;

import haruichiban.Controller.GameController;
import haruichiban.View.FxGameView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // View recebe o HostServices para abrir o PDF de regras
        FxGameView view = new FxGameView(stage, getHostServices());
        GameController controller = new GameController(view);
        view.attachController(controller);

        Scene scene = new Scene(view.getRoot(), 1280, 800);
        stage.setTitle("Haru Ichiban - Primeiro Vento da Primavera");
        stage.setScene(scene);
        stage.show();

        // Já inicia o fluxo equivalente ao botão "Novo Jogo"
        view.solicitarNovoJogo(controller);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
