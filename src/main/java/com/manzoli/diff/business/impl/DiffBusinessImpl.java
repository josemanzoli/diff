package com.manzoli.diff.business.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.manzoli.diff.business.DiffBusiness;
import com.manzoli.diff.enumeration.DiffStatus;
import com.manzoli.diff.model.Difference;
import com.manzoli.diff.model.ReceivedJsons;
import com.manzoli.diff.representation.DiffResult;

@Component
public class DiffBusinessImpl implements DiffBusiness {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DiffBusinessImpl.class);

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
		receivedJsons.setDifferences(parseAndCompareGenericJson(receivedJsons.getRightJson(), receivedJsons.getLeftJson()));
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
		Set<Difference> differences = new HashSet<Difference>();
		try {
			Map<String, Object> rightMap = getJsonMap(rightJson);
			Map<String, Object> letfMap = getJsonMap(leftJson);
			
			//comparing the rightJson with the LeftJson
			for (Map.Entry<String, Object> rightEntry : rightMap.entrySet()){
				Object leftObject = letfMap.get(rightEntry.getKey());
				if (leftObject == null){
					Difference difference = new Difference(rightEntry.getKey(), rightEntry.getValue().toString().length(), null);
					differences.add(difference);
				} else if (!leftObject.equals(rightEntry.getValue())){
					Difference difference = new Difference(rightEntry.getKey(), rightEntry.getValue().toString().length(), leftObject.toString().length());
					differences.add(difference);
				}
			}
			
			//comparing the LeftJson with the RightJson
			for (Map.Entry<String, Object> leftEntry : letfMap.entrySet()){
				Object rightObject = rightMap.get(leftEntry.getKey());
				if (rightObject == null){
					Difference difference = new Difference(leftEntry.getKey(), null, leftEntry.getValue().toString().length());
					differences.add(difference);
				} else if (!rightObject.equals(leftEntry.getValue())){
					Difference difference = new Difference(leftEntry.getKey(), rightObject.toString().length(), leftEntry.getValue().toString().length());
					differences.add(difference);
				}
			}
		} catch (Exception e) {
			//there's no need to do anything with this exception
			LOGGER.warn("Validated json giving exception at parseAndCompareGenericJson: {}", e.getMessage());
		}
		
		return new ArrayList<Difference>(differences);
	}
	
	/**
	 * The JSON is validated at this point so we don't need to validate again.
	 * @param json
	 * @return Map<String, Object>
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 */
	public Map<String, Object> getJsonMap(String json) throws JsonProcessingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		MapType mapType = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
		return mapper.readValue(json, mapType);
	}

}
