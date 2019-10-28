package br.com.comprasa2.apicore.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.NotFoundException;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ListFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.comprasa2.apicore.exception.DuplicatedException;
import br.com.comprasa2.apicore.model.ListItem;
import br.com.comprasa2.apicore.model.Lista;
import br.com.comprasa2.apicore.model.ShortInfoUser;
import br.com.comprasa2.apicore.model.Usuario;
import br.com.comprasa2.apicore.repository.ListaRepository;

@Service
@Component
public class ListaService {

	@Autowired
	private ListaRepository listaRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private MongoTemplate mongoTemplate;



	public Lista insert(Lista model)  {
		List<ShortInfoUser> users = new ArrayList<>();
		users.add(new ShortInfoUser(userService.usuarioLogado));
		model.setUsers(users);
		model.setId(null);
		Lista user = listaRepository.save(model);
		return user;
	}

	public void delete(String id)  {
		listaRepository.deleteById(id);
	}

	public Lista update(Lista model)  {

		if(loadListaById(model.getId()) != null ) {
			Lista user = listaRepository.save(model);
			return user;
		}
		else {
			throw new NotFoundException("Lista não encontrada");
		}

	}

	public List<ShortInfoUser> addUserToList(String userEmailOrPhone, String listId) throws DuplicatedException{

		Usuario userFound = userService.checkUserByEmailOrPhone(userEmailOrPhone);
		if(userFound == null) {
			throw new NotFoundException("Usuário não encontrado!");
		}

		Lista listaTrabalho = loadListaById(listId);
		if(listaTrabalho.getUsers().stream().anyMatch(r-> 
		r.getEmail().toLowerCase().equals(userEmailOrPhone.toLowerCase()) ||
		r.getPhone().toLowerCase().equals(userEmailOrPhone.toLowerCase()))) {
			throw new DuplicatedException("Usuário já está na lista!");
		}

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(listId));
		Update update = new Update();
		update.push("users", new ShortInfoUser(userFound));

		mongoTemplate.updateFirst(query, update, Lista.class);

		listaTrabalho.getUsers().add(new ShortInfoUser(userFound));
		return listaTrabalho.getUsers();
	}
	public List<ShortInfoUser> removeUserFromList(String userEmailOrPhone, String listId){

		
		Lista listaTrabalho = loadListaById(listId);
		Optional<ShortInfoUser> itemFound = listaTrabalho.getUsers().stream().filter(r-> 
			r.getEmail().toLowerCase().equals(userEmailOrPhone.toLowerCase()) ||
			r.getPhone().equals(userEmailOrPhone)
				).findFirst();

		if(!itemFound.isPresent()) {
			throw new NotFoundException("Usuário não encontrado!");
		}
		
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(listId));
		Update update = new Update();
		update.pull("users", itemFound.get());
		
		mongoTemplate.updateFirst(query, update, Lista.class);
		
		Lista listaSaida = loadListaById(listId);
		return listaSaida.getUsers();
	}



	public Lista loadListaById(String id) {
		Optional<Lista> user =  listaRepository.findById(id);
		if(user.isPresent()) {
			return user.get();
		}		
		return null;
	}

	public List<ShortInfoUser> loadUsersFromList(String id) {
		Optional<Lista> user =  listaRepository.findById(id);
		if(user.isPresent()) {
			return user.get().getUsers();
		}		
		return null;
	}

	public List<Lista> loadListsByUser(String userId) {
		Query query = Query.query(Criteria.where("users.uid").is(userId));
		return mongoTemplate.find(query, Lista.class);
	}



	public List<ListItem> addItemToList(ListItem item, String listId) throws DuplicatedException{

		item.setId(ObjectId.get().toString());

		mongoTemplate.updateFirst(
				Query.query(Criteria.where("_id").is(new ObjectId(listId))), 
				new Update().push("itens", item), Lista.class);




		Lista listaTrabalho = loadListaById(listId);
		return listaTrabalho.getItens();
	}
	public List<ListItem> updateItemToList(ListItem item, String listId) throws DuplicatedException{

		Lista listaTrabalho = loadListaById(listId);
		Optional<ListItem> itemFound = listaTrabalho.getItens().stream().filter(r-> 
		r.getId().equals(item.getId())).findFirst();

		if(!itemFound.isPresent()) {
			throw new NotFoundException("Item não encontrado!");
		}


		mongoTemplate.updateFirst(
				Query.query(Criteria.where("_id").is(listId)), 
				new Update().pull("itens", itemFound), List.class);


		mongoTemplate.updateFirst(
				Query.query(Criteria.where("_id").is(listId)), 
				new Update().push("itens", item), List.class);

		Lista listaSaida = loadListaById(listId);
		return listaSaida.getItens();
	}
	public List<ListItem> removeItemFromList(String itemId, String listId){

		
		Lista listaTrabalho = loadListaById(listId);
		Optional<ListItem> itemFound = listaTrabalho.getItens().stream().filter(r-> 
		r.getId().equals(itemId)).findFirst();

		if(!itemFound.isPresent()) {
			throw new NotFoundException("Item não encontrado!");
		}
		
		Query query = new Query();
		query.addCriteria(Criteria.where("itens.uid").is(itemId));
		Update update = new Update();
		update.pull("itens", itemFound.get());
		
		mongoTemplate.updateFirst(query, update, Lista.class);
		
		
		Lista listaSaida = loadListaById(listId);
		return listaSaida.getItens();
		
	}
	public List<ListItem> removeAllItensFromList(String listId){

		mongoTemplate.updateFirst(
				Query.query(Criteria.where("_id").is(listId)), 
				new Update().pull("itens", new ArrayList<>()), List.class);

		Lista listaSaida = loadListaById(listId);
		return listaSaida.getItens();
	}
	public List<ListItem> loadItensFromList(String id) {
		Optional<Lista> user =  listaRepository.findById(id);
		if(user.isPresent()) {
			return user.get().getItens();
		}		
		return null;
	}

	public List<ListItem> comprarItems(String itemId, String listId, boolean comprado) throws DuplicatedException{

		Query query = new Query();
		query.addCriteria(Criteria.where("itens.uid").is(itemId));
		Update update = new Update();
		update.set("itens.$.comprado", comprado);
		mongoTemplate.updateFirst(query, update, Lista.class);


		Lista listaSaida = loadListaById(listId);
		return listaSaida.getItens();
	}
	public List<ListItem> reservarItems(String itemId, String listId, String userId) throws DuplicatedException{

		Query query = new Query();
		query.addCriteria(Criteria.where("itens.uid").is(itemId));
		Update update = new Update();

		
		Usuario userFound = null;

		if(userId != null) {
			if(userService.usuarioLogado.getId().equals(userId)) {
				userFound = userService.usuarioLogado;
			}
			else {
				userFound = userService.loadUserById(userId);
			}
			update.set("itens.$.user", new ShortInfoUser(userFound));
		}
		else {
			update.set("itens.$.user", null);
		}

		mongoTemplate.updateFirst(query, update, Lista.class);

		Lista listaSaida = loadListaById(listId);
		return listaSaida.getItens();
	}

	public List<ListItem> resetItems(String listId) throws DuplicatedException{

		Lista listaFound = loadListaById(listId);
		
		List<ListItem> lstItens = new ArrayList<>();
		for (ListItem item : listaFound.getItens()) {
			item.setComprado(false);
			item.setExcluido(false);
			item.setUser(null);
			lstItens.add(item);
		}
		listaFound.setItens(lstItens);
		this.update(listaFound);

		return listaFound.getItens();
	}



}
