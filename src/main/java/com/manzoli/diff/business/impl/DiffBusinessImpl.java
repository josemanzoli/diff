package com.manzoli.diff.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.manzoli.diff.business.DiffBusiness;
import com.manzoli.diff.enumeration.DiffStatus;
import com.manzoli.diff.model.Difference;
import com.manzoli.diff.model.ReceivedJsons;
import com.manzoli.diff.representation.DiffResult;

@Component
public class DiffBusinessImpl implements DiffBusiness {

	/**
	 * Create an representation object (Transfer Object) for the http answer of the third endpoint
	 * @param {@link ReceivedJsons}
	 * @return {@link DiffResult}
	 */
	@Override
	public DiffResult createDiffResultFromReceiveidJsons(ReceivedJsons receivedJsons) {
		DiffResult diffResult = new DiffResult();
		diffResult.setDiffStatus(receivedJsons.getDiffStatus().getCompare());
		diffResult.setDifferences(receivedJsons.getDifferences());
		return diffResult;
	}

	/**
	 * Make the actual comparison between both JSON's received.
	 * @param {@link ReceivedJsons}
	 * @return {@link ReceivedJsons}
	 */
	@Override
	public ReceivedJsons makeComparisonBetweenJsons(ReceivedJsons receivedJsons) {
		
		if (receivedJsons.getLeftJson().equals(receivedJsons.getRightJson())){
			receivedJsons.setDiffStatus(DiffStatus.EQUAL);
			return receivedJsons;
		}

		receivedJsons.setDiffStatus(DiffStatus.DIFFERENT_SIZE);
		return receivedJsons;
	}

	/**
	 * Method that gets two generic Json's and do a Map comparison between the JSON tree.
	 * @param rightJson {@link String}
	 * @param leftJson {@link String}
	 * @return {@link List<Difference>}
	 */
	@Override
	public List<Difference> parseAndCompareGenericJson(String rightJson, String leftJson) {
		List<Difference> differences = new ArrayList<Difference>();
		
		return differences;
	}
	

}
