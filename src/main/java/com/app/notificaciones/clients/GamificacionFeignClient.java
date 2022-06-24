package com.app.notificaciones.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.app.notificaciones.models.ProyectosGamificacion;

@FeignClient(name = "app-gamification")
public interface GamificacionFeignClient {

	@GetMapping("/gamificacion/proyectos/ver/{nombre}")
	public ProyectosGamificacion verGamificacionProyectos(@PathVariable("nombre") String nombre);

}
