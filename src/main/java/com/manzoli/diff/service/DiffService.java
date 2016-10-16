package com.manzoli.diff.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.manzoli.diff.enumeration.Side;
import com.manzoli.diff.model.ReceivedJsons;
import com.manzoli.diff.representation.DiffResult;
import com.manzoli.diff.service.exceptions.DiffJsonValidationException;
import com.manzoli.diff.service.exceptions.IdNotFoundException;
import com.manzoli.diff.service.exceptions.MissingJsonException;

public interface DiffService {

	ReceivedJsons saveReceivedJson(Integer id, String jsonReceived, Side side);

	String transformAndSanitizeJsonBase64(String jsonBase64) throws DiffJsonValidationException;
	
	boolean validateJson(String json);
	
	String getComparedJsons(Integer id) throws IdNotFoundException, MissingJsonException;

	String toJson(DiffResult diffResult) throws JsonProcessingException;
}
