package com.nateroe.photo.rest.handlers;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nateroe.photo.dao.PhotoDao;
import com.nateroe.photo.model.Photo;
import com.nateroe.photo.rest.RestHandler;

@RestHandler
@Path("/photo")
public class PhotoHandler {
	@EJB
	private PhotoDao photoDao;

	@GET
	@Path("{photoId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Photo getTaxonByTsn(@PathParam("photoId") long photoId) {
		Photo photo = photoDao.findByPrimaryKey(photoId);
		if (photo == null) {
			throw new NotFoundException();
		}

		return photo;
	}
}
