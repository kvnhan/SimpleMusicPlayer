package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;

import java.io.File;

public class MusicContainer {
    @FXML
    private ListView<Music> songListView;
    @FXML
    private MenuButton sortButton, chooseFileButton;
    private ObservableList<Music> musics = FXCollections.observableArrayList();
    //private PlayMusicController playMusicController;
    private String absolutePath;
    private FolderCache folderCache;
    private File currentFolder = null;
    private MusicFileSystem musicFileSystem = MusicFileSystem.getInstance();
    private MusicQueue musicQueue = MusicQueue.getInstance();

    public MusicContainer(Scene scene){
        this.songListView = (ListView) scene.lookup("#songListView");
        this.sortButton = (MenuButton) scene.lookup("#sortButton");
        this.chooseFileButton = (MenuButton) scene.lookup("#chooseFileButton");
        setUpButtons();
        setUpList();
        updateList();
        folderCache = FolderCache.getInstance();
    }

    public ListView<Music> getSongListView(){
        return songListView;
    }
    public void setUpList(){
        songListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String filename = songListView.getSelectionModel().getSelectedItems().get(0).getFilePath();
                System.out.println(filename);
                FileNode fn = new FileNode(filename,null,null);
                musicQueue.setCurrentFile(fn);
                fn.setPrevNode(musicQueue.getPrevFile());
                musicQueue.setFileIsReady(true);
            }
        });
    }

    public void updateList(){
        songListView.setCellFactory(new Callback<ListView<Music>, ListCell<Music>>(){
            @Override
            public ListCell<Music> call(ListView<Music> m){
                ListCell<Music> cell = new ListCell<Music>(){
                    @Override
                    protected void updateItem(Music music, boolean bln) {
                        super.updateItem(music, bln);
                        if (music != null) {
                            setText(music.getName());
                        }
                    }
                };
                return cell;
            }});
    }

    public void setUpButtons(){
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                openFileChooser();
                musics = musicFileSystem.retrieveMusic(currentFolder);
                songListView.setItems(musics);
            }
        };
        chooseFileButton.getItems().get(0).setOnAction(event);
    }

    public String readMusicFiles(String fileFolder){
        absolutePath = new File(fileFolder).getAbsolutePath();
        File musicFolder = new File(absolutePath);
        folderCache.cacheMusicFolder(musicFolder.toString());
        MenuItem menuItem = new MenuItem(musicFolder.toString());
        if(!hasDups(musicFolder.toString())){
            chooseFileButton.getItems().add(menuItem);
        }
        currentFolder = musicFolder;
        return musicFolder.toString();
    }

    public String openFileChooser(){
        DirectoryChooser fileChooser = new DirectoryChooser();
        File selectedFile = fileChooser.showDialog(null);
        return readMusicFiles(selectedFile.toString());
    }

    public boolean hasDups(String id){
        ObservableList<MenuItem> items = chooseFileButton.getItems();

        for(MenuItem mi: items){
            if(mi.getText().equals(id)){
                return true;
            }
        }

        return false;
    }

}
