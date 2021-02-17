package de.uniba.dsg.jaxws.resources;

import de.uniba.dsg.interfaces.PlaylistApi;
import de.uniba.dsg.jaxws.MusicApiImpl;
import de.uniba.dsg.jaxws.exceptions.MusicRecommenderFault;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.PlaylistRequest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Ludwig Leuschner
 */
public class PlaylistResource implements PlaylistApi {

	@Override
	public Response createPlaylist(PlaylistRequest request) {
		Client client = ClientBuilder.newClient();
		Response response = client.target(MusicApiImpl.restServerUri)
				.path("/playlists")
				.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.json(request));

		if (response.getStatus() == 200) {
			String responseAsString = response.readEntity(String.class);
			return Response.ok(responseAsString, MediaType.APPLICATION_JSON).build();
		} else if (response.getStatus() == 400) {
			String cause = response.readEntity(ErrorMessage.class).getMessage();
			throw new MusicRecommenderFault("A client side error occurred", cause);
		} else if (response.getStatus() == 404) {
			String cause = response.readEntity(ErrorMessage.class).getMessage();
			throw new MusicRecommenderFault("The requested resource was not found", cause);
		} else if (response.getStatus() == 500) {
			String cause = response.readEntity(ErrorMessage.class).getMessage();
			throw new MusicRecommenderFault("An internal server error occurred", cause);
		} else {
			String cause = response.readEntity(ErrorMessage.class).getMessage();
			throw new MusicRecommenderFault("An unknown remote server error occurred", cause);
		}
	}
}
