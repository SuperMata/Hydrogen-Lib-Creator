package com.hydroLibCreator.model;

/**
 * Created by SuperMata on 2017/01/08.
 */
public class AudioLibrary {
    private String directorypath;
    private String name;
    private String author;
    private String info;
    private String license;
    private String newLibPath;


    private static AudioLibrary instance = new AudioLibrary();
    private AudioLibrary() {

    }

    public static AudioLibrary getInstance(){
        return instance;
    }

    public String getDirectorypath() {
        return directorypath;
    }

    public void setDirectorypath(String directorypath) {
        this.directorypath = directorypath;
    }

    public String getNewLibPath() {
        return newLibPath;
    }

    public void setNewLibPath(String newLibPath) {
        this.newLibPath = newLibPath;
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }



    @Override
    public String toString(){

        return "Audio Library"+"\n"+
                "path : "+this.directorypath+"\n"+
                "Name : "+this.name+"\n"+
                "Author : "+this.author+"\n"+
                "Info : "+this.info+"\n"+
                "Licence : "+this.license;
    }
}
