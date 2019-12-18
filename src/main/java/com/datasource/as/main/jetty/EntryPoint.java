package com.datasource.as.main.jetty;

import com.datasource.as.util.FileUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Entry points for the Jetty server
 */
@Path("/service")
public class EntryPoint {

    @GET
    @Path("metrics")
    @Produces(MediaType.APPLICATION_JSON)
    public String getStatus(@PathParam("id") String autoscalerId) {
        return FileUtil.readFile("metrics.prom");
    }

}