package com.nateroe.photo.rest.handlers;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nateroe.photo.dao.ExpeditionDao;
import com.nateroe.photo.model.Expedition;
import com.nateroe.photo.rest.RestHandler;

@RestHandler
@Path("/expedition")
public class ExpeditionHandler {
	@EJB
	private ExpeditionDao expeditionDao;

	@GET
	@Path("id/{expeditionId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Expedition getExpeditionbyId(@PathParam("expeditionId") long expeditionId) {
		Expedition expedition = expeditionDao.findByPrimaryKey(expeditionId);
		if (expedition == null) {
			throw new NotFoundException();
		}

		return expedition;
	}

	@GET
	@Path("all")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Expedition> getAllExpeditions() {
		List<Expedition> expeditions = expeditionDao.findAll();
		if (expeditions == null) {
			throw new NotFoundException();
		}

		return expeditions;
	}
}
