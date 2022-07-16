package com.app.notificaciones.requests;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProyectosGamificacion {

	@Id
	@JsonIgnore
	private String id;

	@NotNull(message = "id proyecto cannot be null")
	@Indexed(unique = true)
	private Integer idProyecto;

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
	private String mensajeBienvenida;

}
