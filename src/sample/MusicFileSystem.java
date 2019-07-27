package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.HashMap;

public class MusicFileSystem {
    private static final MusicFileSystem musicFileSystem = new MusicFileSystem();
    private static ObservableList<Music> list;

    private MusicFileSystem(){}

    public static MusicFileSystem getInstance(){
        return musicFileSystem;
    }

    public ObservableList<Music> retrieveMusic(File musicFolder){
        File[] listOfFiles = musicFolder.listFiles();
        list = FXCollections.observableArrayList();
        int fileLength = listOfFiles.length;
        int x = 0;
        while(x < fileLength){
            Music music = new Music(listOfFiles[x].getName(), "", "", 0, listOfFiles[x].getAbsolutePath());
            list.add(music);
            x++;
        }
        return list;
    }

    public ObservableList<Music> getMusicList(){
        return list;
    }

}
