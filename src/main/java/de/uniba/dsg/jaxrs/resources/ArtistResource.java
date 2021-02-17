/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniba.dsg.jaxrs.resources;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.BadRequestException;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.models.Artist;
import com.wrapper.spotify.models.Track;
import de.uniba.dsg.SpotifyApi;
import de.uniba.dsg.interfaces.ArtistApi;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.jaxrs.exceptions.RemoteApiException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Interpret;
import de.uniba.dsg.models.Song;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author boehneal, mubashir
 */
@Path("artists")

public class ArtistResource implements ArtistApi {

    private final Api spotify = SpotifyApi.getInstance();
    private static final Logger LOG = Logger.getLogger(ArtistResource.class.getName());
    private static final int MAX_TOP_TRACKS_COUNT = 5;

    @Override
    @GET
    @Path("{artist-id}")
    public Interpret getArtist(@PathParam("artist-id") String artistId) {

        if (artistId == null || artistId.isEmpty()) {
            throw new ClientRequestException(new ErrorMessage("ArtistId not provided"));
        }

        try {
            Artist artist = spotify.getArtist(artistId).build().get();
            return new Interpret(artist);
        } catch (BadRequestException ex) {
            throw new ResourceNotFoundException(new ErrorMessage("Artist not found"));
        } catch (IOException | WebApiException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new RemoteApiException(new ErrorMessage(ex.getMessage()));
        }
    }

    @Override
    @GET
    @Path("{artist-id}/top-tracks")
    public List<Song> getTopTracks(@PathParam("artist-id") String artistId) {

        if (artistId == null || artistId.isEmpty()) {
            throw new ClientRequestException(new ErrorMessage("ArtistId not provided"));
        }

        try {
            List<Track> topTracks = spotify.getTopTracksForArtist(artistId, "DE").build().get();

            return topTracks.stream()
                    .map(Song::new)
                    .limit(MAX_TOP_TRACKS_COUNT)
                    .collect(Collectors.toList());

        } catch (BadRequestException ex) {
            throw new ResourceNotFoundException(new ErrorMessage("Artist not found"));
        } catch (IOException | WebApiException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new RemoteApiException(new ErrorMessage(ex.getMessage()));
        }
    }

    @Override
    @GET
    @Path("{artist-id}/similar-artist")
    public Interpret getSimilarArtist(@PathParam("artist-id") String artistId) {

        if (artistId == null || artistId.isEmpty()) {
            throw new ClientRequestException(new ErrorMessage("ArtistId not provided"));
        }

        try {
            List<Artist> similiarArtists = spotify.getArtistRelatedArtists(artistId).build().get();

            similiarArtists = similiarArtists.stream().filter(artist -> {
                return !artist.getId().equals(artistId);
            }).collect(Collectors.toList());

            if (similiarArtists.isEmpty()) {
                throw new NoContentException("No related Artists");
            }

            Collections.shuffle(similiarArtists);

            return new Interpret(similiarArtists.get(0));

        } catch (BadRequestException ex) {
            throw new ResourceNotFoundException(new ErrorMessage("Artist not found"));
        } catch (IOException | WebApiException ex) {
            Logger.getLogger(ArtistResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServerErrorException(Status.INTERNAL_SERVER_ERROR);
        }
    }

}
