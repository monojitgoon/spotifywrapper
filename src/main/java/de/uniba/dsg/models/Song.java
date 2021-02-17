package de.uniba.dsg.models;

import com.wrapper.spotify.models.Track;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * TODO: Song attributes should be - title:String - artist:String (possibly
 * multiple artists concatenated with ", ") - duration:double (in seconds)
 */

public class Song {

    private String title;
    private String artist;
    private double duration;

    public Song(String title, String artist, double duration) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
    }

    public Song(Track t) {
        this.title = t.getName();
        this.duration = 0d + t.getDuration() / 1000d;
        this.artist = t.getArtists().stream()
                .map(a -> a.getName())
                .collect(Collectors.joining(","));
    }

    public Song() {
    }
    
    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    // for converting ms to seconds
    public double getDuration() {
        return duration;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.title);
        hash = 97 * hash + Objects.hashCode(this.artist);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.duration) ^ (Double.doubleToLongBits(this.duration) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Song other = (Song) obj;
        if (Double.doubleToLongBits(this.duration) != Double.doubleToLongBits(other.duration)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.artist, other.artist)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Song{" + "title=" + title + ", artist=" + artist + ", duration=" + duration + '}';
    }

}
