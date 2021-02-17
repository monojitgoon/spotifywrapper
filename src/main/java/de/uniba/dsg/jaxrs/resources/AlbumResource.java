/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniba.dsg.jaxrs.resources;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.BadRequestException;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.models.Album;
import com.wrapper.spotify.models.NewReleases;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.SimpleAlbum;
import com.wrapper.spotify.models.SimpleArtist;
import de.uniba.dsg.SpotifyApi;
import de.uniba.dsg.interfaces.AlbumApi;
import de.uniba.dsg.jaxrs.exceptions.RemoteApiException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Release;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 *
 * @author monojit, lucky
 */
@Path("album/new-releases")

public class AlbumResource implements AlbumApi {

	private final Api spotify = SpotifyApi.getInstance();
	private static final Logger LOG = Logger.getLogger(AlbumResource.class.getName());

	@Override
	@GET
//	@Path("{country}/{size}")
	public List<Release> getNewReleases(@QueryParam("country") String country, @QueryParam("size") int size) {
		List<Release> result = new ArrayList<>();

		try {

			NewReleases newRelease = null;

			// get search results
				if(size == 0 && (country == null || country.isEmpty())) {
				newRelease = spotify.getNewReleases().build().get();
			} else if(size == 0 && (country != null || !country.isEmpty())) {
				newRelease = spotify.getNewReleases().country(country).build().get();
			} else if(size != 0 && (country == null || country.isEmpty())) {
					newRelease = spotify.getNewReleases().limit(size).build().get();
			} else {
				newRelease = spotify.getNewReleases().limit(size).country(country).build().get();
			}

			Page<SimpleAlbum> simpleAlbum = newRelease.getAlbums();
			List<SimpleAlbum> listAlbum = simpleAlbum.getItems();
			

			// no album found
			if (listAlbum.isEmpty()) {
				throw new ResourceNotFoundException(
						new ErrorMessage(String.format("No matching album found for query: %s", country)));
			}

			for (SimpleAlbum sa : listAlbum) {

				Release obj = new Release();
				obj.setTitle(sa.getName());
				Album al =   spotify.getAlbum(sa.getId()).build().get();
				List<SimpleArtist> simpleArtists =al.getArtists();
				StringJoiner artistJoiner = new StringJoiner(", ");

				simpleArtists.stream().forEach((a) -> {
					artistJoiner.add(a.getName());
				});

				obj.setArtist(artistJoiner.toString());
				result.add(obj);

			}

			return result;

		} catch (BadRequestException ex) {
			LOG.log(Level.SEVERE, null, ex);
			throw new ResourceNotFoundException(new ErrorMessage("Invalid country code!"));
		} catch (IOException | WebApiException ex) {
			LOG.log(Level.SEVERE, null, ex);
			throw new RemoteApiException(new ErrorMessage(ex.getMessage()));
		}
	}

}
