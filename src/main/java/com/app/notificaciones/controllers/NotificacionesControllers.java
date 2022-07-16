package com.app.notificaciones.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.app.notificaciones.clients.GamificacionFeignClient;
import com.app.notificaciones.clients.ProyectosFeignClient;
import com.app.notificaciones.clients.SuscripcionesFeignClient;
import com.app.notificaciones.models.Notificaciones;
import com.app.notificaciones.repository.NotificacionesRepository;
import com.app.notificaciones.requests.Proyectos;
import com.app.notificaciones.responses.Mensajes;
import com.app.notificaciones.services.IEmailSenderService;
import com.app.notificaciones.services.INotificacionesServices;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class NotificacionesControllers {

	@SuppressWarnings("rawtypes")
	@Autowired
	private CircuitBreakerFactory cbFactory;

	@Autowired
	NotificacionesRepository nRepository;

	@Autowired
	ProyectosFeignClient pClient;

	@Autowired
	GamificacionFeignClient gClient;

	@Autowired
	SuscripcionesFeignClient sClient;

	@Autowired
	INotificacionesServices nServices;

	@Autowired
	IEmailSenderService eService;

//  ****************************	NOTIFICACIONES	***********************************  //

	// ************ USUARIOS ************ //

	// MICROSERVICIO USUARIOS -> CREAR
	@PostMapping("/notificaciones/crear/")
	public Boolean crearNotificaciones(@RequestParam("username") String username, @RequestParam("email") String email)
			throws IOException {
		try {
			nServices.crearNotificacion(username, email);
			return true;
		} catch (Exception e) {
			throw new IOException("Error la creacion, notificaciones: " + e.getMessage());
		}
	}

	// MICROSERVICIO USUARIOS -> EDITAR
	@PutMapping("/notificaciones/usuario/editar/{username}")
	public Boolean editUser(@PathVariable("username") String username, @RequestParam("newUsername") String newUsername,
			@RequestParam("email") String email) throws IOException {
		try {
			if (nServices.editarNotificaciones(username, newUsername, email)) {
			}
			return true;
		} catch (Exception e) {
			throw new IOException("Error la edicion, notificaciones: " + e.getMessage());
		}
	}

	// ELIMINAR NOTIFICACIONES
	@PutMapping("/notificaciones/borrarNotificacion/{username}")
	@ResponseStatus(code = HttpStatus.OK)
	public void borrarNotificacion(@PathVariable String username) {
		nServices.borrarNotificacion(username);
	}

	// CAMBIAR ESTADO NOTIFICACION
	@PutMapping("/notificaciones/cambiarNotificacion/{username}")
	@ResponseStatus(code = HttpStatus.OK)
	public void cambiarNotificacion(@PathVariable String username) {
		nServices.cambiarNotificacion(username);
	}

	// VER NOTIFICACIONES
	@GetMapping("/notificaciones/revisar/{username}")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean revisarNotificacion(@PathVariable String username) {
		return nServices.revisarNotificacion(username);
	}

	// MICROSERVICIO USUARIOS -> ENVIAR CODIGO
	@GetMapping("/notificaciones/usuario/enviar/{username}")
	public void enviarCodigoEditUsuario(@PathVariable("username") String username,
			@RequestParam(value = "codigo") Integer codigo) throws IOException {
		nServices.enviarCodigoEditarUsuario(username, codigo);
	}

	// VER NOTIFICACIONES
	@GetMapping("/notificaciones/verNotificaciones/{username}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<List<String>> verNotificaciones(@PathVariable String username) {
		return nServices.verNotificaciones(username);
	}

	// MICROSERVICIO USUARIOS -> ELIMINAR TODOS
	@DeleteMapping("/notificacioneso/eliminar/all/usuarios/")
	public Boolean eliminarAllUsuario() throws IOException {
		try {
			nRepository.deleteAll();
			return true;
		} catch (Exception e) {
			throw new IOException("Error: " + e.getMessage());
		}
	}

	// MICROSERVICIO USUARIOS -> ELIMINAR
	@DeleteMapping("/notificaciones/eliminar/")
	public Boolean eliminarNotificacion(@RequestParam String nombre) throws IOException {
		try {
			nServices.eliminarNotificacion(nombre);
			return true;
		} catch (Exception e) {
			throw new IOException("Error la eliminacion, notificaciones: " + e.getMessage());
		}
	}

	// ************ REGISTRO ************ //

	// MICROSERVICIO REGISTRO -> ENVIAR CODIGO
	@PostMapping("/notificaciones/registro/")
	public void enviarMensajeRegistro(@RequestParam("email") String email, @RequestParam("codigo") String codigo) {
		Mensajes mensaje = new Mensajes();
		String texto = "El codigo de verificacion de la cuenta de City SuperApp es: " + codigo;
		mensaje.setName("Registro: City SuperApp");
		mensaje.setMensaje(texto);
		mensaje.setEmail(email);
		eService.enviarMensaje(mensaje);
	}

	// ************ PROYECTOS ************ //

	// MICROSERVICIO PROYECTOS -> ENVIAR CODIGO - CAMBIAR ENABLED
	@PutMapping("/notificaciones/proyecto/edit/enabled/")
	public void enviarMensajeEnabled(@RequestParam("idProyecto") Integer idProyecto,
			@RequestParam("enabled") Boolean enabled, @RequestParam("nombre") String nombre) {
		List<String> notificacion = new ArrayList<>();
		List<List<String>> totalNotificacions = new ArrayList<List<String>>();
		Calendar c = Calendar.getInstance();
		String dia = Integer.toString(c.get(Calendar.DATE));
		String mes = Integer.toString(c.get(Calendar.MONTH));
		String annio = Integer.toString(c.get(Calendar.YEAR));
		String fecha = dia + "/" + mes + "/" + annio;
		Integer hora = c.get(Calendar.HOUR_OF_DAY);
		Integer minutos = c.get(Calendar.MINUTE);
		String tiempo = hora + ":" + minutos;
		Mensajes correo = new Mensajes();
		String estado;
		if (enabled) {
			estado = "Habilitado";
		} else {
			estado = "DesHabilitado";
		}
		String mensaje = "Cambio la disponibilidad del proyecto: " + nombre + ". A: " + estado
				+ " Puedes Revisar sus Estadisticas en la app: City SuperApp.";
		notificacion.add(nombre);
		notificacion.add(mensaje);
		notificacion.add(fecha);
		notificacion.add(tiempo);
		correo.setMensaje(mensaje);
		List<String> suscritos = cbFactory.create("notificaciones").run(
				() -> sClient.obtenerListaSuscripciones(idProyecto), e -> encontrarListaSuscripciones(idProyecto, e));
		if (!suscritos.isEmpty()) {
			for (int i = 0; i < suscritos.size(); i++) {
				Notificaciones noti = nRepository.findByUsername(suscritos.get(i));
				totalNotificacions = noti.getMensajes();
				totalNotificacions.add(notificacion);
				noti.setMensajes(totalNotificacions);
				noti.setActivar(true);
				nRepository.save(noti);
				correo.setName(suscritos.get(i));
				correo.setEmail(noti.getEmail());
				eService.enviarMensaje(correo);
			}
		}
	}

	// MICROSERVICIO PROYECTOS -> ENVIAR CODIGO - CAMBIAR ESTADO
	@PutMapping("/notificaciones/proyecto/edit/estado/")
	@ResponseStatus(code = HttpStatus.OK)
	public void enviarMensajeEstado(@RequestParam("idProyecto") Integer idProyecto,
			@RequestParam("estado") Integer estado, @RequestParam("nombre") String nombre) {
		List<String> notificacion = new ArrayList<>();
		List<List<String>> totalNotificacions = new ArrayList<List<String>>();
		Calendar c = Calendar.getInstance();
		String dia = Integer.toString(c.get(Calendar.DATE));
		String mes = Integer.toString(c.get(Calendar.MONTH));
		String annio = Integer.toString(c.get(Calendar.YEAR));
		String fecha = dia + "/" + mes + "/" + annio;
		Integer hora = c.get(Calendar.HOUR_OF_DAY);
		Integer minutos = c.get(Calendar.MINUTE);
		String tiempo = hora + ":" + minutos;
		Mensajes correo = new Mensajes();
		String mensajeCom;
		if (estado == 1) {
			mensajeCom = "Produccion";
		} else if (estado == 2) {
			mensajeCom = "Desarrollo";
		} else if (estado == 3) {
			mensajeCom = "Implementacion";
		} else {
			mensajeCom = "Finalizo";
		}
		String mensaje = "Cambio el estado del proyecto: " + nombre + ". A: " + mensajeCom
				+ " Puedes Revisar sus Estadisticas en la app: City SuperApp.";
		List<String> suscritos = cbFactory.create("notificaciones").run(
				() -> sClient.obtenerListaSuscripciones(idProyecto), e -> encontrarListaSuscripciones(idProyecto, e));
		notificacion.add(nombre);
		notificacion.add(mensaje);
		notificacion.add(fecha);
		notificacion.add(tiempo);
		correo.setMensaje(mensaje);
		if (!suscritos.isEmpty()) {
			for (int i = 0; i < suscritos.size(); i++) {
				Notificaciones noti = nRepository.findByUsername(suscritos.get(i));
				totalNotificacions = noti.getMensajes();
				totalNotificacions.add(notificacion);
				noti.setMensajes(totalNotificacions);
				noti.setActivar(true);
				nRepository.save(noti);
				correo.setName(suscritos.get(i));
				correo.setEmail(noti.getEmail());
				eService.enviarMensaje(correo);
			}
		}
	}

//  ****************************	SUSCRIPCIONES	***********************************  //

	// MICROSERVICIO SUSCRIPCIONES -> ENVIAR MENSAJE SUSCRIPCION
	@PutMapping("/notificaciones/suscripciones/")
	@ResponseStatus(code = HttpStatus.OK)
	public void enviarMensajeSuscripciones(@RequestParam String nombre, @RequestParam String username) {
		Notificaciones noti = new Notificaciones();
		List<String> notificacion = new ArrayList<>();
		List<List<String>> totalNotificacions = new ArrayList<List<String>>();
		Mensajes correo = new Mensajes();
		String mensaje = "Suscrito correctamente al proyecto: " + nombre + ".";

		Calendar c = Calendar.getInstance();
		String dia = Integer.toString(c.get(Calendar.DATE));
		String mes = Integer.toString(c.get(Calendar.MONTH));
		String annio = Integer.toString(c.get(Calendar.YEAR));
		String fecha = dia + "/" + mes + "/" + annio;
		Integer hora = c.get(Calendar.HOUR_OF_DAY);
		Integer minutos = c.get(Calendar.MINUTE);
		String tiempo = hora + ":" + minutos;

		notificacion.add(nombre);
		notificacion.add(mensaje);
		notificacion.add(fecha);
		notificacion.add(tiempo);
		correo.setMensaje(mensaje);

		noti = nRepository.findByUsername(username);

		totalNotificacions = noti.getMensajes();
		totalNotificacions.add(notificacion);
		noti.setMensajes(totalNotificacions);
		noti.setActivar(true);
		nRepository.save(noti);
		correo.setName(username);
		correo.setEmail(noti.getEmail());
		eService.enviarMensaje(correo);
	}

	// MICROSERVICIO SUSCRIPCIONES -> ENVIAR MENSAJE CONTESTAR FORMULARIO
	@PutMapping("/notificaciones/inscripciones/")
	public void enviarMensajeInscripciones(@RequestParam("idProyecto") Integer idProyecto, @RequestParam String nombre,
			@RequestParam String username) {
		Notificaciones noti = new Notificaciones();
		List<String> notificacion = new ArrayList<>();
		List<List<String>> totalNotificacions = new ArrayList<List<String>>();
		Calendar c = Calendar.getInstance();

		String dia = Integer.toString(c.get(Calendar.DATE));
		String mes = Integer.toString(c.get(Calendar.MONTH));
		String annio = Integer.toString(c.get(Calendar.YEAR));
		String fecha = dia + "/" + mes + "/" + annio;

		Integer hora = c.get(Calendar.HOUR_OF_DAY);
		Integer minutos = c.get(Calendar.MINUTE);
		String tiempo = hora + ":" + minutos;
		Mensajes correo = new Mensajes();

		Proyectos p = cbFactory.create("notificaciones").run(() -> pClient.verProyecto(idProyecto),
				e -> encontrarProyecto(idProyecto, e));
		String mensaje = p.getMensajeParticipacion();

		if (p.getGamificacion())
			mensaje += "\n" + gClient.verMensajeParticipacionGamificacionProyectos(idProyecto);

		notificacion.add(nombre);
		notificacion.add(mensaje);
		notificacion.add(fecha);
		notificacion.add(tiempo);
		correo.setMensaje(mensaje);

		noti = nRepository.findByUsername(username);
		totalNotificacions = noti.getMensajes();
		totalNotificacions.add(notificacion);
		noti.setMensajes(totalNotificacions);
		noti.setActivar(true);
		nRepository.save(noti);
		correo.setName(username);
		correo.setEmail(noti.getEmail());
		eService.enviarMensaje(correo);
	}

//  ****************************	GAMIFICACIONES	***********************************  //

	// MICROSERVICIO GAMIFICACION -> ENVIAR MENSAJE GANADOR
	@PutMapping("/notificaciones/gamificacion/proyecto/notificacion-ganadores/")
	public void enviarNotificacionGanador(@RequestParam("nombre") List<String> nombre,
			@RequestParam("mensajeGanador") String mensajeGanador) {
		List<String> notificacion = new ArrayList<>();
		Mensajes correo = new Mensajes();

		List<String> lU = nombre;
		String mensaje = mensajeGanador;

		Calendar c = Calendar.getInstance();
		String dia = Integer.toString(c.get(Calendar.DATE));
		String mes = Integer.toString(c.get(Calendar.MONTH));
		String annio = Integer.toString(c.get(Calendar.YEAR));
		String fecha = dia + "/" + mes + "/" + annio;
		Integer hora = c.get(Calendar.HOUR_OF_DAY);
		Integer minutos = c.get(Calendar.MINUTE);
		String tiempo = hora + ":" + minutos;

		notificacion.add(mensaje);
		notificacion.add(fecha);
		notificacion.add(tiempo);
		correo.setMensaje(mensaje);

		lU.forEach(x -> {
			notificacion.add(x);
			Notificaciones noti = nRepository.findByUsername(x);
			List<List<String>> totalNotificacions = noti.getMensajes();
			totalNotificacions.add(notificacion);
			noti.setMensajes(totalNotificacions);
			noti.setActivar(true);
			nRepository.save(noti);
			correo.setName(x);
			correo.setEmail(noti.getEmail());
			eService.enviarMensaje(correo);
		});
	}

//  ****************************	PRUEBA	***********************************  //
	@PutMapping("/notificaciones/enviar/mensaje/prueba/")
	@ResponseStatus(code = HttpStatus.OK)
	public Object enviarMensajePrueba(@RequestParam("subject") String subject, @RequestParam("email") String email,
			@RequestParam("body") String body) {
		eService.sendSimpleEmail(email, body, subject);
		return eService.sendMail();
	}

//  ****************************	FUNCIONES TOLERANCIA A FALLOS	***********************************  //

	private List<String> encontrarListaSuscripciones(Integer idProyecto, Throwable e) {
		log.info(e.getMessage());
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Servicio de suscripciones no esta disponible");
	}

	private Proyectos encontrarProyecto(Integer idProyecto, Throwable e) {
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Servicio de proyectos no esta disponible");
	}
}
