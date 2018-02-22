package com.nateroe.photo.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nateroe.photo.model.Taxon;

@Path("/taxa")
public class TaxaHandler {
	private static final Logger logger = LogManager.getLogger(TaxaHandler.class);

	@GET
	@Path("{taxonId}")
	@Produces({ "application/json" })
	public Taxon getTaxonById(@PathParam("taxonId") long taxonId) {
		logger.warn("getTaxonById(" + taxonId + ")");

//		Taxon taxon = new TaxonDao().findByPrimaryKey(taxonId);
//		logger.info("Found taxon: {}", taxon);
//		return taxon;

		return null;
	}
}
