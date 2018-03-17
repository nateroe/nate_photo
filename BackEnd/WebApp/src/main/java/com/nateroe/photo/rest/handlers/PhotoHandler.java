/**
 * NatePhoto - A photo catalog and presentation application.
 * Copyright (C) 2018 Nathaniel Roe
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact nate [at] nateroe [dot] com
 */
package com.nateroe.photo.rest.handlers;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nateroe.photo.dao.PhotoDao;
import com.nateroe.photo.model.ImageResource;
import com.nateroe.photo.model.Photo;
import com.nateroe.photo.rest.RestHandler;

@RestHandler
@Path("/photo")
public class PhotoHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(PhotoHandler.class);

	// maximum resource resolution (see sanitizeResources(...))
	private static final int MAX_RES = 2048;

	@EJB
	private PhotoDao photoDao;

	@GET
	@Path("id/{photoId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Photo getPhotobyId(@PathParam("photoId") long photoId) {
		Photo photo = photoDao.findByPrimaryKey(photoId);
		if (photo == null) {
			throw new NotFoundException();
		}

		return sanitizeResources(photo);
	}

	@GET
	@Path("all")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Photo> getAllPhotos() {
		List<Photo> photos = photoDao.findAll();
		if (photos == null) {
			throw new NotFoundException();
		}

		return sanitizeResources(photos);
	}

	@GET
	@Path("highlights")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Photo> getBestPhotos() {
		List<Photo> photos = photoDao.findAllHighlights();
		if (photos == null) {
			throw new NotFoundException();
		}

		return sanitizeResources(photos);
	}

	@GET
	@Path("expedition/{expeditionId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Photo> getPhotosByExpedition(@PathParam("expeditionId") long expeditionId) {
		return sanitizeResources(photoDao.findByExpeditionId(expeditionId));
	}

	@GET
	@Path("expeditionHighlight/{expeditionId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Photo> getPhotosByExpeditionHighlight(
			@PathParam("expeditionId") long expeditionId) {
		return sanitizeResources(photoDao.findHighlightsByExpeditionId(expeditionId));
	}

	private List<Photo> sanitizeResources(List<Photo> photos) {
		for (Photo photo : photos) {
			sanitizeResources(photo);
		}
		return photos;
	}

	private Photo sanitizeResources(Photo photo) {
		for (ImageResource resource : photo.getImageResources()) {
			if (resource.getWidth() > MAX_RES || resource.getHeight() > MAX_RES) {
				photo.removeImageResource(resource);
			}
		}

		return photo;
	}
}
