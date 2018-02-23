package com.nateroe.photo.rest;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nateroe.photo.dao.TaxonDao;
import com.nateroe.photo.model.Taxon;

@Path("/taxa")
public class TaxaHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(TaxaHandler.class);

	@EJB
	private TaxonDao taxonDao;

	public TaxaHandler() {
		LOGGER.warn("TaxaHandler()");
	}

	@GET
	@Path("{taxonId}")
	@Produces({ "application/json" })
	public Taxon getTaxonById(@PathParam("taxonId") long taxonId) {
		LOGGER.warn("getTaxonById(" + taxonId + ")");

		Taxon taxon = taxonDao.findByPrimaryKey(taxonId);
		LOGGER.info("Found taxon: {}", taxon);
		return taxon;

//		Taxon taxon = new Taxon();
//		taxon.setName("WTF, Over.");
//		return taxon;
	}
}
