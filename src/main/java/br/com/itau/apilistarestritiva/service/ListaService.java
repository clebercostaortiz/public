package br.com.itau.apilistarestritiva.service;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.itau.apilistarestritiva.model.ListaRestritivaItem;
import br.com.itau.apilistarestritiva.repository.ListaRepository;

@Service
@Component
public class ListaService {

	@Autowired
	private ListaRepository listaRepository;

	@Autowired
	private MongoTemplate mongoTemplate;



	public ListaRestritivaItem insert(ListaRestritivaItem model)  {
		return listaRepository.insert(model);
	}

	public boolean delete(String id)  {
		 try {
			 listaRepository.deleteById(id);
			 return true;
		 }
		 catch (Exception e) {
			throw e;
		}
	}

	public ListaRestritivaItem update(ListaRestritivaItem model)  {
		return listaRepository.save(model);
	}

	
	
	public ListaRestritivaItem getById(String id) throws NotFoundException {
		Optional<ListaRestritivaItem> item = listaRepository.findById(id);
		if(item.isPresent()) {
			return item.get();
		}
		else {
			throw new NotFoundException();
		}
	}

	public List<ListaRestritivaItem> getByUpdatedAfter(long time) throws NotFoundException {
		return listaRepository.findListByTsDataUpdateGreaterThan(time);
	}
	
	public List<ListaRestritivaItem> GetItensWithSomeList(String list) {
		Query query = Query.query(Criteria.where("lsitas").is(list));
		return mongoTemplate.find(query, ListaRestritivaItem.class);
	}

	
	public ListaRestritivaItem removeListaFromItem(String itemId, String list){
		
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(itemId));
		Update update = new Update();
		update.pull("itens", list);
		
		mongoTemplate.updateFirst(query, update, ListaRestritivaItem.class);
		
		return getById(itemId);
		
	}
	
	public ListaRestritivaItem addListaIntoItem(String itemId, String list){
		
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(itemId));
		Update update = new Update();
		update.push("itens", list);
		
		mongoTemplate.updateFirst(query, update, ListaRestritivaItem.class);
		
		return getById(itemId);
		
	}

	
	

}
