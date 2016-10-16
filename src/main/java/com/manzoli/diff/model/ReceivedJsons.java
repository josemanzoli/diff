package com.manzoli.diff.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.manzoli.diff.enumeration.DiffStatus;

/**
 * 
 * @author jmanzol
 * @since 0.0.1
 * Collection that holds all the information needed for this application work.
 */
@Document(collection="received_jsons")
public class ReceivedJsons {

	/** The ID received at the Post Request */
	@Id
	private Integer id;
	
	/** The left JSON received at the Post Request */
	private String leftJson;
	
	/** The right JSON received at the Post Request */
	private String rightJson;
	
	/** The DiffStatus {@link DiffStatus} */
	private DiffStatus diffStatus;
	
	/** A list of the Differences found between the received JSON's {@link Difference} */
	private List<Difference> differences;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLeftJson() {
		return leftJson;
	}
	public void setLeftJson(String leftJson) {
		this.leftJson = leftJson;
	}
	public String getRightJson() {
		return rightJson;
	}
	public void setRightJson(String rightJson) {
		this.rightJson = rightJson;
	}
	public DiffStatus getDiffStatus() {
		return diffStatus;
	}
	public void setDiffStatus(DiffStatus diffStatus) {
		this.diffStatus = diffStatus;
	}
	public List<Difference> getDifferences() {
		return differences;
	}
	public void setDifferences(List<Difference> differences) {
		this.differences = differences;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((diffStatus == null) ? 0 : diffStatus.hashCode());
		result = prime * result + ((differences == null) ? 0 : differences.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((leftJson == null) ? 0 : leftJson.hashCode());
		result = prime * result + ((rightJson == null) ? 0 : rightJson.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReceivedJsons other = (ReceivedJsons) obj;
		if (diffStatus != other.diffStatus)
			return false;
		if (differences == null) {
			if (other.differences != null)
				return false;
		} else if (!differences.equals(other.differences))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (leftJson == null) {
			if (other.leftJson != null)
				return false;
		} else if (!leftJson.equals(other.leftJson))
			return false;
		if (rightJson == null) {
			if (other.rightJson != null)
				return false;
		} else if (!rightJson.equals(other.rightJson))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "ReceivedJsons [id=" + id + ", leftJson=" + leftJson + ", rightJson=" + rightJson
				+ ", diffStatus=" + diffStatus + ", differences=" + differences + "]";
	}
	
}
