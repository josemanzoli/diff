package com.manzoli.diff.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.Base64Utils;

import com.manzoli.diff.business.DiffBusiness;
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
 * Unit tests for {@link DiffService} 
 * 
 * @author jmanzol
 * @since 0.0.1
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DiffServiceImplTest {

	@Mock
	private ReceivedJsonsRepository receivedJsonsRepository;
	
	@Mock
	private DiffBusiness diffBusiness;
	
	@InjectMocks
	private DiffService diffService = new DiffServiceImpl();
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void shouldTransformAndSanitizeJsonBase64() throws DiffJsonValidationException{
		String encodedJson = Base64Utils.encodeToString("{\"name\":\"jsonBase64\"}".getBytes());
		String json = diffService.transformAndSanitizeJsonBase64(encodedJson);
		assertNotNull("should not be null", json);
	}
	
	@Test(expected=DiffJsonValidationException.class)
	public void shouldThrowDiffJsonValidationException() throws DiffJsonValidationException{
		String wrongEncodedString = "asdkfjhadsfkhasf87686=";
		diffService.transformAndSanitizeJsonBase64(wrongEncodedString);
	}
	
	@Test
	public void shouldBeSavedLeftJson(){
		Integer id = 1;
		String jsonReceived = "{\"name\":\"jsonBase64\"}";
		
		ReceivedJsons receivedJsons = new ReceivedJsons();
		receivedJsons.setId(id);
		receivedJsons.setLeftJson(jsonReceived);
		
		when(receivedJsonsRepository.save(receivedJsons)).thenReturn(receivedJsons);
		
		ReceivedJsons receivedJsons2 = diffService.saveReceivedJson(id, jsonReceived, Side.LEFT);
		assertNotNull(receivedJsons2);
	}
	
	@Test
	public void shouldBeSavedRightJson(){
		Integer id = 1;
		String jsonReceived = "{\"name\":\"jsonBase64\"}";
		
		ReceivedJsons receivedJsons = new ReceivedJsons();
		receivedJsons.setId(id);
		receivedJsons.setRightJson(jsonReceived);
		
		when(receivedJsonsRepository.save(receivedJsons)).thenReturn(receivedJsons);
		
		ReceivedJsons receivedJsons2 = diffService.saveReceivedJson(id, jsonReceived, Side.RIGHT);
		assertNotNull(receivedJsons2);
	}
	
	@Test
	public void shouldBeValidJson(){
		String json = "{\"name\":\"jsonBase64\"}";
		assertTrue(diffService.validateJson(json));
	}
	
	@Test
	public void shouldNotBeValidJson(){
		String json = "name:jsonBase64";
		assertFalse(diffService.validateJson(json));
	}
	
	@Test(expected=IdNotFoundException.class)
	public void shouldThrowIdNotFoundException() throws IdNotFoundException, MissingJsonException{
		Integer id = 1;
		when(receivedJsonsRepository.findById(id)).thenReturn(null);
		diffService.getComparedJsons(id);
	}
	
	@Test
	public void shouldThrowMissingJsonExceptionForTheLeft() throws IdNotFoundException, MissingJsonException{
		Integer id = 1;
		
		ReceivedJsons receivedJsons = new ReceivedJsons();
		receivedJsons.setId(id);
		receivedJsons.setRightJson("{\"name\":\"jsonBase64\"}");
		
		thrown.expect(MissingJsonException.class);
		thrown.expectMessage("Missing the Left JSON");
		
		when(receivedJsonsRepository.findById(id)).thenReturn(receivedJsons);
		
		diffService.getComparedJsons(id);
	}
	
	@Test
	public void shouldThrowMissingJsonExceptionForTheRight() throws IdNotFoundException, MissingJsonException{
		Integer id = 1;
		
		ReceivedJsons receivedJsons = new ReceivedJsons();
		receivedJsons.setId(id);
		receivedJsons.setLeftJson("{\"name\":\"jsonBase64\"}");
		
		when(receivedJsonsRepository.findById(id)).thenReturn(receivedJsons);
		
		thrown.expect(MissingJsonException.class);
		thrown.expectMessage("Missing the Right JSON");
		
		diffService.getComparedJsons(id);
	}
	
	@Test
	public void shouldCreateStringEqualsAnswer() throws IdNotFoundException, MissingJsonException{
		Integer id = 1;
		
		ReceivedJsons receivedJsons = new ReceivedJsons();
		receivedJsons.setId(id);
		receivedJsons.setLeftJson("{\"name\":\"jsonBase64\"}");
		receivedJsons.setRightJson("{\"name\":\"jsonBase64\"}");
		receivedJsons.setDiffStatus(DiffStatus.EQUAL);
		
		when(receivedJsonsRepository.findById(id)).thenReturn(receivedJsons);
		when(diffBusiness.makeComparisonBetweenJsons(receivedJsons)).thenReturn(receivedJsons);
		
		DiffResult diffResult = new DiffResult();
		diffResult.setDiffStatus(receivedJsons.getDiffStatus().getCompare());
		diffResult.setDifferences(receivedJsons.getDifferences());
	
		when(diffBusiness.createDiffResultFromReceiveidJsons(receivedJsons)).thenReturn(diffResult);
		
		String compare = diffService.getComparedJsons(id);
		
		assertEquals(compare,"{\"result\":\"JSON's provided are equals\"}");
	}
	
	@Test
	public void shouldCreateStringDifferentAnswer() throws IdNotFoundException, MissingJsonException{
		Integer id = 1;
		
		ReceivedJsons receivedJsons = new ReceivedJsons();
		receivedJsons.setId(id);
		receivedJsons.setLeftJson("{\"name\":\"jsonBase64\"}");
		receivedJsons.setRightJson("{\"name\":\"another super cool string with more complexity\"}");
		receivedJsons.setDiffStatus(DiffStatus.DIFFERENT_SIZE);
		
		when(receivedJsonsRepository.findById(id)).thenReturn(receivedJsons);
		when(diffBusiness.makeComparisonBetweenJsons(receivedJsons)).thenReturn(receivedJsons);
		
		DiffResult diffResult = new DiffResult();
		diffResult.setDiffStatus(receivedJsons.getDiffStatus().getCompare());
		diffResult.setDifferences(receivedJsons.getDifferences());
	
		when(diffBusiness.createDiffResultFromReceiveidJsons(receivedJsons)).thenReturn(diffResult);
		
		String compare = diffService.getComparedJsons(id);
		
		assertEquals(compare,"{\"result\":\"JSON's provided have different sizes\"}");
	}
}
