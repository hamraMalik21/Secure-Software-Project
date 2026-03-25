import javafx.application.Application;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        //System.out.println(BCrypt.hashpw("staff",BCrypt.gensalt(12)));
        UserLogin loginView = new UserLogin(primaryStage);
        loginView.initializeComponents();
    }

    public static void main(String[] args) {
        launch(args);
    }
}