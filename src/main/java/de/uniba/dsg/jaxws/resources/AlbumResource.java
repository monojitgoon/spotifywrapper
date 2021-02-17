    /*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
     */
    package de.uniba.dsg.jaxws.resources;

    import de.uniba.dsg.interfaces.AlbumApi;
    import de.uniba.dsg.jaxws.MusicApiImpl;
    import de.uniba.dsg.jaxws.exceptions.MusicRecommenderFault;
    import de.uniba.dsg.models.ErrorMessage;
    import de.uniba.dsg.models.Release;
    import java.util.List;

    import javax.jws.WebMethod;
    import javax.ws.rs.client.Client;
    import javax.ws.rs.client.ClientBuilder;
    import javax.ws.rs.core.GenericType;
    import javax.ws.rs.core.MediaType;
    import javax.ws.rs.core.Response;

    /**
     *
     * @author Monojit,Lucky
     */
    public class AlbumResource implements AlbumApi {

        private Client client = ClientBuilder.newClient();

        public AlbumResource() {
        }


        @Override
        @WebMethod
        public List<Release> getNewReleases(String country, int size) {
            if(country.equals("?")) {
                country=country.replace("?","");
            }
               Response response = client.target(MusicApiImpl.restServerUri)
                        .path("album/new-releases").queryParam("country", country).queryParam("size", size)
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .get();

                switch (response.getStatus()) {
                    case 200:
                        List<Release> releases = response.readEntity(new GenericType<List<Release>>(){});
                        return releases;
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
