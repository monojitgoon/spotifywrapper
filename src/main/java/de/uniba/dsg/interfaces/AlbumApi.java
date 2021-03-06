package de.uniba.dsg.interfaces;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;

import de.uniba.dsg.models.Release;

public interface AlbumApi {
    /**
     * TODO:
     * Method should return a collection of new releases modeled by the releases model class
     * Method should be available at /albums/new-releases
     * Specify the country of the new releases and the size of the returned collection via query parameters, i.e., named 'country' and 'size'
     * Both parameters 'country' and 'size' should be optional
     * Handle requests with invalid country and size parameters correctly, i.e., signal a bad request
     */
    @WebMethod(operationName = "album-new-releases")
    @WebResult(name = "release")
    List<Release> getNewReleases(@WebParam(name="country") String country,@WebParam(name="size")  int size);
}
