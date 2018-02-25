package com.nateroe.photo.rest;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nateroe.photo.dao.TaxonDao;
import com.nateroe.photo.itis.ItisHelper;
import com.nateroe.photo.model.Taxon;

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

	@GET
	@Path("{taxonId}")
	@Produces({ "application/json" })
	public Taxon getTaxonById(@PathParam("taxonId") long taxonId) {
		LOGGER.debug("getTaxonById({})", taxonId);

		Taxon taxon = taxonDao.findByPrimaryKey(taxonId);
		LOGGER.debug("Found taxon: {}", taxon);
		return taxon;
	}

	@PUT
	@Path("{tsn}")
	@Produces({ "application/json" })
	public Taxon putTaxonByTsn(@PathParam("tsn") int tsn) throws Exception {
		LOGGER.debug("putTaxonByTsn({})", tsn);

		Taxon taxon = itisHelper.readTaxonomy(tsn);
		LOGGER.debug("Found taxon: {}", taxon);
		return taxon;
	}
}
