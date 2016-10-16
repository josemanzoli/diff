package com.manzoli.diff.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.manzoli.diff.model.ReceivedJsons;
/**
 * 
 * @author jmanzol
 * @since 0.0.1
 * The repository interface that gives control of the {@link ReceivedJsons} collection at MongoDb database.
 *
 */
public interface ReceivedJsonsRepository extends MongoRepository<ReceivedJsons, Integer> {

	ReceivedJsons findById(Integer id);
}
