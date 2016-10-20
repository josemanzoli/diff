package com.manzoli.diff.business;

import java.util.List;

import com.manzoli.diff.model.Difference;
import com.manzoli.diff.model.ReceivedJsons;
import com.manzoli.diff.representation.DiffResult;

public interface DiffBusiness {
	
	DiffResult createDiffResultFromReceiveidJsons(ReceivedJsons receivedJsons);
	
	ReceivedJsons makeComparisonBetweenJsons(ReceivedJsons receivedJsons);
	
	List<Difference> parseAndCompareGenericJson(String rightJson, String leftJson);
}
