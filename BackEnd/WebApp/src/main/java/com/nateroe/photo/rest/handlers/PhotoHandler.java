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

import com.nateroe.photo.dao.PhotoDao;
import com.nateroe.photo.model.Photo;
import com.nateroe.photo.rest.RestHandler;

@RestHandler
@Path("/photo")
public class PhotoHandler {
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

		return photo;
	}

	@GET
	@Path("all")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Photo> getAllPhotos() {
		List<Photo> photos = photoDao.findAll();
		if (photos == null) {
			throw new NotFoundException();
		}

		return photos;
	}
}
