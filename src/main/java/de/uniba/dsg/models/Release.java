package de.uniba.dsg.models;


/**
 * TODO:
 * Release attributes should be
 * - title:String
 * - artist:String (possibly multiple artists concatenated with ", ")
 */
public class Release {
	private String title;
    private String artist;
    
    
    public Release(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public Release() {
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
