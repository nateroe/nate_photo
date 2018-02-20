package com.nateroe.photo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/hello")
public class HelloWorld {

	@GET
	@Path("/echo/{input}")
	@Produces("text/plain")
	public String ping(@PathParam("input") String input) {
		return input;
	}

//    @POST
//    @Produces("application/json")
//    @Consumes("application/json")
//    @Path("/jsonBean")
//    public Response modifyJson(JsonBean input) {
//        input.setVal2(input.getVal1());
//        return Response.ok().entity(input).build();
//    }
}
