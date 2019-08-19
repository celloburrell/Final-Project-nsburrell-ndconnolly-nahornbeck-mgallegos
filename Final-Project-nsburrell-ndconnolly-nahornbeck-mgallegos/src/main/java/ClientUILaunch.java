import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

    public class ClientUILaunch extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception{
            Parent root = FXMLLoader.load(getClass().getResource("/ClientUI.fxml"));
            primaryStage.setTitle("ClientUI");
            primaryStage.setScene(new Scene(root, 665, 400));
            primaryStage.show();
        }
    }

