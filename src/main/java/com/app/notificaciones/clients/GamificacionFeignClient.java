package com.app.notificaciones.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.app.notificaciones.requests.ProyectosGamificacion;

@FeignClient(name = "app-gamification")
public interface GamificacionFeignClient {

	@GetMapping("/gamificacion/proyectos/ver/{idProyecto}")
	public ProyectosGamificacion verGamificacionProyectos(@PathVariable("idProyecto") Integer idProyecto);

	@GetMapping("/gamificacion/proyectos/mensaje/participacion/{idProyecto}")
	public String verMensajeParticipacionGamificacionProyectos(@PathVariable("idProyecto") Integer idProyecto);

}
