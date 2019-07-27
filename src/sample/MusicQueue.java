package sample;


import javafx.collections.ObservableList;

import java.io.File;
import java.util.LinkedList;

public class MusicQueue {
    private static MusicQueue musicQueue = new MusicQueue();
    private static FileNode currentFile;
    private static FileNode prevFile = currentFile;
    private static Thread currentThread;
    private static boolean fileIsReady = false;

    public static MusicQueue getInstance(){
        return musicQueue;
    }

    public static FileNode getCurrentFile() {
        return currentFile;
    }

    public static void setCurrentFile(FileNode currentFile) {
        MusicQueue.currentFile = currentFile;
    }

    public MusicQueue(){}

    public static boolean isFileIsReady() {
        return fileIsReady;
    }

    public static void setFileIsReady(boolean fileIsReady) {
        MusicQueue.fileIsReady = fileIsReady;
    }

    public static Thread getCurrentThread() {
        return currentThread;
    }

    public static void setCurrentThread(Thread currentThread) {
        MusicQueue.currentThread = currentThread;
    }

    public static FileNode getPrevFile() {
        return prevFile;
    }

    public static void setPrevFile(FileNode prevFile) {
        MusicQueue.prevFile = prevFile;
    }
}
