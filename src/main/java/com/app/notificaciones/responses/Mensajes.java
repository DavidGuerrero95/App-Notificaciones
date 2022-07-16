package com.app.notificaciones.responses;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class Mensajes {

	@NotNull
	private String name;

	@NotNull
	@Email
	private String email;

	@NotNull
	private String mensaje;
	
	public Mensajes(String name, String email, String mensaje) {
		super();
		this.name = name;
		this.email = email;
		this.mensaje = mensaje;
	}
	
	public Mensajes() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	
}
