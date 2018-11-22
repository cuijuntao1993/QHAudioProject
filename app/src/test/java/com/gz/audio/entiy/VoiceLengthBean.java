package com.gz.audio.entiy;

/**
 * Created by Liuyz on 2018/5/22.
 */

public class VoiceLengthBean {
    private String fileaddress;
    private String filename;
    private int filelength;
    private boolean filestatus;

    public VoiceLengthBean(int filelength,boolean filestatus) {
        this.filelength = filelength;
        this.filestatus = filestatus;
    }

    public VoiceLengthBean(String fileaddress, String filename, int filelength, boolean filestatus) {
        this.fileaddress = fileaddress;
        this.filename = filename;
        this.filelength = filelength;
        this.filestatus = filestatus;
    }

    public String getFileaddress() {
        return fileaddress;
    }

    public void setFileaddress(String fileaddress) {
        this.fileaddress = fileaddress;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getFilelength() {
        return filelength;
    }

    public void setFilelength(int filelength) {
        this.filelength = filelength;
    }

    public boolean getFilestatus() {
        return filestatus;
    }

    public void setFilestatus(boolean filestatus) {
        this.filestatus = filestatus;
    }
}
