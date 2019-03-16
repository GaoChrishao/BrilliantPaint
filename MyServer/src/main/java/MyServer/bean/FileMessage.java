package MyServer.bean;

import java.util.ArrayList;
import java.util.List;

public class FileMessage {
    public static final String status_ok="ok";
    public static final String status_toolarge="toolarge";
    public static final String status_wrong="wrong";
    public static final String status_wrongRequest="wrongRequest";
    public static final String status_wait="wait";


    private String status;
    private List<File> fileList;

    public FileMessage(){
        fileList=new ArrayList<>();
        status=status_ok;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }
    public void addFile(File file){
        fileList.add(file);
    }
}
