package com.app.notificaciones.models;

import java.util.List;

import javax.validation.constraints.Email;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notificaciones")
public class Notificaciones {

	@Id
	private String id;

	@Indexed(unique = true)
	private String username;

	@Indexed(unique = true)
	@Email
	private String email;

	private List<List<String>> mensajes;
	private Boolean activar;

	public Notificaciones() {
	}

	public Notificaciones(String username, @Email String email, List<List<String>> mensajes, Boolean activar) {
		super();
		this.username = username;
		this.email = email;
		this.mensajes = mensajes;
		this.activar = activar;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<List<String>> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<List<String>> mensajes) {
		this.mensajes = mensajes;
	}

	public Boolean getActivar() {
		return activar;
	}

	public void setActivar(Boolean activar) {
		this.activar = activar;
	}

}
