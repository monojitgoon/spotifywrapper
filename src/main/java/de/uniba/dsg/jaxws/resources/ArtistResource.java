/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniba.dsg.jaxws.resources;

import de.uniba.dsg.interfaces.ArtistApi;
import de.uniba.dsg.jaxws.MusicApiImpl;
import de.uniba.dsg.jaxws.exceptions.MusicRecommenderFault;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Interpret;
import de.uniba.dsg.models.Song;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author boehneal
 */
public class ArtistResource implements ArtistApi {

    private Client client = ClientBuilder.newClient();

    public ArtistResource() {
    }

    @Override
    public Interpret getArtist(String artistId) {

        if (artistId == null || artistId.trim().isEmpty()) {
            throw new MusicRecommenderFault("A client side error occured", "artistId is mandatory");
        }

        Response response = client.target(MusicApiImpl.restServerUri)
                .path("artists").path(artistId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        switch (response.getStatus()) {
            case 200:
                Interpret artist = response.readEntity(Interpret.class);
                return artist;
            case 400: {
                String cause = response.readEntity(ErrorMessage.class).getMessage();
                throw new MusicRecommenderFault("A client side error occurred", cause);
            }
            case 404: {
                String cause = response.readEntity(ErrorMessage.class).getMessage();
                throw new MusicRecommenderFault("The requested resource was not found", cause);
            }
            case 500: {
                String cause = response.readEntity(ErrorMessage.class).getMessage();
                throw new MusicRecommenderFault("An internal server error occurred", cause);
            }
            default: {
                String cause = response.readEntity(ErrorMessage.class).getMessage();
                throw new MusicRecommenderFault("An unknown remote server error occurred", cause);
            }
        }
    }

    @Override
    @WebMethod
    public List<Song> getTopTracks(String artistId) {

        if (artistId == null || artistId.trim().isEmpty()) {
            throw new MusicRecommenderFault("A client side error occured", "artistId is mandatory");
        }

        Response response = client.target(MusicApiImpl.restServerUri)
                .path("artists").path(artistId).path("top-tracks")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        switch (response.getStatus()) {
            case 200:
                List<Song> songs = response.readEntity(new GenericType<List<Song>>() {
                });
                return songs;
            case 400: {
                String cause = response.readEntity(ErrorMessage.class).getMessage();
                throw new MusicRecommenderFault("A client side error occurred", cause);
            }
            case 404: {
                String cause = response.readEntity(ErrorMessage.class).getMessage();
                throw new MusicRecommenderFault("The requested resource was not found", cause);
            }
            case 500: {
                String cause = response.readEntity(ErrorMessage.class).getMessage();
                throw new MusicRecommenderFault("An internal server error occurred", cause);
            }
            default: {
                String cause = response.readEntity(ErrorMessage.class).getMessage();
                throw new MusicRecommenderFault("An unknown remote server error occurred", cause);
            }
        }
    }

    @Override
    public Interpret getSimilarArtist(String artistId) {

        if (artistId == null || artistId.trim().isEmpty()) {
            throw new MusicRecommenderFault("A client side error occured", "artistId is mandatory");
        }

        Response response = client.target(MusicApiImpl.restServerUri)
                .path("artists").path(artistId).path("similar-artist")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        switch (response.getStatus()) {
            case 200:
                Interpret artist = response.readEntity(Interpret.class);
                return artist;
            case 400: {
                String cause = response.readEntity(ErrorMessage.class).getMessage();
                throw new MusicRecommenderFault("A client side error occurred", cause);
            }
            case 404: {
                String cause = response.readEntity(ErrorMessage.class).getMessage();
                throw new MusicRecommenderFault("The requested resource was not found", cause);
            }
            case 500: {
                String cause = response.readEntity(ErrorMessage.class).getMessage();
                throw new MusicRecommenderFault("An internal server error occurred", cause);
            }
            default: {
                String cause = response.readEntity(ErrorMessage.class).getMessage();
                throw new MusicRecommenderFault("An unknown remote server error occurred", cause);
            }
        }
    }

}
