package br.com.comprasa2.apicore.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "users")
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 6045877200295077506L;
	
	@Id
	private String id="";

	@Field("userName")
	private String userName="";
	@Field("password")
	private String password="";
	@Field("name")
	private String name="";
	@Field("status")
	private String status="";
	@Field("phone")
	private String phone="";
	@Field("idGoogle")
	private String idGoogle="";
	@Field("users")
	private List<ShortInfoUser> users=new ArrayList<>();
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}



	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIdGoogle() {
		return idGoogle;
	}

	public void setIdGoogle(String idGoogle) {
		this.idGoogle = idGoogle;
	}

	public List<ShortInfoUser> getUsers() {
		return users;
	}

	public void setUsers(List<ShortInfoUser> users) {
		this.users = users;
	}

}
