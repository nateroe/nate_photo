package com.nateroe.photo.rest;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;

import com.nateroe.photo.db.JpaUtil;
import com.nateroe.photo.model.Taxon;

@Path("/taxa")
public class TaxaHandler {
	private static final Logger logger = Logger.getLogger(TaxaHandler.class);

	@GET
	@Path("{taxonId}")
	@Produces({ "application/json" })
	public Taxon getTaxonById(@PathParam("taxonId") long taxonId) {
		logger.info("getTaxonById(" + taxonId + ")");

		Taxon taxon = null;
		EntityManager entityManager = null;
		try {
			entityManager = JpaUtil.getEntityManager();
			entityManager.getTransaction().begin();

			taxon = (Taxon) entityManager.find(Taxon.class, taxonId);

			entityManager.getTransaction().commit();
		} finally {
			entityManager.close();
		}

		return taxon;
	}
}
