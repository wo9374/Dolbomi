package com.example.project.Model;

public class Album {
    public String albumImageUri;

    public Album() {
    }

    public Album(String albumImageUri) {
        this.albumImageUri = albumImageUri;
    }

    public String getAlbumImageUri() {
        return albumImageUri;
    }

    public void setAlbumImageUri(String albumImageUri) {
        this.albumImageUri = albumImageUri;
    }
}
