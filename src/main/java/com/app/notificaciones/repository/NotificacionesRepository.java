package com.app.notificaciones.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.app.notificaciones.models.Notificaciones;

public interface NotificacionesRepository extends MongoRepository<Notificaciones, String> {

	@RestResource(path = "buscar-name")
	public Notificaciones findByUsername(@Param("username") String username);

	@RestResource(path = "exists-name")
	public Boolean existsByUsername(@Param("username") String username);

	@RestResource(path = "delete-name")
	public void deleteByUsername(@Param("username") String nombre);

}