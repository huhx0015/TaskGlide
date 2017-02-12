package com.devhack.taskglide.models;

/**
 * Created by Michael Yoon Huh on 2/12/2017.
 */

public class File {

    private int imageResource;
    private String fileName;
    private String fileType;
    private String imageUrl;

    public File(String fileName, String imageUrl) {
        this.fileName = fileName;
        this.imageUrl = imageUrl;
    }

    public File(String fileName, int imageResource) {
        this.fileName = fileName;
        this.imageResource = imageResource;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
