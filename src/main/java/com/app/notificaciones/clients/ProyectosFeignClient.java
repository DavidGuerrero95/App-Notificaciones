package com.app.notificaciones.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.app.notificaciones.requests.Proyectos;

@FeignClient(name = "app-proyectos")
public interface ProyectosFeignClient {

	@GetMapping("/proyectos/ver/proyecto/{idProyecto}")
	public Proyectos verProyecto(@PathVariable("idProyecto") Integer idProyecto);

}
