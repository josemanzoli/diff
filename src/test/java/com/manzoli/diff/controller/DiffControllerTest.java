package com.manzoli.diff.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;

import com.manzoli.diff.enumeration.Side;
import com.manzoli.diff.model.ReceivedJsons;
import com.manzoli.diff.service.DiffService;

@RunWith(MockitoJUnitRunner.class)
public class DiffControllerTest {
	
	@Mock
	private DiffService diffServiceMock;
	
	@InjectMocks
	private DiffController diffController;
	
	private String encodedJson = Base64Utils.encodeToString("{\"name\":\"jsonBase64\"}".getBytes());
	private String unsupportedMediaType = Base64Utils.encodeToString("name jsonBase64".getBytes());
	private String json = "{\"name\":\"jsonBase64\"}";
		
	@Test
	public void shouldReturn201CreatedRightMethod() throws Exception{
		Integer id = 1;
		
		ReceivedJsons receivedJsons = new ReceivedJsons();
		receivedJsons.setId(id);
		receivedJsons.setRightJson(json);
		
		when(diffServiceMock.saveReceivedJson(id, json, Side.RIGHT)).thenReturn(receivedJsons);
		when(diffServiceMock.transformAndSanitizeJsonBase64(encodedJson)).thenReturn(json);
		when(diffServiceMock.validateJson(json)).thenReturn(true);
		
		ResponseEntity<Void> response  = diffController.rightJson(id, encodedJson);
		
		assertEquals(HttpStatus.CREATED_201, response.getStatusCodeValue());
	}
	
	@Test
	public void shouldReturn415UnsupportedMediaTypeRightMethod() throws Exception{
		Integer id = 1;
		
		ReceivedJsons receivedJsons = new ReceivedJsons();
		receivedJsons.setId(id);
		receivedJsons.setRightJson(json);
		
		when(diffServiceMock.saveReceivedJson(id, json, Side.RIGHT)).thenReturn(receivedJsons);
		when(diffServiceMock.transformAndSanitizeJsonBase64(encodedJson)).thenReturn(json);
		when(diffServiceMock.validateJson(json)).thenReturn(true);
		
		ResponseEntity<Void> response  = diffController.rightJson(id, unsupportedMediaType);
		
		assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE_415, response.getStatusCodeValue());
	}

	@Test
	public void shouldReturn201CreatedLeftMethod() throws Exception{
		Integer id = 1;
		
		ReceivedJsons receivedJsons = new ReceivedJsons();
		receivedJsons.setId(id);
		receivedJsons.setRightJson(json);
		
		when(diffServiceMock.saveReceivedJson(id, json, Side.LEFT)).thenReturn(receivedJsons);
		when(diffServiceMock.transformAndSanitizeJsonBase64(encodedJson)).thenReturn(json);
		when(diffServiceMock.validateJson(json)).thenReturn(true);
		
		ResponseEntity<Void> response  = diffController.rightJson(id, encodedJson);
		
		assertEquals(HttpStatus.CREATED_201, response.getStatusCodeValue());
	}
	
	@Test
	public void shouldReturn415UnsupportedMediaTypeLeftMethod() throws Exception{
		Integer id = 1;
		
		ReceivedJsons receivedJsons = new ReceivedJsons();
		receivedJsons.setId(id);
		receivedJsons.setRightJson(json);
		
		when(diffServiceMock.saveReceivedJson(id, json, Side.LEFT)).thenReturn(receivedJsons);
		when(diffServiceMock.transformAndSanitizeJsonBase64(encodedJson)).thenReturn(json);
		when(diffServiceMock.validateJson(json)).thenReturn(true);
		
		ResponseEntity<Void> response  = diffController.rightJson(id, unsupportedMediaType);
		
		assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE_415, response.getStatusCodeValue());
	}
}
