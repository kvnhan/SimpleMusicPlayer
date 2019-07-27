package sample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int width = 900;
    private static final int height = 700;
    private MusicFileSystem musicFileSystem;


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("PlayMusicView.fxml"));
        Scene sc = new Scene(root, width, height);
        primaryStage.setScene(sc);
        primaryStage.show();
        PlayMusicController pmc =  new PlayMusicController();
        pmc.setScene(sc);
        sample.SceneController sceneController= new sample.SceneController(sc);
        musicFileSystem = MusicFileSystem.getInstance();
    }


    public static void main(String[] args) {
        launch(args);

    }
}
