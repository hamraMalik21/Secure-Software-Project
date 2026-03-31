import javafx.application.Application;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        //System.out.println(BCrypt.hashpw("staff",BCrypt.gensalt(12)));
        MainView mainView = new MainView(primaryStage);
        mainView.initializeComponents();
    }

    public static void main(String[] args) {
        launch(args);
    }
}