package com.manzoli.diff.service.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.json.JsonSanitizer;
import com.manzoli.diff.enumeration.DiffStatus;
import com.manzoli.diff.enumeration.Side;
import com.manzoli.diff.model.ReceivedJsons;
import com.manzoli.diff.repository.ReceivedJsonsRepository;
import com.manzoli.diff.representation.DiffResult;
import com.manzoli.diff.service.DiffService;
import com.manzoli.diff.service.exceptions.DiffJsonValidationException;
import com.manzoli.diff.service.exceptions.IdNotFoundException;
import com.manzoli.diff.service.exceptions.MissingJsonException;

/**
 * 
 * @author jmanzol
 * @since 0.0.1
 * Service that implements the business methods to find the diff between the jsonBase64 encoded.
 * 
 */
@Service
public class DiffServiceImpl implements DiffService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DiffServiceImpl.class);

	@Autowired
	private ReceivedJsonsRepository receivedJsonsRepository;
	
	/**
	 * Method that validates if the String is a correct base64 JSON. 
	 * @param jsonBase64
	 */
	@Override
	public String transformAndSanitizeJsonBase64(String jsonBase64) throws DiffJsonValidationException {
		try {
			byte[] decodedJson = Base64Utils.decodeFromString(jsonBase64);
			String json = new String(decodedJson);
			final String sanitizedJson = JsonSanitizer.sanitize(json);
			LOGGER.info("JSON validated and sanitized");
			
			return sanitizedJson;	
		} catch (Exception e) {
			throw new DiffJsonValidationException("Invalid JSON base64 encoded: " + e.getMessage());
		}
	}
	
	/**
	 * Method that inserts or update a document in received_jsons collection at MongoDB.
	 * @param id
	 * @param jsonReceived
	 * @param {@link Side}
	 * @return The saved Entity at MongoDB repository
	 */
	@Override
	public ReceivedJsons saveReceivedJson(Integer id, String jsonReceived, Side side){
		ReceivedJsons receivedJsons = receivedJsonsRepository.findById(id);
		
		if (receivedJsons == null) {
			receivedJsons = new ReceivedJsons();
			receivedJsons.setId(id);
		}
		
		if (Side.LEFT.equals(side)) {
			receivedJsons.setLeftJson(jsonReceived);
		} else {
			receivedJsons.setRightJson(jsonReceived);
		}
		
		if (receivedJsons.getLeftJson() !=null && receivedJsons.getRightJson() != null){
			makeComparisonBetweenJsons(receivedJsons);
		} 
		
		return receivedJsonsRepository.save(receivedJsons);
	}

	/**
	 * Method that validates if the JSON is a valid JSON by reading the three.
	 * @param json
	 * @return true or false
	 */
	@Override
	public boolean validateJson(String json) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.readTree(json);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Method that get the object from MongoDB and do the comparison between the two JSON's if it is not made yet.
	 * @param id
	 * @return {@link DiffResult}
	 * @throws IdNotFoundException 
	 * @throws MissingJsonException 
	 */
	@Override
	public String getComparedJsons(Integer id) throws IdNotFoundException, MissingJsonException {
		ReceivedJsons receivedJsons = receivedJsonsRepository.findById(id);
		
		if (receivedJsons == null) {
			throw new IdNotFoundException("Id not found at repository");
		}
		
		if (receivedJsons.getLeftJson() == null) {
			throw new MissingJsonException("Missing the Left JSON");
		}
		
		if (receivedJsons.getRightJson() == null) {
			throw new MissingJsonException("Missing the Right JSON");
		}
		
		if (receivedJsons.getDiffStatus() != null){
			return toJson(createDiffResultFromReceiveidJsons(receivedJsons));
		}
		
		return makeComparisonBetweenJsons(receivedJsons);
	}

	/**
	 * Method that transform the Result in a JSON String. 
	 */
	@Override
	public String toJson(DiffResult diffResult) {
		final ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(diffResult);
		} catch (JsonProcessingException e) {
			final String message = "Error converting DiffResult to a JSON String:";
			LOGGER.error(message + " {}", e.getMessage());
			return message;
		}
	}
	
	/**
	 * Create an representation object (Transfer Object) for the http answer of the third endpoint
	 * @param receivedJsons
	 * @return
	 */
	private DiffResult createDiffResultFromReceiveidJsons(ReceivedJsons receivedJsons){
		DiffResult diffResult = new DiffResult();
		diffResult.setDiffStatus(receivedJsons.getDiffStatus().getCompare());
		diffResult.setDifferences(receivedJsons.getDifferences());
		return diffResult;
	}
	
	/**
	 * Make the actual comparison between both JSON's received.
	 * @param receivedJsons
	 * @return
	 */
	private String makeComparisonBetweenJsons(ReceivedJsons receivedJsons) {
		DiffResult diffResult = new DiffResult();
		
		if (receivedJsons.getLeftJson().equals(receivedJsons.getRightJson())){
			diffResult.setDiffStatus(DiffStatus.EQUAL.getCompare());
			
			receivedJsons.setDiffStatus(DiffStatus.EQUAL);
			receivedJsonsRepository.save(receivedJsons);
			
			return toJson(diffResult);
		}
		
		diffResult.setDiffStatus(DiffStatus.DIFFERENT_SIZE.getCompare());
		
		receivedJsons.setDiffStatus(DiffStatus.DIFFERENT_SIZE);
		receivedJsonsRepository.save(receivedJsons);
		
		return toJson(diffResult);
	}
}
