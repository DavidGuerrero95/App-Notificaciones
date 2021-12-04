package com.app.notificaciones.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.app.notificaciones.request.Mensajes;

@Service
public class EmailSenderService implements IEmailSenderService {

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void enviarMensaje(Mensajes mensaje) {
		sendSimpleEmail(mensaje.getEmail(), mensaje.getMensaje(), mensaje.getName());

	}

	public void sendSimpleEmail(String toEmail, String body, String subject) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("coo.appcity@gmail.com");
		message.setTo(toEmail);
		message.setText(body);
		message.setSubject(subject);
		mailSender.send(message);
		System.out.println("Mail Send ........");
	}

}
