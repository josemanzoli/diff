package com.manzoli.diff.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.manzoli.diff.enumeration.Side;
import com.manzoli.diff.representation.DiffResult;
import com.manzoli.diff.service.DiffService;
import com.manzoli.diff.service.exceptions.DiffJsonValidationException;
import com.manzoli.diff.service.exceptions.IdNotFoundException;
import com.manzoli.diff.service.exceptions.MissingJsonException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * 
 * @author jmanzol
 * @since 0.0.1
 * Controller that handle the requests for this Web project.
 * Composed by 3 endpoints, two to receive a JSON base64 encoded for the left/right sides and one to return the result of the diff between the JSON's. 
 */
@Controller
@RequestMapping(path="/diff")
@Api(value = "diff")
public class DiffController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DiffController.class);
	
	@Autowired
	private DiffService diffService;

	/**
	 * @param id
	 * @param jsonBase64
	 * @return 201 - if is a valid jsonBase64
	 * @return 415 - if it's not a valid jsonBase64
	 * @return 500 - if some unrecognized error occurs.
	 */
	@PostMapping(path="/v1/diff/{ID}/left")
	@ResponseBody
	@ApiOperation(value = "Used to receive the Left JSON ", response = ResponseEntity.class, httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 415, message = "Unsupported Media Type"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public ResponseEntity<Void> leftJson(@PathVariable("ID") Integer id, @RequestBody String jsonBase64){
		return workForBothSides(id, jsonBase64, Side.LEFT);
	}

	/**
	 * @param id
	 * @param jsonBase64
	 * @return 201 - if is a valid jsonBase64
	 * @return 415 - if it's not a valid jsonBase64
	 * @return 500 - if some unrecognized error occurs.
	 */
	@PostMapping(path="/v1/diff/{ID}/right")
	@ResponseBody
	@ApiOperation(value = "Used to receive the Right JSON ", response = ResponseEntity.class, httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 415, message = "Unsupported Media Type"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public ResponseEntity<Void> rightJson(@PathVariable("ID") Integer id, @RequestBody String jsonBase64){ 
		return workForBothSides(id, jsonBase64, Side.RIGHT);
	}
	
	/**
	 * 
	 * @param id
	 * @param jsonBase64
	 * @param side
	 * @return the correct ResponseEntity for the right and left endpoints.
	 */
	private ResponseEntity<Void> workForBothSides(Integer id, String jsonBase64, Side side){
		try {
			String json = diffService.transformAndSanitizeJsonBase64(jsonBase64);
			if (json!=null && diffService.validateJson(json)){
				diffService.saveReceivedJson(id, json, side);
				return ResponseEntity.status(HttpStatus.CREATED).build();
			} else {
				return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();	
			}
		} catch (DiffJsonValidationException d) {
			LOGGER.error("Invalid JSON: {}", d.getMessage());
			return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
		} catch (Exception e) {
			LOGGER.error("Exception at workForBothSides: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * @param id
	 * @return 200 {@link DiffResult} the result of the differences between the left and right jsonBase64 that has been received and processed by the application.
	 * @return 404 - if the id is not found
	 * @return 500 - if some unrecognized error occurs.
	 */
	@GetMapping(path="/v1/diff/{ID}")
	@ResponseBody
	@ApiOperation(value = "Used to get the diff result", response = ResponseEntity.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "DiffResult"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public ResponseEntity<String> diffResult(@PathVariable("ID") Integer id){
		String diffResult = null;
		try {
			diffService.getComparedJsons(id);
		} catch (IdNotFoundException i) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(i.getMessage());
		} catch (MissingJsonException m) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m.getMessage());
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(diffResult);
	}

}
