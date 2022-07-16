package com.app.notificaciones.services;

import com.app.notificaciones.responses.Mensajes;

public interface IEmailSenderService {

	public void sendSimpleEmail(String toEmail, String body, String subject);

	public void enviarMensaje(Mensajes mensaje);
	
	Object sendMail();
	
}
