package com.app.notificaciones.models;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "gamificacion-proyectos")
public class ProyectosGamificacion {

	@Id
	private String id;

	@NotBlank(message = "Name cannot be null")
	@Size(max = 200)
	@Indexed(unique = true)
	private String nombre;

	private String titulo;
	private List<String> premios;
	private String tyc;
	private Date fechaTerminacion;
	private List<String> patrocinadores;
	private List<String> usuariosParticipantes;
	private List<String> usuariosGanadores;
	private Boolean habilitado;
	private Integer ganadores;
	private String mensajeParticipacion;
	private String mensajeGanador;

	public ProyectosGamificacion() {
	}

	public ProyectosGamificacion(String nombre, String titulo, List<String> premios, String tyc, Date fechaTerminacion,
			List<String> patrocinadores, List<String> usuariosParticipantes, List<String> usuariosGanadores,
			Boolean habilitado, Integer ganadores, String mensajeParticipacion, String mensajeGanador) {
		super();
		this.nombre = nombre;
		this.titulo = titulo;
		this.premios = premios;
		this.tyc = tyc;
		this.fechaTerminacion = fechaTerminacion;
		this.patrocinadores = patrocinadores;
		this.usuariosParticipantes = usuariosParticipantes;
		this.usuariosGanadores = usuariosGanadores;
		this.habilitado = habilitado;
		this.ganadores = ganadores;
		this.mensajeParticipacion = mensajeParticipacion;
		this.mensajeGanador = mensajeGanador;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<String> getPremios() {
		return premios;
	}

	public void setPremios(List<String> premios) {
		this.premios = premios;
	}

	public String getTyc() {
		return tyc;
	}

	public void setTyc(String tyc) {
		this.tyc = tyc;
	}

	public Date getFechaTerminacion() {
		return fechaTerminacion;
	}

	public void setFechaTerminacion(Date fechaTerminacion) {
		this.fechaTerminacion = fechaTerminacion;
	}

	public List<String> getPatrocinadores() {
		return patrocinadores;
	}

	public void setPatrocinadores(List<String> patrocinadores) {
		this.patrocinadores = patrocinadores;
	}

	public List<String> getUsuariosParticipantes() {
		return usuariosParticipantes;
	}

	public void setUsuariosParticipantes(List<String> usuariosParticipantes) {
		this.usuariosParticipantes = usuariosParticipantes;
	}

	public List<String> getUsuariosGanadores() {
		return usuariosGanadores;
	}

	public void setUsuariosGanadores(List<String> usuariosGanadores) {
		this.usuariosGanadores = usuariosGanadores;
	}

	public Boolean getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}

	public Integer getGanadores() {
		return ganadores;
	}

	public void setGanadores(Integer ganadores) {
		this.ganadores = ganadores;
	}

	public String getMensajeParticipacion() {
		return mensajeParticipacion;
	}

	public void setMensajeParticipacion(String mensajeParticipacion) {
		this.mensajeParticipacion = mensajeParticipacion;
	}

	public String getMensajeGanador() {
		return mensajeGanador;
	}

	public void setMensajeGanador(String mensajeGanador) {
		this.mensajeGanador = mensajeGanador;
	}

}
