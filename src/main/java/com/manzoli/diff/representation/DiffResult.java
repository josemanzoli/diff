package com.manzoli.diff.representation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.manzoli.diff.controller.DiffController;
import com.manzoli.diff.enumeration.DiffStatus;
import com.manzoli.diff.model.Difference;

/**
 * 
 * @author jmanzol
 * @since 0.0.1
 * Representation object that will be transformed to JSON and returned as the result of the difference between the JSON's received at {@link DiffController}.
 *  
 */
@JsonInclude(Include.NON_EMPTY)
public class DiffResult {
	
	/** The result of the difference itself. {@link DiffStatus#EQUAL} {@link DiffStatus#DIFFERENT_SIZE} */ 
	private String diffStatus;
	private List<Difference> differences;
	
	public DiffResult(){
		
	}
	
	public DiffResult(String diffStatus, List<Difference> differences){
		this.diffStatus = diffStatus;
		this.differences = differences;
	}

	@JsonProperty("result")
	public String getDiffStatus() {
		return diffStatus;
	}

	public void setDiffStatus(String diffStatus) {
		this.diffStatus = diffStatus;
	}

	public List<Difference> getDifferences() {
		return differences;
	}

	public void setDifferences(List<Difference> differences) {
		this.differences = differences;
	}

}
