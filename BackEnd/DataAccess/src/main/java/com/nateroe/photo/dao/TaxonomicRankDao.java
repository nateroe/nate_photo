package com.nateroe.photo.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.nateroe.photo.model.TaxonomicRank;

public class TaxonomicRankDao extends AbstractDao<TaxonomicRank, Long> {
	public TaxonomicRank findByName(String name) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<TaxonomicRank> criteria = builder.createQuery(TaxonomicRank.class);
		Root<TaxonomicRank> rankRoot = criteria.from(TaxonomicRank.class);
		criteria.select(rankRoot);
		criteria.where(builder.equal(rankRoot.get("name"), name));

		TaxonomicRank returnVal = null;
		List<TaxonomicRank> results = em.createQuery(criteria).getResultList();
		if (results.size() > 0) {
			returnVal = results.get(0);
		}

		return returnVal;
	}
}
