package br.com.comprasa2.apicore.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.comprasa2.apicore.model.Log;

public interface LogRepository extends MongoRepository<Log, String>{

	
	
}
