package br.com.itau.apilistarestritiva.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class ListaRestritivaItem {
	
	@Id
	private String id;
	@Field("nome")
	private String nome;
	@Field("tipoItem")
	private TipoItem tipoItem; 
	@Field("local")
	private String local;
	@Field("listas")
	private List<String> listas;
	@Field("parametros")
	private Map<String, String> parametros;
	@Field("tsDataUpdate")
	private long tsDataUpdate;
	
	public ListaRestritivaItem() {}
	public ListaRestritivaItem(String _id, String _name, TipoItem _tipoItem, String _local) {
		id= _id;
		nome = _name;
		tipoItem = _tipoItem;
		local = _local;
		tsDataUpdate = new Date().getTime();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return nome;
	}
	public void setName(String name) {
		this.nome = name;
	}
	public TipoItem getTipoItem() {
		return tipoItem;
	}
	public void setTipoItem(TipoItem tipoItem) {
		this.tipoItem = tipoItem;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public long getTsDataInclusao() {
		return tsDataUpdate;
	}
	public void setTsDataInclusao(long tsDataInclusao) {
		this.tsDataUpdate = tsDataInclusao;
	}
	public List<String> getListas() {
		return listas;
	}
	public void setListas(List<String> listas) {
		this.listas = listas;
	}
	public Map<String, String> getParametros() {
		return parametros;
	}
	public void setParametros(Map<String, String> parametros) {
		this.parametros = parametros;
	}
	
	

}
