package joy_market.main;
import javafx.application.Application;
import javafx.stage.Stage;
import joy_market.windows.RegisterWindow;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        new RegisterWindow().start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
