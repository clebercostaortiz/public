package br.com.comprasa2.apicore.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "listas")
public class Lista {
	
	@Id
	private String id="";

	@Field("nome")
	private String nome="";
	
	@Field("descricao")
	private String descricao="";

	private List<ShortInfoUser> users = new ArrayList<>();
	private List<ListItem> itens = new ArrayList<>();

	
	
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ShortInfoUser> getUsers() {
		return users;
	}

	public void setUsers(List<ShortInfoUser> users) {
		this.users = users;
	}

	public List<ListItem> getItens() {
		return itens;
	}

	public void setItens(List<ListItem> itens) {
		this.itens = itens;
	}

}
