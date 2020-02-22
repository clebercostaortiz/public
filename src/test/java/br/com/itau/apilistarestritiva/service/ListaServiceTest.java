package br.com.itau.apilistarestritiva.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.NotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;

import br.com.itau.apilistarestritiva.model.ListaRestritivaItem;
import br.com.itau.apilistarestritiva.model.TipoItem;
import br.com.itau.apilistarestritiva.repository.ListaRepository;

@RunWith(MockitoJUnitRunner.class)
public class ListaServiceTest {
	
	@InjectMocks
	private ListaService service;

	@Mock 
	private MongoTemplate mongoTemplate;

	@Mock 
	private ListaRepository listaRepository;

	private ListaRestritivaItem lista = new ListaRestritivaItem();
	
	
	@Before
	public void before() {
		lista.setId("cpf-do-item");
		lista.setListas(new ArrayList<>());
		lista.setLocal("local do item");
		lista.setName("nome do item");
		lista.setParametros(new HashMap<String, String>());
		lista.setTipoItem(TipoItem.CLIENTE_PF);
		lista.setTsDataInclusao(new Date().getTime());
	}
	
	@Test
	public void testInsertItem() {
		
		Mockito.when(listaRepository.insert(Mockito.any(ListaRestritivaItem.class)))
		.thenReturn(lista);
		
		ListaRestritivaItem itemSaved = service.insert(lista);
		
		assertEquals(lista.getId(), itemSaved.getId());
		
	}
	
	
	@Test
	public void testUpdateItem() {
		
		Mockito.when(listaRepository.save(Mockito.any(ListaRestritivaItem.class)))
		.thenReturn(lista);
		
		ListaRestritivaItem itemSaved = service.update(lista);
		
		assertEquals(lista.getId(), itemSaved.getId());
		
	}
	
	
	@Test
	public void testDeleteItem() {
		assertTrue(service.delete("id"));
	}
	
	@Test
	public void testDeleteItemError() {
		
		
		Mockito.doThrow(new IllegalArgumentException()).when(listaRepository).deleteById(Mockito.anyString());
		
		try {
			assertTrue(service.delete("id"));
		}
		catch (Exception e) {
			assertEquals(IllegalArgumentException.class, e.getClass());
		}

	}
	
	
	
	
	
	
	
	
	
	@Test
	public void testGetByIdItem() {
		
		Mockito.when(listaRepository.findById(Mockito.anyString()))
		.thenReturn(Optional.of(lista));
		
		ListaRestritivaItem itemSaved = service.getById(lista.getId());
		
		assertEquals(lista.getId(), itemSaved.getId());
		
	}
	
	@Test
	public void testGetByIdItemError() {
		
		Mockito.when(listaRepository.findById(Mockito.anyString()))
		.thenReturn(Optional.empty());
		
		try {
			ListaRestritivaItem itemSaved = service.getById(lista.getId());
			assertNotEquals(lista.getId(), itemSaved.getId());
		}
		catch (Exception e) {
			assertEquals(NotFoundException.class, e.getClass());
		}
		
		
	}
	
	
	
	
	
	@Test
	public void testGetByUpdatedAfter() {
		List<ListaRestritivaItem> itens = new ArrayList<>();
		itens.add(lista);
		
		Mockito.when(listaRepository.findListByTsDataUpdateGreaterThan(Mockito.anyLong()))
		.thenReturn(itens);
		
		assertEquals(1, service.getByUpdatedAfter(0l).size());
		
	}
	
	
	
	@Test
	public void testGetByLista() {
		List<ListaRestritivaItem> itens = new ArrayList<>();
		itens.add(lista);
		
		Mockito.when(mongoTemplate.find(Mockito.any(), Mockito.eq(ListaRestritivaItem.class)))
		.thenReturn(itens);
		
		assertEquals(1, service.GetItensWithSomeList("").size());
		
	}
	
	
	@Test
	public void testRemoveLista() {
	
		Mockito.when(listaRepository.findById(Mockito.anyString()))
		.thenReturn(Optional.of(lista));
		
		assertEquals(lista.getId(), service.removeListaFromItem("itemId", "lista").getId());
		
	}
	
	
	@Test
	public void testAddLista() {
	
		Mockito.when(listaRepository.findById(Mockito.anyString()))
		.thenReturn(Optional.of(lista));
		
		assertEquals(lista.getId(), service.addListaIntoItem("itemId", "lista").getId());
		
	}

	
	
	
	
	
	
	
	
	
	
}
