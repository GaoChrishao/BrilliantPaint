package com.gaoch.brilliantpic.myclass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Keep;

@Keep
public class FileMessage implements Serializable {
    private static final long serialVersionUID = 1L;
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
