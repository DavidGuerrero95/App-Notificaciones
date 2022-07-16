package com.app.notificaciones.models;

import java.util.List;

import javax.validation.constraints.Email;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "notificaciones")
@Data
@NoArgsConstructor
public class Notificaciones {

	@Id
	@JsonIgnore
	private String id;

	@Indexed(unique = true)
	private String username;

	@Indexed(unique = true)
	@Email
	private String email;

	private List<List<String>> mensajes;
	private Boolean activar;

}
