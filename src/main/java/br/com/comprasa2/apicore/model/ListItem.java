package br.com.comprasa2.apicore.model;

import org.springframework.data.mongodb.core.mapping.Field;

public class ListItem {

	
	@Field("uid")
	private String id="";
	@Field("nome")
	private String nome="";
	@Field("obs")
    private String obs = "";
	@Field("comprado")
    private boolean comprado = false;
	@Field("excluido")
    private boolean excluido = false;
	@Field("categoria")
    private String categoria = "";
	@Field("unidade")
    private String unidade = "";
	@Field("tsLastUpdate")
    private long tsLastUpdate = 0;
	@Field("tsLastUpdateServer")
    private long tsLastUpdateServer = 0;
	@Field("qtd")
    private double qtd = 0;
	@Field("user")
    private ShortInfoUser user = null;
	
	
	
	
	
	
	
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
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	public boolean isComprado() {
		return comprado;
	}
	public void setComprado(boolean comprado) {
		this.comprado = comprado;
	}
	public boolean isExcluido() {
		return excluido;
	}
	public void setExcluido(boolean excluido) {
		this.excluido = excluido;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getUnidade() {
		return unidade;
	}
	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}
	public long getTsLastUpdate() {
		return tsLastUpdate;
	}
	public void setTsLastUpdate(long tsLastUpdate) {
		this.tsLastUpdate = tsLastUpdate;
	}
	public long getTsLastUpdateServer() {
		return tsLastUpdateServer;
	}
	public void setTsLastUpdateServer(long tsLastUpdateServer) {
		this.tsLastUpdateServer = tsLastUpdateServer;
	}
	public double getQtd() {
		return qtd;
	}
	public void setQtd(double qtd) {
		this.qtd = qtd;
	}
	public ShortInfoUser getUser() {
		return user;
	}
	public void setUser(ShortInfoUser user) {
		this.user = user;
	}
	
	
	
	
	
	
	
	
	
	
	
}
