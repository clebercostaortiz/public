package br.com.comprasa2.apicore.model;

import org.springframework.data.mongodb.core.mapping.Field;

public class ShortInfoUser {

	@Field("uid")
	private String id="";
	@Field("nome")
	private String nome="";
	@Field("email")
	private String email="";
	@Field("phone")
	private String phone="";
	
	
	public ShortInfoUser() {}
	public ShortInfoUser(Usuario _user) {
		this.setId(_user.getId());
		this.setNome(_user.getName());
		this.setEmail(_user.getUsername());
		this.setPhone(_user.getPhone());
	}
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	
	
	
}
