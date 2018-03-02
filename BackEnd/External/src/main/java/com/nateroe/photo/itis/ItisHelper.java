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

package com.nateroe.photo.itis;

import java.util.Iterator;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nateroe.photo.dao.TaxonDao;
import com.nateroe.photo.dao.TaxonomicRankDao;
import com.nateroe.photo.http.HttpClientHelper;
import com.nateroe.photo.model.CommonName;
import com.nateroe.photo.model.Taxon;
import com.nateroe.photo.model.TaxonomicRank;

/**
 * Retrieve Taxonomy data from ITIS.gov
 * 
 * @author nate
 */
@Stateless
public class ItisHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItisHelper.class);

	@Inject
	private TaxonDao taxonDao;

	@Inject
	private TaxonomicRankDao rankDao;

	/**
	 * Read taxonomic hierarchy from ITIS for the given TSN.
	 * 
	 * @param tsn
	 * @return
	 * @throws Exception
	 */
	public Taxon readTaxonomy(Integer tsn) throws Exception {
		Taxon targetTaxon = taxonDao.findByTsn(tsn);

		if (targetTaxon == null) {
			targetTaxon = new Taxon();
			targetTaxon.setTsn(tsn);

			String file = HttpClientHelper.downloadFileToString(
					"http://www.itis.gov/ITISWebService/jsonservice/getFullRecordFromTSN?tsn="
							+ tsn);

			LOGGER.debug(" ---  ---  ---  ---  ---");

			// Parse the JSON and build the object model
			// Use the object model to display some HTML of the hierarchy.
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(file);
			JsonElement element = object.get("scientificName");
			if (element != null && element.isJsonObject()) {
				JsonObject scientificName = element.getAsJsonObject();

				LOGGER.debug("combinedName: {}", scientificName.get("combinedName").getAsString());
				targetTaxon.setName(scientificName.get("combinedName").getAsString().trim());
			}

			element = object.get("taxRank");
			if (element != null && element.isJsonObject()) {
				JsonObject taxRank = element.getAsJsonObject();

				String rankName = taxRank.get("rankName").getAsString().trim();
				LOGGER.debug("rankName: {}", rankName);

				TaxonomicRank rank = rankDao.findByName(rankName);
				if (rank == null) {
					rank = new TaxonomicRank();
					rank.setName(rankName);
				}

				targetTaxon.setRank(rank);
			}

			element = object.get("commonNameList");
			if (element != null && element.isJsonObject()) {
				JsonObject commonNameList = element.getAsJsonObject();

				JsonArray array = commonNameList.get("commonNames").getAsJsonArray();
				Iterator<JsonElement> i = array.iterator();
				while (i.hasNext()) {
					JsonElement nextElement = i.next();
					if (nextElement.isJsonObject()) {
						JsonObject commonName = nextElement.getAsJsonObject();
						if (commonName.get("language").getAsString().toLowerCase().trim()
								.equals("english")) {
							LOGGER.debug("commonName: {}",
									commonName.get("commonName").getAsString());
							targetTaxon.addCommonName(new CommonName(
									commonName.get("commonName").getAsString().trim(),
									targetTaxon));
						}
					}
				}
			}

			element = object.get("parentTSN");
			String parentTsnString = null;
			if (element != null && element.isJsonObject()) {
				JsonObject parentTsnJson = element.getAsJsonObject();

				LOGGER.debug("TSN: {}", parentTsnJson.get("tsn").getAsString());
				LOGGER.debug("parentTSN: " + parentTsnJson.get("parentTsn").getAsString());
				parentTsnString = parentTsnJson.get("parentTsn").getAsString();

				if (!parentTsnString.equals("0")) {
					Integer parentTsn = Integer.parseInt(parentTsnString.trim());
					targetTaxon.setParent(readTaxonomy(parentTsn));
				}
			}
		} else {
			LOGGER.debug("Existing taxon found ({}) for {}", targetTaxon.getId(),
					targetTaxon.getName());
		}

		return targetTaxon;
	}
}
