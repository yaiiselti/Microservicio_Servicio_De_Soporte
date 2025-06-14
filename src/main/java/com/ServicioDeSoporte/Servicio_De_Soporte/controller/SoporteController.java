package com.ServicioDeSoporte.Servicio_De_Soporte.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ServicioDeSoporte.Servicio_De_Soporte.model.EstadoSoporte;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.Soporte;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.tipo_problema;
import com.ServicioDeSoporte.Servicio_De_Soporte.service.SoporteService;

@RestController
@RequestMapping("/soporte")
public class SoporteController {

    @Autowired
    private SoporteService soporteService;

    @PostMapping("/crear")
    public Soporte crearSolicitud(@RequestParam int usuarioId,
                                  @RequestParam String descripcion,
                                  @RequestParam tipo_problema tipoProblema) {
        return soporteService.crearSolicitud(usuarioId, descripcion, tipoProblema);
    }

    @PutMapping("/actualizar_estado")
    public Soporte actualizarEstado(@RequestParam int soporteId,
                                @RequestParam EstadoSoporte nuevoEstado) {
    return soporteService.actualizarEstado(soporteId, nuevoEstado);
    }


    @GetMapping("/usuario/{usuarioId}")
    public List<Soporte> obtenerSolicitudesPorUsuario(@PathVariable int usuarioId) {
        return soporteService.obtenerSolicitudesPorUsuario(usuarioId);
    }

    @GetMapping("/todas")
    public List<Soporte> obtenerTodasLasSolicitudes(@RequestParam int usuarioId) {
        return soporteService.obtenerTodasLasSolicitudes(usuarioId);
    }

    @GetMapping("/filtrarportipo")
    public List<Soporte> filtrarPorTipoProblema(@RequestParam tipo_problema tipoProblema) {
        return soporteService.filtrarPorTipoProblema(tipoProblema);
    }

    @GetMapping("/filtrarporestado")
    public List<Soporte> filtrarPorEstado(@RequestParam EstadoSoporte estado) {
    return soporteService.filtrarPorEstado(estado);
}
}