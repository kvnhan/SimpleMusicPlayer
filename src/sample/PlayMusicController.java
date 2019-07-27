package sample;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Callback;
import sun.awt.windows.ThemeReader;

import java.io.File;
import java.net.URI;
import java.net.URL;

public class PlayMusicController{
    @FXML
    private Button backwardButton;
    @FXML
    private Button playButton;
    @FXML
    private Button forwardButton;
    @FXML
    private Slider timeBar, volumeSlider;
    private static double dragDist;
    @FXML
    private ImageView imageView;
    @FXML
    private ImageView volumeImage;

    private Media media;
    private MediaPlayer mediaPlayer = null;
    private Scene main;
    private String volumeFile1 = "/Images/volume1.png";
    private String volumeFile2 = "/Images/volume2.png";
    private URL imageURI = null;
    private MusicContainer musicContainer;
    private final double startingVloume = 50;

    private MusicQueue musicQueue;
    private Thread checkStoppage;

    public PlayMusicController(){
    }

    public void setScene(Scene scene){
        this.musicContainer = new MusicContainer(scene);
        musicQueue = MusicQueue.getInstance();
        this.main = scene;
        this.timeBar = (Slider) scene.lookup("#timeBar");
        this.volumeSlider = (Slider) scene.lookup("#volumeSlider");
        this.volumeImage = (ImageView) scene.lookup("#volumeImage");
        this.playButton = (Button) scene.lookup("#playButton");
        this.backwardButton = (Button) scene.lookup("#backwardButton");
        this.forwardButton = (Button) scene.lookup("#forwardButton");
        resetBarAtStart();
        setPlayButton();
        setUpPlayMusic();
        setForwardButton();
        setBackwardButtonButton();
        updateBar();
        setUpImage();
        autoPlay();
        changePlayText();
    }
    public void setPlayButton(){
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (mediaPlayer != null){
                    if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING){
                        mediaPlayer.pause();
                    }else if (mediaPlayer.getStatus() == MediaPlayer.Status.READY ||
                        mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED ||
                        mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED ) {
                        mediaPlayer.play();
                    }
                }
            }
        });
    }
    public void setBackwardButtonButton(){
        backwardButton.setOnAction(event -> System.out.println("Prev Music"));

    }
    public void setForwardButton(){
        forwardButton.setOnAction(event ->  {
            System.out.println("Forward");
        });

    }
    public void resetBarAtStart(){
        timeBar.setValue(0.0);
    }
    public void updateBar(){
        timeBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dragDist = timeBar.getValue();
                System.out.println(dragDist);
            }
        });

    }
    public void setUpImage(){
        try {
            if(volumeSlider.getValue() == 0){
                imageURI = getClass().getResource(volumeFile2);
            }else{
                imageURI = getClass().getResource(volumeFile1);
            }
            Image image = new Image(imageURI.toString());
            volumeImage.setImage(image);
        }catch(Exception e){
            e.printStackTrace();
        }
        updateVolumeImage();
        updateVolume();
    }
    public void updateVolumeImage(){
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.doubleValue() == 0){
                    imageURI = getClass().getResource(volumeFile2);
                }else{
                    imageURI = getClass().getResource(volumeFile1);
                }
                Image image = new Image(imageURI.toString());
                volumeImage.setImage(image);
            }
        });
    }
    public void updateVolume(){
        volumeSlider.setValue(startingVloume);
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if(volumeSlider.isValueChanging()){
                    if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                        mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
                    }
                }
            }
        });
    }
    public void setUpPlayMusic(){
        System.out.println("Play this music");
        Thread musicThread = new Thread(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                if (musicQueue.isFileIsReady()) {
                    if(mediaPlayer != null){
                        checkStoppage.interrupt();
                        mediaPlayer.stop();
                        mediaPlayer = null;
                        autoPlay();
                    }
                    setUpMediaPlayer(musicQueue.getCurrentFile().getFilePath());
                    musicQueue.setFileIsReady(false);
                }
            }
        });
        musicThread.setName("musicThread");
        musicThread.setDaemon(true);
        musicThread.start();
    }
    public void setUpMediaPlayer(String file){
        URI resource = new File(file).toURI();
        media = new Media(resource.toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(startingVloume / 100.0);
        updateBarMaxLim();
        checkStoppage.start();
        mediaPlayer.play();
    }
    public void updateBarMaxLim(){
        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if(!timeBar.isValueChanging()){
                timeBar.setValue(newTime.toSeconds());
            }
        });
    }
    public void autoPlay(){
        checkStoppage = new Thread(() -> {
           while(!Thread.currentThread().isInterrupted()){
               if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                   if (timeBar.getMax() == timeBar.getValue()) {
                       System.out.println("Music Stopped");
                       FileNode fn = new FileNode(musicQueue.getCurrentFile().getFilePath(),null, null);
                       musicQueue.setPrevFile(fn);
                       mediaPlayer.stop();
                       mediaPlayer.dispose();
                   }
               }
           }
        });
        checkStoppage.setDaemon(true);
    }
    public void changePlayText(){
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (true) {
                    Platform.runLater(() -> {
                        if(mediaPlayer != null){
                            if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING){
                                playButton.setText("Pause");
                            }else if (mediaPlayer.getStatus() == MediaPlayer.Status.READY ||
                                    mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED ||
                                    mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED ) {
                                playButton.setText("Play");
                            }
                        }
                    });
                    Thread.sleep(1000);
                }
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }


}
