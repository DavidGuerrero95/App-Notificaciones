package com.app.notificaciones.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.notificaciones.models.Notificaciones;
import com.app.notificaciones.repository.NotificacionesRepository;
import com.app.notificaciones.responses.Mensajes;

@Service
public class NotificacionesServices implements INotificacionesServices {

	@Autowired
	NotificacionesRepository nRepository;

	@Autowired
	IEmailSenderService eService;

	@Override
	public void crearNotificacion(String username, String email) {
		Notificaciones n = new Notificaciones();
		n.setUsername(username);
		n.setEmail(email);
		n.setMensajes(new ArrayList<List<String>>());
		n.setActivar(false);
		nRepository.save(n);

	}

	@Override
	public boolean editarNotificaciones(String username, String newUsername, String email) {
		if (nRepository.existsByUsername(username)) {
			Notificaciones n = nRepository.findByUsername(username);
			if (newUsername != null)
				n.setUsername(newUsername);
			if (email != null)
				n.setUsername(email);
			nRepository.save(n);
			return true;
		}
		return false;
	}

	@Override
	public void eliminarNotificacion(String nombre) {
		nRepository.deleteByUsername(nombre);
	}

	@Override
	public void enviarCodigoEditarUsuario(String username, Integer codigo) {
		Notificaciones n = nRepository.findByUsername(username);
		Mensajes mensaje = new Mensajes();
		String texto = "El codigo de verificacion de para la edición de tu perfil en la City SuperApp es: " + codigo;
		mensaje.setName("Edición de perfil: " + username);
		mensaje.setMensaje(texto);
		mensaje.setEmail(n.getEmail());
		eService.enviarMensaje(mensaje);
	}

	@Override
	public void borrarNotificacion(String username) {
		Notificaciones noti = nRepository.findByUsername(username);
		noti.setMensajes(new ArrayList<List<String>>());
		noti.setActivar(false);
		nRepository.save(noti);
	}

	@Override
	public void cambiarNotificacion(String username) {
		Notificaciones noti = nRepository.findByUsername(username);
		noti.setActivar(false);
		nRepository.save(noti);
	}

	@Override
	public Boolean revisarNotificacion(String username) {
		Notificaciones noti = nRepository.findByUsername(username);
		return noti.getActivar();
	}

	@Override
	public List<List<String>> verNotificaciones(String username) {
		Notificaciones noti = nRepository.findByUsername(username);
		return noti.getMensajes();
	}
}
