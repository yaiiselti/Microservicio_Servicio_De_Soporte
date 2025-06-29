package com.ServicioDeSoporte.Servicio_De_Soporte.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.ServicioDeSoporte.Servicio_De_Soporte.model.EstadoSoporte;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.Soporte;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.UsuarioDTO;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.tipo_problema;
import com.ServicioDeSoporte.Servicio_De_Soporte.repository.SoporteRepository;



public class soporteservicetest {

    @Mock
    private SoporteRepository soporteRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks 
    private SoporteService soporteService;

  
    private Soporte solicitudEjemplo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        solicitudEjemplo = new Soporte();
        solicitudEjemplo.setId(1);
        solicitudEjemplo.setUsuarioId(101);
        solicitudEjemplo.setDescripcion("Problema con el login del sistema");
        solicitudEjemplo.setTipoProblema(tipo_problema.OTRO);
        solicitudEjemplo.setEstado(EstadoSoporte.ABIERTO);
        solicitudEjemplo.setFechaCreacionSolicitud(LocalDateTime.now());
    }
    
    @Test
    void testCrearSolicitud_DebeGuardarYRetornarSolicitud() {
        UsuarioDTO dummyUser = new UsuarioDTO(solicitudEjemplo.getUsuarioId(), "Usuario Test", "email@ejemplo.com");

        when(usuarioClient.getUsuarioById(solicitudEjemplo.getUsuarioId())).thenReturn(dummyUser);
        when(soporteRepository.save(any(Soporte.class))).thenReturn(solicitudEjemplo);
        Soporte resultado = soporteService.crearSolicitud(
            solicitudEjemplo.getUsuarioId(),
            solicitudEjemplo.getDescripcion(),
            solicitudEjemplo.getTipoProblema()
        );

        assertNotNull(resultado);
        assertEquals(solicitudEjemplo.getId(), resultado.getId());
        assertEquals(solicitudEjemplo.getUsuarioId(), resultado.getUsuarioId());
        assertEquals(solicitudEjemplo.getDescripcion(), resultado.getDescripcion());
        assertEquals(EstadoSoporte.ABIERTO, resultado.getEstado());
        assertNotNull(resultado.getFechaCreacionSolicitud());
        verify(soporteRepository, times(1)).save(any(Soporte.class));
        verify(usuarioClient, times(1)).getUsuarioById(solicitudEjemplo.getUsuarioId());
    }


    @Test
    void testActualizarEstado_DebeActualizarEstadoYFechaResolucionCuandoResuelto() {
        when(soporteRepository.findById(solicitudEjemplo.getId())).thenReturn(Optional.of(solicitudEjemplo));
        when(soporteRepository.save(any(Soporte.class))).thenReturn(solicitudEjemplo);

        EstadoSoporte nuevoEstado = EstadoSoporte.RESUELTO;

        Soporte resultado = soporteService.actualizarEstado(solicitudEjemplo.getId(), nuevoEstado);

        assertNotNull(resultado);
        assertEquals(nuevoEstado, resultado.getEstado());
        assertNotNull(resultado.getFechaResolucion());
        verify(soporteRepository, times(1)).findById(solicitudEjemplo.getId());
        verify(soporteRepository, times(1)).save(any(Soporte.class));
    }

    @Test
    void testActualizarEstado_NoDebeEstablecerFechaResolucionParaOtrosEstados() {
        when(soporteRepository.findById(solicitudEjemplo.getId())).thenReturn(Optional.of(solicitudEjemplo));
        when(soporteRepository.save(any(Soporte.class))).thenReturn(solicitudEjemplo);

    
        solicitudEjemplo.setEstado(EstadoSoporte.EN_PROCESO);
        solicitudEjemplo.setFechaResolucion(null);

        EstadoSoporte nuevoEstado = EstadoSoporte.EN_PROCESO;
        Soporte resultado = soporteService.actualizarEstado(solicitudEjemplo.getId(), nuevoEstado);

        assertNotNull(resultado);
        assertEquals(nuevoEstado, resultado.getEstado());
        assertNull(resultado.getFechaResolucion());
        verify(soporteRepository, times(1)).findById(solicitudEjemplo.getId());
        verify(soporteRepository, times(1)).save(any(Soporte.class));
    }

    @Test
    void testActualizarEstado_DebeLanzarExcepcionCuandoNoEncontrado() {
        when(soporteRepository.findById(anyInt())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            soporteService.actualizarEstado(999, EstadoSoporte.CERRADO);
        });

        assertEquals("la solicitud de soporte no encontrado con ID: 999", thrown.getMessage());
        verify(soporteRepository, never()).save(any(Soporte.class));
    }

    @Test
    void testObtenerSolicitudesPorUsuario_DebeRetornarListaDeSolicitudes() {
        List<Soporte> solicitudesUsuario = Arrays.asList(
            solicitudEjemplo,
            new Soporte(1, 101, LocalDateTime.now(), EstadoSoporte.ABIERTO, "Problema con la impresora", tipo_problema.OTRO, null)
        );
        when(soporteRepository.findByUsuarioId(101)).thenReturn(solicitudesUsuario);

        List<Soporte> resultado = soporteService.obtenerSolicitudesPorUsuario(101);

        assertThat(resultado).isNotNull().hasSize(2);
        assertThat(resultado).containsExactlyInAnyOrder(solicitudEjemplo, solicitudesUsuario.get(1));
        verify(soporteRepository, times(1)).findByUsuarioId(101);
    }

    @Test
    void testObtenersolicitudesPorUsuario_debeRetornarListaVaciaSiNoHaysolicitudes() {
        when(soporteRepository.findByUsuarioId(999)).thenReturn(Collections.emptyList());

        List<Soporte> resultado = soporteService.obtenerSolicitudesPorUsuario(999);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(soporteRepository, times(1)).findByUsuarioId(999);
    }

    @Test
    void testObtenerTodasLasSolicitudes_DebeRetornarTodasLasSolicitudes() {
        List<Soporte> todasLasSolicitudes = Arrays.asList(
            solicitudEjemplo,
            new Soporte(2, 102, LocalDateTime.now(), EstadoSoporte.EN_PROCESO, "Internet lento en la oficina", tipo_problema.OTRO, null),
            new Soporte(3, 103, LocalDateTime.now(), EstadoSoporte.RESUELTO, "Software de diseño bloqueado", tipo_problema.TECNICO, LocalDateTime.now())
        );
        when(soporteRepository.findAll()).thenReturn(todasLasSolicitudes);

        List<Soporte> resultado = soporteService.obtenerTodasLasSolicitudes();

        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertThat(resultado).containsExactlyInAnyOrderElementsOf(todasLasSolicitudes);
        verify(soporteRepository, times(1)).findAll();
    }

    @Test
    void testObtenerTodasLasSolicitudes_DebeRetornarListaVaciaSiNoHaySolicitudes() {
        when(soporteRepository.findAll()).thenReturn(Collections.emptyList());

        List<Soporte> resultado = soporteService.obtenerTodasLasSolicitudes();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(soporteRepository, times(1)).findAll();
    }

     @Test
    void testFiltrarPorTipoProblema_DebeRetornarSolicitudesDelTipoCorrecto() {

        Soporte soporteSoftware1 = new Soporte(10, 201, LocalDateTime.now(), EstadoSoporte.ABIERTO, "Problema con Ia", tipo_problema.OTRO, null);
        Soporte soporteSoftware2 = new Soporte(11, 202, LocalDateTime.now(), EstadoSoporte.EN_PROCESO, "Bug en librería", tipo_problema.TECNICO, null);

        List<Soporte> solicitudesSoftware = Arrays.asList(
            soporteSoftware1,
            soporteSoftware2
        );
        when(soporteRepository.findByTipoProblema(tipo_problema.OTRO)).thenReturn(solicitudesSoftware);

        List<Soporte> resultado = soporteService.filtrarPorTipoProblema(tipo_problema.OTRO);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertThat(resultado).containsExactlyInAnyOrderElementsOf(solicitudesSoftware);
        verify(soporteRepository, times(1)).findByTipoProblema(tipo_problema.OTRO);
    }

    @Test
    void testFiltrarPorTipoProblema_DebeRetornarListaVaciaSiNoHayCoincidencias() {
        when(soporteRepository.findByTipoProblema(tipo_problema.TECNICO)).thenReturn(Collections.emptyList());

        List<Soporte> resultado = soporteService.filtrarPorTipoProblema(tipo_problema.TECNICO);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(soporteRepository, times(1)).findByTipoProblema(tipo_problema.TECNICO);
    }

    @Test
    void testFiltrarPorEstado_DebeRetornarSolicitudesDelEstadoCorrecto() {
        Soporte soportePendiente1 = new Soporte(20, 301, LocalDateTime.now(), EstadoSoporte.EN_PROCESO, "Error de acceso", tipo_problema.ACCESO, null);
        Soporte soportePendiente2 = new Soporte(21, 302, LocalDateTime.now(), EstadoSoporte.ABIERTO, "Configuración fallida", tipo_problema.TECNICO, null);

        List<Soporte> solicitudesPendientes = Arrays.asList(
            soportePendiente1,
            soportePendiente2
        );
        when(soporteRepository.findByEstado(EstadoSoporte.EN_PROCESO)).thenReturn(solicitudesPendientes);

        List<Soporte> resultado = soporteService.filtrarPorEstado(EstadoSoporte.EN_PROCESO);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertThat(resultado).containsExactlyInAnyOrderElementsOf(solicitudesPendientes);
        verify(soporteRepository, times(1)).findByEstado(EstadoSoporte.EN_PROCESO);
    }


    @Test
    void test_Filtrar_PorEstado_Debe_Retornar_Lista_Vacia_SiNoHay_Coincidencias() {
        when(soporteRepository.findByEstado(EstadoSoporte.CERRADO)).thenReturn(Collections.emptyList());

        List<Soporte> resultado = soporteService.filtrarPorEstado(EstadoSoporte.CERRADO);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(soporteRepository, times(1)).findByEstado(EstadoSoporte.CERRADO);
    }

}