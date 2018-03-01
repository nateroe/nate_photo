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

import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.nateroe.photo.dao.TaxonDao;
import com.nateroe.photo.itis.ItisHelper;
import com.nateroe.photo.model.Taxon;
import com.nateroe.photo.rest.RestHandler;

@RestHandler
@Path("/taxa")
public class TaxaHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(TaxaHandler.class);

	@EJB
	private TaxonDao taxonDao;

	@EJB
	private ItisHelper itisHelper;

	public TaxaHandler() {
		LOGGER.debug("TaxaHandler()");
	}

	private List<Taxon> listWithParents(Taxon taxon) {
		List<Taxon> returnVal = new LinkedList<>();
		returnVal.add(taxon);
		while (taxon != null && taxon.getParent() != null) {
			taxon = taxon.getParent();
			returnVal.add(taxon);
		}
		return Lists.reverse(returnVal);
	}

	@GET
	@Path("{tsn}")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Taxon> getTaxonByTsn(@PathParam("tsn") int tsn) {
		LOGGER.debug("getTaxonById({})", tsn);

		Taxon taxon = taxonDao.findByTsn(tsn);
		LOGGER.debug("Found taxon: {}", taxon);

		if (taxon == null) {
			throw new NotFoundException();
		}

		return listWithParents(taxon);
	}

	@PUT
	@Path("{tsn}")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Taxon> putTaxonByTsn(@PathParam("tsn") int tsn) throws Exception {
		LOGGER.debug("putTaxonByTsn({})", tsn);

		Taxon taxon = itisHelper.readTaxonomy(tsn);
		LOGGER.debug("Found taxon: {}", taxon);

		if (taxon == null) {
			throw new NotFoundException();
		}

		taxonDao.save(taxon);
		return listWithParents(taxon);
	}
}
