package com.app.notificaciones.services;

import java.util.List;

public interface INotificacionesServices {

	void crearNotificacion(String username, String email);

	boolean editarNotificaciones(String username, String newUsername, String email);

	void eliminarNotificacion(String nombre);

	void enviarCodigoEditarUsuario(String username, Integer codigo);

	void borrarNotificacion(String username);

	void cambiarNotificacion(String username);

	Boolean revisarNotificacion(String username);

	List<List<String>> verNotificaciones(String username);

}
