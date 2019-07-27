package sample;


public class FileNode {
    private String filePath;
    private FileNode prevNode;
    private FileNode nextNode;

    public FileNode(String filePath, FileNode prevNode, FileNode nextNode) {
        this.filePath = filePath;
        this.prevNode = prevNode;
        this.nextNode = nextNode;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public FileNode getPrevNode() {
        return prevNode;
    }

    public void setPrevNode(FileNode prevNode) {
        this.prevNode = prevNode;
    }

    public FileNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(FileNode nextNode) {
        this.nextNode = nextNode;
    }
}
