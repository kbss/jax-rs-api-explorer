package org.forsp.jax.rs.explorer.example;

import com.sun.org.glassfish.gmbal.Description;

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
    @Description("Test jax-rs API")
    void getTest(@PathParam("test") String test, @QueryParam("id") Integer id);


    @GET
    @Path("/simple/get")
    @Description("Simple get test")
    void getTest();

    @GET
    @Path("/test2/{stringValue: \\d+}/{intValue}")
    @Description("Simple query/path params API")
    void getTest2(@PathParam("stringValue") String stringValue, @PathParam("intValue") Long intValue, @QueryParam("id") Integer id, @QueryParam("id2") String id2);
}
