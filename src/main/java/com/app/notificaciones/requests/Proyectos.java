package com.app.notificaciones.requests;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Proyectos {

	@Id
	@JsonIgnore
	private String id;

	@NotNull(message = "Name cannot be null")
	@Size(max = 200)
	@Indexed(unique = true)
	private String nombre;

	@Indexed(unique = true)
	private Integer codigoProyecto;

	@NotEmpty(message = "palabras clave cannot be empty")
	private List<String> palabrasClave;

	@NotEmpty(message = "ubicacion cannot be null")
	private List<Double> ubicacion;

	@NotBlank(message = "resumen cannot be null")
	private String resumen;

	@NotBlank(message = "objetivos cannot be null")
	private List<String> objetivos;

	@NotBlank(message = "descripcion cannot be null")
	private String descripcion;

	@NotBlank(message = "principales itos cannot be null")
	private List<String> hitos;

	@NotNull(message = "presupuesto itos cannot be null")
	private Long presupuesto;

	private List<String> cronograma;

	@NotNull(message = "enabled cannot be null")
	private Boolean activo;

	private Integer estadoProyecto;
	private List<Integer> proyectoDesarrollo;
	private Integer muro;
	private List<String> creador;

	@NotNull
	private Date fechaLanzamiento;
	private Boolean gamificacion;
	private String mensajeParticipacion;

	public Proyectos(String nombre, Integer codigoProyecto, List<String> palabrasClave, List<Double> ubicacion,
			String resumen, List<String> objetivos, String descripcion, List<String> hitos, Long presupuesto,
			List<String> cronograma, Boolean activo, Integer estadoProyecto, List<Integer> proyectoDesarrollo,
			Integer muro, List<String> creador, Date fechaLanzamiento, Boolean gamificacion,
			String mensajeParticipacion) {
		super();
		this.nombre = nombre;
		this.codigoProyecto = codigoProyecto;
		this.palabrasClave = palabrasClave;
		this.ubicacion = ubicacion;
		this.resumen = resumen;
		this.objetivos = objetivos;
		this.descripcion = descripcion;
		this.hitos = hitos;
		this.presupuesto = presupuesto;
		this.cronograma = cronograma;
		this.activo = activo;
		this.estadoProyecto = estadoProyecto;
		this.proyectoDesarrollo = proyectoDesarrollo;
		this.muro = muro;
		this.creador = creador;
		this.fechaLanzamiento = fechaLanzamiento;
		this.gamificacion = gamificacion;
		this.mensajeParticipacion = mensajeParticipacion;
	}
}