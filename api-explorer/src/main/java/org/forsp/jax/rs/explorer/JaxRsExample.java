package org.forsp.jax.rs.explorer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * @author Serhii Kryvtsov
 * @since 1.0
 */
@Path("class/path")
public interface JaxRsExample {

    @GET
    @Path("/test/{test: \\d+}")
    void getTest(@PathParam("test") String test, @QueryParam("id") Integer id);
}
