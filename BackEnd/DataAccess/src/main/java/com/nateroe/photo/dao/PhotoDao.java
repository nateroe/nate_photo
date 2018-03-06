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

package com.nateroe.photo.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.nateroe.photo.model.Photo;

@Stateless
public class PhotoDao extends AbstractEntityDao<Photo> {
	public Photo findByOrigFilename(String fileName, Date date) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Photo> criteria = builder.createQuery(Photo.class);
		Root<Photo> root = criteria.from(Photo.class);
		criteria.select(root);
//		criteria.where(builder.equal(root.get("origFileName"), fileName));
		criteria.where(builder.and(builder.equal(root.get("origFileName"), fileName),
				builder.equal(root.get("date"), date)));

		Photo returnVal = null;
		List<Photo> results = em.createQuery(criteria).getResultList();
		if (results.size() > 0) {
			returnVal = results.get(0);
		}

		return returnVal;
	}
}
