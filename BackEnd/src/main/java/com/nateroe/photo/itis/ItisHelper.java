package com.nateroe.photo.itis;

import java.util.Iterator;

import javax.ejb.EJB;

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
public class ItisHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItisHelper.class);

	@EJB
	private TaxonDao taxonDao;

	@EJB
	private TaxonomicRankDao rankDao;

	/**
	 * Read taxonomic hierarchy from ITIS for the given TSN.
	 * 
	 * @param tsn
	 * @return
	 * @throws Exception
	 */
	public Taxon readTaxonomy(String tsnString) throws Exception {
		Integer tsn = Integer.parseInt(tsnString.trim());
		Taxon returnVal = taxonDao.findByTsn(tsn);

		if (returnVal == null) {
			returnVal = new Taxon();
			returnVal.setTsn(tsn);

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
				returnVal.setName(scientificName.get("combinedName").getAsString().trim());
			}

			element = object.get("taxRank");
			if (element != null && element.isJsonObject()) {
				JsonObject taxRank = element.getAsJsonObject();

				LOGGER.debug("rankName: {}", taxRank.get("rankName").getAsString());

				TaxonomicRank rank = rankDao
						.findByName(taxRank.get("rankName").getAsString().trim());
				returnVal.setRank(rank);
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
							returnVal.addCommonName(new CommonName(
									commonName.get("commonName").getAsString().trim(), returnVal));
						}
					}
				}
			}

			element = object.get("parentTSN");
			String parentTsn = null;
			if (element != null && element.isJsonObject()) {
				JsonObject parentTsnJson = element.getAsJsonObject();

				LOGGER.debug("TSN: {}", parentTsnJson.get("tsn").getAsString());
//			System.out.println("parentTSN: " + parentTsnJson.get("parentTsn").getAsString());
				parentTsn = parentTsnJson.get("parentTsn").getAsString();
			}

			if (!parentTsn.equals("0")) {
				returnVal.setParent(readTaxonomy(parentTsn));
			}
		} else {
			LOGGER.debug("Taxon found for " + returnVal.getName());
		}

		return returnVal;
	}
}
