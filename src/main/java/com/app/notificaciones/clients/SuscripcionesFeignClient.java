package com.app.notificaciones.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "app-subscripciones")
public interface SuscripcionesFeignClient {

	@GetMapping("/suscripciones/obtener/nombre/lista/suscritos/{idProyecto}")
	public List<String> obtenerListaSuscripciones(@PathVariable("idProyecto") Integer idProyecto);

}
