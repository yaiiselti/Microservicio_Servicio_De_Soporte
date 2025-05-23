package com.ServicioDeSoporte.Servicio_De_Soporte.service;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ServicioDeSoporte.Servicio_De_Soporte.model.EstadoSoporte;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.Soporte;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.UsuarioDTO;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.tipo_problema;
import com.ServicioDeSoporte.Servicio_De_Soporte.repository.SoporteRepository;

@Service
public class SoporteService {

    @Autowired
    private SoporteRepository soporteRepository;

    @Autowired
    private UsuarioClient usuarioClient;

    public Soporte crearSolicitud(int usuarioId, String descripcion, tipo_problema tipoProblema) {
        UsuarioDTO usuario = usuarioClient.getUsuarioById(usuarioId);
        if (usuario == null) throw new RuntimeException("Usuario no encontrado");
        Soporte soporte = new Soporte();
        soporte.setUsuarioId(usuarioId);
        soporte.setDescripcion(descripcion);
        soporte.setTipoProblema(tipoProblema);
        soporte.setEstado(EstadoSoporte.ABIERTO); // Usar el enum
        soporte.setFechaCreacionSolicitud(LocalDateTime.now());
        return soporteRepository.save(soporte);
    }

    public Soporte actualizarEstado(int soporteId, EstadoSoporte nuevoEstado, int usuarioId) {
        UsuarioDTO usuario = usuarioClient.getUsuarioById(usuarioId);
        if (!"SOPORTE_TECNICO".equals(usuario.getTipo())) {
            throw new RuntimeException("No autorizado para modificar el estado");
        }
        Soporte soporte = soporteRepository.findById(soporteId).orElseThrow();
        soporte.setEstado(nuevoEstado);
        if (nuevoEstado == EstadoSoporte.RESUELTO || nuevoEstado == EstadoSoporte.CERRADO) {
            soporte.setFechaResolucion(LocalDateTime.now());
        } else {
            soporte.setFechaResolucion(null);
        }
        return soporteRepository.save(soporte);
    }

    public List<Soporte> obtenerSolicitudesPorUsuario(int usuarioId) {
        return soporteRepository.findByUsuarioId(usuarioId);
    }

    public List<Soporte> obtenerTodasLasSolicitudes(int usuarioId) {
        UsuarioDTO usuario = usuarioClient.getUsuarioById(usuarioId);
        if (!"SOPORTE_TECNICO".equals(usuario.getTipo())) {
            throw new RuntimeException("No autorizado para ver todas las solicitudes");
        }
        return soporteRepository.findAll();
    }

    public List<Soporte> filtrarPorTipoProblema(tipo_problema tipoProblema, int usuarioId) {
        UsuarioDTO usuario = usuarioClient.getUsuarioById(usuarioId);
        if (!"SOPORTE_TECNICO".equals(usuario.getTipo())) {
            throw new RuntimeException("No autorizado para filtrar solicitudes");
        }
        return soporteRepository.findByTipoProblema(tipoProblema);
    }

    public List<Soporte> filtrarPorEstado(EstadoSoporte estado, int usuarioId) {
        UsuarioDTO usuario = usuarioClient.getUsuarioById(usuarioId);
        if (!"SOPORTE_TECNICO".equals(usuario.getTipo())) {
            throw new RuntimeException("No autorizado para filtrar solicitudes");
        }
        return soporteRepository.findByEstado(estado);
    }
}