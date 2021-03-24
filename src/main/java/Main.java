import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.net.URL;
import java.nio.file.Paths;

public class Main extends Application
{
    @Override
    public void start(Stage stage) {
        try {
            URL url = Paths.get("./src/main/java/fxml/application.fxml").toUri().toURL();
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root);

            URL iconUrl = Paths.get("./src/main/java/images/icon3.png").toUri().toURL();
            stage.getIcons().add(new Image(iconUrl.toString()));
            stage.setMaximized(true);
            stage.setTitle("Drawing Panel");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
