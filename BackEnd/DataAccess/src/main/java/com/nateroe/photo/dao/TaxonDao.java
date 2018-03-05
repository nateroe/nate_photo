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

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nateroe.photo.model.Taxon;

@Stateless
public class TaxonDao extends AbstractEntityDao<Taxon> {
	private static final Logger LOGGER = LoggerFactory.getLogger(TaxonDao.class);

	@EJB
	TaxonomicRankDao rankDao;

	public Taxon findByTsn(Integer tsn) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Taxon> criteria = builder.createQuery(Taxon.class);
		Root<Taxon> root = criteria.from(Taxon.class);
		criteria.select(root);
		criteria.where(builder.equal(root.get("tsn"), tsn));

		Taxon returnVal = null;
		List<Taxon> results = em.createQuery(criteria).getResultList();
		if (results.size() > 0) {
			returnVal = results.get(0);
		}

		return returnVal;
	}

	@Override
	public void save(Taxon entity) {
		LOGGER.debug(">Save taxon {}", entity.getName());
		if (entity.getRank() != null && entity.getRank().getId() == null) {
			LOGGER.debug("--> persist rank {}", entity.getRank().getName());
			rankDao.save(entity.getRank());
		}

		if (entity.getParent() != null && entity.getParent().getId() == null) {
			LOGGER.debug("--> persist parent {}", entity.getParent().getName());
			save(entity.getParent());
		}

		LOGGER.debug("--> finally, presist taxon {}", entity.getName());
		em.persist(entity);
		LOGGER.debug("<Save taxon {}", entity.getName());
	}
}
