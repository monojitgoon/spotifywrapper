package de.uniba.dsg.jaxrs.resources;

import com.google.gson.Gson;
import de.uniba.dsg.interfaces.PlaylistApi;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.jaxrs.exceptions.RemoteApiException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;
import de.uniba.dsg.models.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.logging.Logger;

/**
 *
 * @author Ludwig Leuschner
 */
@Path("playlists")
@Produces(MediaType.APPLICATION_JSON)
public class PlaylistResource implements PlaylistApi {
    private static final Logger LOG = Logger.getLogger(ArtistResource.class.getName());
    private static final int DEFAULT_NUMBER_OF_SONGS = 10;
    private ArtistResource artistResource = new ArtistResource();
    private SearchResource searchResource = new SearchResource();
    private Random random = new Random();

    @Override
    @POST
    public Response createPlaylist(PlaylistRequest request) {
        checkRequest(request);

        List<Song> songs = new ArrayList<>();
        Queue<String> knownArtistQueue = new LinkedList<>();
        final int numberOfSongs = request.getNumberOfSongs() > 0 ? request.getNumberOfSongs() : DEFAULT_NUMBER_OF_SONGS;

        for (String artistId : request.getArtistIds()) {
            if(songs.size() >= numberOfSongs) break;

            Song song = getRandomSong(artistId);

            if(song != null) {
                songs.add(song);
                knownArtistQueue.add(getKnownArtistNameFromSong(song));
            }
        }

        checkNumberOfSongsAfterInitialSearch(songs);

        while (songs.size() < numberOfSongs && knownArtistQueue.size() > 0) {
            Interpret similarInterpret = getSimilarInterpret(knownArtistQueue);

            if(similarInterpret != null) {
                Song song = getRandomSong(similarInterpret.getId());

                if(song != null) {
                    songs.add(song);
                    knownArtistQueue.add(getKnownArtistNameFromSong(song));
                }
            }
        }

        Playlist playlist = getPlaylist(request, songs);
        String responseAsJson = new Gson().toJson(playlist);

        return Response.ok(responseAsJson, MediaType.APPLICATION_JSON).build();
    }

    private void checkNumberOfSongsAfterInitialSearch(List<Song> songs) {
        if(songs.isEmpty()) {
            throw  new ClientRequestException(new ErrorMessage("all of the provided seeds are invalid"));
        }
    }

    private void checkRequest(PlaylistRequest request) {
        if (request == null) {
            throw  new ClientRequestException(new ErrorMessage("cannot parse request object"));
        } else if (request.getTitle() == null) {
            throw  new ClientRequestException(new ErrorMessage("there is no playlist title"));
        } else if (request.getTitle().isEmpty()) {
            throw  new ClientRequestException(new ErrorMessage("there is no playlist title"));
        } else if (request.getArtistIds() == null) {
            throw  new ClientRequestException(new ErrorMessage("there are no artist seeds"));
        } else if (request.getArtistIds().size() == 0) {
            throw  new ClientRequestException(new ErrorMessage("there are no artist seeds"));
        } else if (request.getNumberOfSongs() < 0) {
            throw  new ClientRequestException(new ErrorMessage("number of songs must not be negative"));
        } else if (request.getArtistIds().size() > request.getNumberOfSongs() &&
                request.getArtistIds().size() > DEFAULT_NUMBER_OF_SONGS) {
            LOG.warning("There are more artists than max amount of songs! Skip last artists..");
        }
    }


    // artist may contain a comma separated list of artists. if so, return the first one
    private String getKnownArtistNameFromSong(Song song) {
        String artist = song.getArtist();

        if(artist.contains(",")) {
            return artist.split(",")[0];
        }

        return artist;
    }

    private Playlist getPlaylist(PlaylistRequest request, List<Song> songs) {
        Playlist playlist = new Playlist();
        playlist.setSize(songs.size());
        playlist.setTitle(request.getTitle());
        playlist.setTracks(songs);

        return playlist;
    }

    private Interpret getSimilarInterpret(Queue<String> knownArtistQueue) {
        Interpret knownInterpret;
        String knownArtist = knownArtistQueue.remove();

        try {
            knownInterpret = searchResource.searchArtist(knownArtist);
        } catch (ResourceNotFoundException e) {
            LOG.warning("No Artist found with the following name: " + knownArtist);

            return null;
        }

        return artistResource.getSimilarArtist(knownInterpret.getId());
    }

    private Song getRandomSong(String artistId) {
        List<Song> topTracks;

        try {
             topTracks = artistResource.getTopTracks(artistId);
        } catch (NotFoundException | ResourceNotFoundException notFoundException) {
            LOG.warning("artist not found with id: " + artistId);
            return null;
        } catch (RemoteApiException remoteApiException) {
            LOG.warning("remote API exeption");
            return null;
        }

        int randomTrackNumber = random.nextInt(topTracks.size() - 1);
        return topTracks.get(randomTrackNumber);
    }
}
