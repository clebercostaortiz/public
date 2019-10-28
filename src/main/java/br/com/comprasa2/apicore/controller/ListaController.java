package br.com.comprasa2.apicore.controller;

import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.comprasa2.apicore.model.ListItem;
import br.com.comprasa2.apicore.model.Lista;
import br.com.comprasa2.apicore.service.ListaService;

@RestController
@CrossOrigin
@RequestMapping("/lista")
public class ListaController {
	
	@Autowired
	private ListaService listaService;

	private static Logger logger = LoggerFactory.getLogger(ListaController.class);
	
	
	@PostMapping(value = { "/",  ""})
	public ResponseEntity<?> insert(@RequestBody Lista dto) {
		try {
			return ResponseEntity.ok(listaService.insert(dto));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(value = { "/{id-lista}",  "/{id-lista}/"})
	public ResponseEntity<?> update(@RequestBody Lista dto) {
		try {
			return ResponseEntity.ok(listaService.update(dto));
		} catch (NotFoundException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = { "/{id-lista}", "/{id-lista}/" })
	public ResponseEntity<?> getById(@PathVariable("id-lista") String idLista){
		try {
			return ResponseEntity.ok(listaService.loadListaById(idLista));
		} catch (NotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(value = { "/{id-lista}", "/{id-lista}/" })
	public ResponseEntity<?> deleteById(@PathVariable("id-lista") String idLista){
		try {
			listaService.delete(idLista);
			return ResponseEntity.ok("");
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@GetMapping(value = { "/by-user"})
	public ResponseEntity<?> getAllListByUser(@RequestParam(value = "user-id", required = true) String userId			
			) {
		try {
			return ResponseEntity.ok(listaService.loadListsByUser(userId));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = { "/{id-lista}/user"})
	public ResponseEntity<?> addUserToList(@PathVariable("id-lista") String idLista,
			@RequestParam(value = "email-or-phone", required = true) String emailOrPhone			
			) {
		try {
			return ResponseEntity.ok(listaService.addUserToList(emailOrPhone, idLista));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@DeleteMapping(value = { "/{id-lista}/user"})
	public ResponseEntity<?> removeUserToList(@PathVariable("id-lista") String idLista,
			@RequestParam(value = "email-or-phone", required = true) String emailOrPhone			
			) {
		try {
			return ResponseEntity.ok(listaService.removeUserFromList(emailOrPhone, idLista));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping(value = { "/{id-lista}/user"})
	public ResponseEntity<?> getUsersByIdLista(@PathVariable("id-lista") String idLista){
		try {
			return ResponseEntity.ok(listaService.loadUsersFromList(idLista));
		} catch (NotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	
	
	@PostMapping(value = { "/{id-lista}/item"})
	public ResponseEntity<?> addItemToList(@PathVariable("id-lista") String idLista,
			@RequestBody ListItem dto) {
		try {
			return ResponseEntity.ok(listaService.addItemToList(dto, idLista));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PutMapping(value = { "/{id-lista}/item"})
	public ResponseEntity<?> updateItemToList(@PathVariable("id-lista") String idLista,
			@RequestBody ListItem dto) {
		try {
			return ResponseEntity.ok(listaService.updateItemToList(dto, idLista));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@DeleteMapping(value = { "/{id-lista}/item/{id-item}"})
	public ResponseEntity<?> removeItemToList(@PathVariable("id-lista") String idLista,
			@PathVariable("id-item") String idItem) {
		try {
			return ResponseEntity.ok(listaService.removeItemFromList(idItem, idLista));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@DeleteMapping(value = { "/{id-lista}/item"})
	public ResponseEntity<?> removeAllItensToList(@PathVariable("id-lista") String idLista) {
		try {
			return ResponseEntity.ok(listaService.removeAllItensFromList(idLista));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping(value = { "/{id-lista}/item"})
	public ResponseEntity<?> getItensByIdLista(@PathVariable("id-lista") String idLista){
		try {
			return ResponseEntity.ok(listaService.loadItensFromList(idLista));
		} catch (NotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = { "/{id-lista}/item/{id-item}/comprar"})
	public ResponseEntity<?> comprarItem(@PathVariable("id-lista") String idLista,
			@RequestParam(value = "item-comprado", required = true) boolean itemComprado,
			@PathVariable("id-item") String idItem) {
		try {
			return ResponseEntity.ok(listaService.comprarItems(idItem, idLista, itemComprado));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(value = { "/{id-lista}/item/{id-item}/reservar"})
	public ResponseEntity<?> reservarItem(@PathVariable("id-lista") String idLista,
			@RequestParam(value = "item-user-id", required = false) String itemUserId,
			@PathVariable("id-item") String idItem) {
		try {
			return ResponseEntity.ok(listaService.reservarItems(idItem, idLista, itemUserId));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = { "/{id-lista}/item/reset"})
	public ResponseEntity<?> resetItem(@PathVariable("id-lista") String idLista) {
		try {
			return ResponseEntity.ok(listaService.resetItems(idLista));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
