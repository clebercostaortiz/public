package br.com.itau.apilistarestritiva.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.itau.apilistarestritiva.model.ListaRestritivaItem;

public interface ListaRepository extends MongoRepository<ListaRestritivaItem, String>{

	List<ListaRestritivaItem> findListByTsDataUpdateGreaterThan(long time);

	
}
