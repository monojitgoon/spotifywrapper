package de.uniba.dsg.models;

import java.util.List;
import java.util.Objects;

/**
 * TODO: PlaylistRequest attributes should be
 * title:String
 * artistSeeds:List<String>, must be serialized as 'seeds'
 * numberOfSongs:int,
 * must be serialized as 'size'
 */
public class PlaylistRequest {

    private String title;
    private int numberOfSongs;
    private List<String> artistIds;

    public PlaylistRequest(String title, List<String> artistIds, int numberOfSongs) {
        this.title = title;
        this.artistIds = artistIds;
        this.numberOfSongs = numberOfSongs;
    }

    public PlaylistRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public List<String> getArtistIds() {
        return artistIds;
    }

    public void setArtistIds(List<String> artistIds) {
        this.artistIds = artistIds;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setNumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.artistIds);
        hash = 67 * hash + this.numberOfSongs;
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
        final PlaylistRequest other = (PlaylistRequest) obj;
        if (this.numberOfSongs != other.numberOfSongs) {
            return false;
        }
        if (!Objects.equals(this.artistIds, other.artistIds)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PlaylistRequest{" + "artistIds=" + artistIds + ", numberOfSongs=" + numberOfSongs + '}';
    }

}
