package com.nateroe.photo.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.nateroe.photo.model.Taxon;

@Stateless
public class TaxonDao extends AbstractDao<Taxon, Long> {
	public Taxon findByTsn(Integer tsn) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Taxon> criteria = builder.createQuery(Taxon.class);
		Root<Taxon> taxonRoot = criteria.from(Taxon.class);
		criteria.select(taxonRoot);
		criteria.where(builder.equal(taxonRoot.get("tsn"), tsn));

		Taxon returnVal = null;
		List<Taxon> results = em.createQuery(criteria).getResultList();
		if (results.size() > 0) {
			returnVal = results.get(0);
		}

		return returnVal;
	}
}
