package com.ServicioDeSoporte.Servicio_De_Soporte.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ServicioDeSoporte.Servicio_De_Soporte.DTOAS.SoporteDTOAS;
import com.ServicioDeSoporte.Servicio_De_Soporte.DTOAS.SoporteModelAssembler;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.EstadoActualizacionDTO;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.FiltroEstadoDTO;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.FiltroTipoProblemaDTO;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.SolicitudCreacionDTO;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.Soporte;
import com.ServicioDeSoporte.Servicio_De_Soporte.service.SoporteService;


@RestController
@RequestMapping("/soporte")
public class SoporteController {

    @Autowired
    private SoporteService soporteService;

    
    @Autowired
    private SoporteModelAssembler assembler; 
   
    @PostMapping("/crearsolicitud") 
    public ResponseEntity<Soporte> crearSolicitud(@RequestBody SolicitudCreacionDTO solicitudDto) {
    Soporte nuevaSolicitud = soporteService.crearSolicitud(
        solicitudDto.getUsuarioId(),
        solicitudDto.getDescripcion(),
        solicitudDto.getTipoProblema()); 
    return new ResponseEntity<>(nuevaSolicitud, HttpStatus.CREATED);
    }
    
    @PutMapping("/estado") 
    public ResponseEntity<Soporte> actualizarEstado(
        @RequestBody EstadoActualizacionDTO estadoDto) {
        Soporte soporteActualizado = soporteService.actualizarEstado(estadoDto.getSoporteId(), estadoDto.getNuevoEstado());
    return ResponseEntity.ok(soporteActualizado);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Soporte>> obtenerSolicitudesPorUsuario(@PathVariable int usuarioId) {
        List<Soporte> solicitudes = soporteService.obtenerSolicitudesPorUsuario(usuarioId);
        if (solicitudes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
    return ResponseEntity.ok(solicitudes);
    }

     @GetMapping("/solicitud/{id}")
    public ResponseEntity<SoporteDTOAS> obtenerSolicitudPorId(@PathVariable Integer id) {
        Soporte soporte = soporteService.obtenerSolicitudPorId(id);
        if (soporte == null) {
            return ResponseEntity.notFound().build();}
        SoporteDTOAS soporteDTOAS = assembler.toModel(soporte); 
        return ResponseEntity.ok(soporteDTOAS); 
    }

    @GetMapping("/todas")
    public ResponseEntity<List<Soporte>> obtenerTodasLasSolicitudes() { 
        List<Soporte> solicitudes = soporteService.obtenerTodasLasSolicitudes();
        if (solicitudes.isEmpty()) {
            return ResponseEntity.noContent().build(); 
        }
    return ResponseEntity.ok(solicitudes);
    }
    
    @PostMapping("/filtrarportipo")
    public ResponseEntity<List<Soporte>> filtrarPorTipoProblema(@RequestBody FiltroTipoProblemaDTO filtroDto) {
    List<Soporte> solicitudes = soporteService.filtrarPorTipoProblema(filtroDto.getTipoProblema());
        if (solicitudes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
    return ResponseEntity.ok(solicitudes);
    }

    
    @PostMapping("/filtrarporestado")
    public ResponseEntity<List<Soporte>> filtrarPorEstado(@RequestBody FiltroEstadoDTO filtroDto) {
    List<Soporte> solicitudes = soporteService.filtrarPorEstado(filtroDto.getEstado());
        if (solicitudes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
    return ResponseEntity.ok(solicitudes);
    }


}