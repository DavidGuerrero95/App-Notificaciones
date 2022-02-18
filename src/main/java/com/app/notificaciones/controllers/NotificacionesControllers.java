package com.app.notificaciones.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.app.notificaciones.clients.SuscripcionesFeignClient;
import com.app.notificaciones.models.Notificaciones;
import com.app.notificaciones.models.Suscripciones;
import com.app.notificaciones.repository.NotificacionesRepository;
import com.app.notificaciones.repository.SuscripcionesRepository;
import com.app.notificaciones.request.Mensajes;
import com.app.notificaciones.services.IEmailSenderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
public class NotificacionesControllers {

	private final Logger logger = LoggerFactory.getLogger(NotificacionesControllers.class);

	@Autowired
	IEmailSenderService eService;

	@Autowired
	NotificacionesRepository nRepository;

	@Autowired
	SuscripcionesRepository sRepository;

	@Autowired
	SuscripcionesFeignClient sClient;

	@PostMapping("/notificaciones/crear/")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Boolean crearNotificaciones(@RequestParam("username") String username, @RequestParam("email") String email)
			throws IOException {
		try {
			Notificaciones n = new Notificaciones();
			n.setUsername(username);
			n.setEmail(email);
			n.setMensajes(new ArrayList<List<String>>());
			n.setActivar(false);
			nRepository.save(n);
			return true;
		} catch (Exception e) {
			throw new IOException("Error la creacion, notificaciones: " + e.getMessage());
		}
		
	}

	@PutMapping("/notificaciones/usuario/editar/{username}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Boolean editUser(@PathVariable("username") String username, @RequestParam("newUsername") String newUsername,
			@RequestParam("email") String email) throws IOException {
		try {
			Notificaciones n = nRepository.findByUsername(newUsername);
			if (!newUsername.isEmpty() && email.isEmpty())
				n.setUsername(newUsername);
			if (newUsername.isEmpty() && !email.isEmpty())
				n.setUsername(email);
			nRepository.save(n);
			return true;
		} catch (Exception e) {
			throw new IOException("Error la edicion, notificaciones: " + e.getMessage());
		}
		
	}

	@DeleteMapping("/notificaciones/eliminar/")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean eliminarNotificacion(@RequestParam String nombre) throws IOException {
		try {
			Notificaciones n = nRepository.findByUsername(nombre);
			nRepository.delete(n);
			return true;
		} catch (Exception e) {
			throw new IOException("Error la eliminacion, notificaciones: " + e.getMessage());
		}
		
	}

	@GetMapping("/notificaciones/usuario/enviar/{username}")
	@ResponseStatus(code = HttpStatus.OK)
	public void enviarCodigoEditUsuario(@PathVariable("username") String username,
			@RequestParam(value = "codigo") Integer codigo) throws IOException {
		Notificaciones n = nRepository.findByUsername(username);
		Mensajes mensaje = new Mensajes();
		String texto = "El codigo de verificacion de para la edición de tu perfil en la City SuperApp es: " + codigo;
		mensaje.setName("Edición de perfil: " + username);
		mensaje.setMensaje(texto);
		mensaje.setEmail(n.getEmail());
		eService.enviarMensaje(mensaje);
	}

	@PostMapping("/notificaciones/registro/")
	@ResponseStatus(code = HttpStatus.OK)
	public void enviarMensajeRegistro(@RequestParam("email") String email, @RequestParam("codigo") String codigo) {
		Mensajes mensaje = new Mensajes();
		String texto = "El codigo de verificacion de la cuenta de City SuperApp es: " + codigo;
		mensaje.setName("Registro: City SuperApp");
		mensaje.setMensaje(texto);
		mensaje.setEmail(email);
		eService.enviarMensaje(mensaje);
	}
	
	@PutMapping("/notificaciones/enviar/mensaje/prueba/")
	@ResponseStatus(code = HttpStatus.OK)
	public Object  enviarMensajePrueba(@RequestParam("subject") String subject, @RequestParam("email") String email, @RequestParam("body") String body) {
		eService.sendSimpleEmail(email,body,subject);
		return eService.sendMail();
	}

	@CircuitBreaker(name = "notificaciones", fallbackMethod = "obtenerListaSuscripciones")
	@PutMapping("/notificaciones/proyecto/edit/enabled/")
	@ResponseStatus(code = HttpStatus.OK)
	public void enviarMensajeEnabled(@RequestParam("nombre") String nombre, @RequestParam("enabled") Boolean enabled) {
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
		List<String> suscritos = sClient.obtenerListaSuscripciones(nombre);
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

	@PutMapping("/notificaciones/proyecto/edit/estado/")
	@ResponseStatus(code = HttpStatus.OK)
	public void enviarMensajeEstado(@RequestParam("nombre") String nombre, @RequestParam("estado") Integer estado) {
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
		List<String> suscritos = sClient.obtenerListaSuscripciones(nombre);
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

	@SuppressWarnings("unused")
	private List<String> obtenerListaSuscripciones(String username, Throwable e) {
		logger.info(e.getMessage());
		Suscripciones s = sRepository.findByNombre(username);
		return s.getSuscripciones();
	}

	@PostMapping("/notificaciones/suscripciones/crear/")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean crearSuscripciones(@RequestBody Suscripciones s) throws IOException {
		try {
			sRepository.save(s);
			return true;
		} catch (Exception e) {
			throw new IOException("Error la creacion, notificaciones: " + e.getMessage());
		}
		
	}

	@PutMapping("/notificaciones/suscripciones/editar/")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean editarSuscripciones(@RequestBody Suscripciones s) throws IOException {
		try {
			sRepository.save(s);
			return true;
		} catch (Exception e) {
			throw new IOException("Error la edicion, notificaciones: " + e.getMessage());
		}
		
	}

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

	@PostMapping("/notificaciones/inscripciones/")
	@ResponseStatus(code = HttpStatus.OK)
	public void enviarMensajeInscripciones(@RequestParam String nombre, @RequestParam String username) {
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
		String mensaje = "Gracias por participar en el proyecto: " + nombre
				+ ", sus aportes serán muy valiosos para el diseño y seguimiento del proyecto."
				+ " Puedes ver las estadística de participación en la City SuperApp."
				+ "\nDeseas adquirir información de la evolución del proyecto, inscríbete!";
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

	@GetMapping("/notificaciones/revisar/{username}")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean revisarNotificacion(@PathVariable String username) {
		Notificaciones noti = nRepository.findByUsername(username);
		return noti.getActivar();
	}

	@PutMapping("/notificaciones/cambiarNotificacion/{username}")
	@ResponseStatus(code = HttpStatus.OK)
	public void cambiarNotificacion(@PathVariable String username) {
		Notificaciones noti = nRepository.findByUsername(username);
		noti.setActivar(false);
		nRepository.save(noti);
	}

	@PutMapping("/notificaciones/borrarNotificacion/{username}")
	@ResponseStatus(code = HttpStatus.OK)
	public void borrarNotificacion(@PathVariable String username) {
		Notificaciones noti = nRepository.findByUsername(username);
		noti.setMensajes(new ArrayList<List<String>>());
		noti.setActivar(false);
		nRepository.save(noti);
	}

	@GetMapping("/notificaciones/verNotificaciones/{username}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<List<String>> verNotificaciones(@PathVariable String username) {
		Notificaciones noti = nRepository.findByUsername(username);
		return noti.getMensajes();
	}

}
