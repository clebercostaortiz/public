package br.com.itau.apilistarestritiva.controller;

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

import br.com.itau.apilistarestritiva.model.ListaRestritivaItem;
import br.com.itau.apilistarestritiva.service.ListaService;

@RestController
@CrossOrigin
@RequestMapping("/lista")
public class ListaController {
	
	@Autowired
	private ListaService listaService;

	private static Logger logger = LoggerFactory.getLogger(ListaController.class);
	
	
	@PostMapping()
	public ResponseEntity<?> insert(@RequestBody ListaRestritivaItem dto) {
		try {
			return ResponseEntity.ok(listaService.insert(dto));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PutMapping(value = "/{id-lista}")
	public ResponseEntity<?> update(@RequestBody ListaRestritivaItem dto) {
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
	
	@GetMapping(value = "/{id-lista}")
	public ResponseEntity<?> getById(@PathVariable("id-lista") String idLista){
		try {
			return ResponseEntity.ok(listaService.getById(idLista));
		} catch (NotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(value = "/all-updated-before/{time}")
	public ResponseEntity<?> getByUpdateLessThen(@PathVariable("time") long time){
		try {
			return ResponseEntity.ok(listaService.getByUpdatedAfter(time));
		} catch (NotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/all-with-list/{list}")
	public ResponseEntity<?> getByUpdateLessThen(@PathVariable("list") String list){
		try {
			return ResponseEntity.ok(listaService.GetItensWithSomeList(list));
		} catch (NotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	@DeleteMapping(value = "/{id-lista}")
	public ResponseEntity<?> deleteById(@PathVariable("id-lista") String idLista){
		try {
			listaService.delete(idLista);
			return ResponseEntity.ok("");
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	

	@PostMapping(value = { "/{id-lista}/lista"})
	public ResponseEntity<?> AddLista(@PathVariable("id-lista") String idLista,
			@RequestParam(value = "lista", required = true) String lista) {
		try {
			return ResponseEntity.ok(listaService.addListaIntoItem(idLista, lista));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(value = { "/{id-lista}/lista"})
	public ResponseEntity<?> RemoverLista(@PathVariable("id-lista") String idLista,
			@RequestParam(value = "lista", required = true) String lista) {
		try {
			return ResponseEntity.ok(listaService.addListaIntoItem(idLista, lista));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
}
