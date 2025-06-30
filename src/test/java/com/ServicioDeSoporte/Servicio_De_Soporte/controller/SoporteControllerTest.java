package com.ServicioDeSoporte.Servicio_De_Soporte.controller;


import com.ServicioDeSoporte.Servicio_De_Soporte.model.EstadoActualizacionDTO;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.FiltroEstadoDTO;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.FiltroTipoProblemaDTO;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.SolicitudCreacionDTO;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.Soporte;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.EstadoSoporte;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.tipo_problema;
import com.ServicioDeSoporte.Servicio_De_Soporte.service.SoporteService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SoporteController.class)
public class SoporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SoporteService soporteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Soporte createTestSoporte(int id, int usuarioId, String descripcion, tipo_problema tipoProblema, EstadoSoporte estado) {
        Soporte soporte = new Soporte();
        soporte.setId(id);
        soporte.setUsuarioId(usuarioId);
        soporte.setDescripcion(descripcion);
        soporte.setTipoProblema(tipoProblema);
        soporte.setEstado(estado);
        soporte.setFechaCreacionSolicitud(LocalDateTime.now());
        soporte.setFechaResolucion(LocalDateTime.now());
        return soporte;
    }

    @Test
    void testCrearSolicitud_Success() throws Exception {

        SolicitudCreacionDTO solicitudDto = new SolicitudCreacionDTO();
        solicitudDto.setUsuarioId(1);
        solicitudDto.setDescripcion("Problema con el login");
        solicitudDto.setTipoProblema(tipo_problema.ACCESO);

        Soporte nuevaSolicitud = createTestSoporte(1, 1, "Problema con el login", tipo_problema.ACCESO, EstadoSoporte.ABIERTO);

        when(soporteService.crearSolicitud(anyInt(), anyString(), any(tipo_problema.class))).thenReturn(nuevaSolicitud);


        mockMvc.perform(post("/soporte/crearsolicitud")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solicitudDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.descripcion").value("Problema con el login"))
                .andExpect(jsonPath("$.tipoProblema").value("ACCESO"))
                .andExpect(jsonPath("$.estado").value("ABIERTO"));
    }

    @Test
    void testActualizarEstado_Success() throws Exception {

        EstadoActualizacionDTO estadoDto = new EstadoActualizacionDTO();
        estadoDto.setSoporteId(1);
        estadoDto.setNuevoEstado(EstadoSoporte.RESUELTO);

        Soporte soporteActualizado = createTestSoporte(1, 1, "Problema con el login", tipo_problema.ACCESO, EstadoSoporte.RESUELTO);

        when(soporteService.actualizarEstado(anyInt(), any(EstadoSoporte.class))).thenReturn(soporteActualizado);

        mockMvc.perform(put("/soporte/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estadoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("RESUELTO"));
    }

    @Test
    void testObtenerSolicitudesPorUsuario_Found() throws Exception {

        int usuarioId = 1;
        List<Soporte> solicitudes = Arrays.asList(
            createTestSoporte(1, 1, "Desc 1", tipo_problema.ACCESO, EstadoSoporte.ABIERTO),
            createTestSoporte(2, 1, "Desc 2", tipo_problema.TECNICO, EstadoSoporte.RESUELTO)
        );

        when(soporteService.obtenerSolicitudesPorUsuario(usuarioId)).thenReturn(solicitudes);

        mockMvc.perform(get("/soporte/usuario/{usuarioId}", usuarioId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioId").value(1))
                .andExpect(jsonPath("$[1].usuarioId").value(1))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testObtenerSolicitudesPorUsuario_NotFound() throws Exception {

        int usuarioId = 99;
        when(soporteService.obtenerSolicitudesPorUsuario(usuarioId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/soporte/usuario/{usuarioId}", usuarioId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testObtenerTodasLasSolicitudes_Found() throws Exception {
        List<Soporte> solicitudes = Arrays.asList(
            createTestSoporte(1, 1, "Desc 1", tipo_problema.ACCESO, EstadoSoporte.ABIERTO),
            createTestSoporte(2, 2, "Desc 2", tipo_problema.TECNICO, EstadoSoporte.RESUELTO)
        );
        when(soporteService.obtenerTodasLasSolicitudes()).thenReturn(solicitudes);

        mockMvc.perform(get("/soporte/todas")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testObtenerTodasLasSolicitudes_NotFound() throws Exception {
        when(soporteService.obtenerTodasLasSolicitudes()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/soporte/todas")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFiltrarPorTipoProblema_Found() throws Exception {
        FiltroTipoProblemaDTO filtroDto = new FiltroTipoProblemaDTO();
        filtroDto.setTipoProblema(tipo_problema.ACCESO);

        List<Soporte> solicitudes = Arrays.asList(
            createTestSoporte(1, 1, "Problema login", tipo_problema.ACCESO, EstadoSoporte.ABIERTO),
            createTestSoporte(3, 2, "Error de credenciales", tipo_problema.ACCESO, EstadoSoporte.EN_PROCESO)
        );
        when(soporteService.filtrarPorTipoProblema(any(tipo_problema.class))).thenReturn(solicitudes);

        mockMvc.perform(post("/soporte/filtrarportipo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filtroDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipoProblema").value("ACCESO"))
                .andExpect(jsonPath("$[1].tipoProblema").value("ACCESO"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testFiltrarPorTipoProblema_NotFound() throws Exception {
        FiltroTipoProblemaDTO filtroDto = new FiltroTipoProblemaDTO();
        filtroDto.setTipoProblema(tipo_problema.OTRO);

        when(soporteService.filtrarPorTipoProblema(any(tipo_problema.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/soporte/filtrarportipo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filtroDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFiltrarPorEstado_Found() throws Exception {
        FiltroEstadoDTO filtroDto = new FiltroEstadoDTO();
        filtroDto.setEstado(EstadoSoporte.RESUELTO);
        List<Soporte> solicitudes = Arrays.asList(
            createTestSoporte(2, 1, "Problema impresora", tipo_problema.TECNICO, EstadoSoporte.RESUELTO),
            createTestSoporte(4, 3, "Conexión lenta", tipo_problema.OTRO, EstadoSoporte.RESUELTO)
        );
        when(soporteService.filtrarPorEstado(any(EstadoSoporte.class))).thenReturn(solicitudes);

        mockMvc.perform(post("/soporte/filtrarporestado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filtroDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("RESUELTO"))
                .andExpect(jsonPath("$[1].estado").value("RESUELTO"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testFiltrarPorEstado_NotFound() throws Exception {
        FiltroEstadoDTO filtroDto = new FiltroEstadoDTO();
        filtroDto.setEstado(EstadoSoporte.CERRADO);
        when(soporteService.filtrarPorEstado(any(EstadoSoporte.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/soporte/filtrarporestado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filtroDto)))
                .andExpect(status().isNoContent());
    }
}