package com.app.notificaciones.services;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.app.notificaciones.responses.Mensajes;

@Service
public class EmailSenderService implements IEmailSenderService {

	private static final String NOREPLY_ADDRESS = "appcity@udea.edu.co";

	@Autowired
	private JavaMailSender emailSender;

	@Override
	public void enviarMensaje(Mensajes mensaje) {
		sendSimpleEmail(mensaje.getEmail(), mensaje.getMensaje(), mensaje.getName());
	}

	public void sendSimpleEmail(String toEmail, String body, String subject) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(NOREPLY_ADDRESS);
			message.setTo(toEmail);
			message.setSubject(subject);
			message.setText(body);
			emailSender.send(message);
			System.out.println("Mail Send ........");
		} catch (MailException exception) {
			exception.printStackTrace();
			System.out.println(exception.getMessage());
		}
	}

	@Override
	public Object sendMail() {
		// Set required configs
		String from = NOREPLY_ADDRESS;
		String to = "guerrer16.395@gmail.com";
		String host = "172.19.0.101";
		String port = "25";
		//String user = "from_mail@gmail.com";
		//String password = "from_mail_password";

		// Set system properties
		Properties properties = System.getProperties();
		properties.put("mail.smtp.auth", "false");
		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.port", port);
		//properties.setProperty("mail.smtp.user", user);
		//properties.setProperty("mail.smtp.password", password);
		properties.setProperty("mail.smtp.starttls.enable", "false");

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			// Set from email address
			message.setFrom(new InternetAddress(from, "UdeA"));
			// Set the recipient email address
			message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
			// Set email subject
			message.setSubject("Mail Subject");
			// Set email body
			message.setText("This is message body");
			// Set configs for sending email
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from);
			// Send email
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			System.out.println("done");
			return "Email Sent! Check Inbox!";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (javax.mail.MessagingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
