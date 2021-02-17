package de.uniba.dsg.models;

import java.util.List;
import java.util.Objects;

/**
 * TODO:
 * Playlist attributes should be
 * - title:String
 * - size:int
 * - tracks:List<Song>
 */
public class Playlist {

    private String title = "";
    private int size;
    private List<Song> tracks;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Song> getTracks() {
        return tracks;
    }

    public void setTracks(List<Song> tracks) {
        this.tracks = tracks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return size == playlist.size &&
                Objects.equals(title, playlist.title) &&
                Objects.equals(tracks, playlist.tracks);
    }

    @Override
    public int hashCode() {

        return Objects.hash(title, size, tracks);
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "title='" + title + '\'' +
                ", size=" + size +
                ", tracks=" + tracks +
                '}';
    }
}