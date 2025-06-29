package com.ServicioDeSoporte.Servicio_De_Soporte.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.ServicioDeSoporte.Servicio_De_Soporte.model.UsuarioDTO;

@ExtendWith(MockitoExtension.class)
public class UsuarioClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UsuarioClient usuarioClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsuarioById_Success() {
        int userId = 1;
        String url = "http://localhost:8085/Usuarios/" + userId;
        UsuarioDTO expectedUser = new UsuarioDTO(userId, "Usuario Prueba", "admin"); 

        when(restTemplate.getForObject(eq(url), eq(UsuarioDTO.class)))
            .thenReturn(expectedUser);

        UsuarioDTO result = usuarioClient.getUsuarioById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId()); 
        assertEquals("Usuario Prueba", result.getNombre());  
        assertEquals("admin", result.getTipo());

        verify(restTemplate, times(1)).getForObject(eq(url), eq(UsuarioDTO.class));
    }

    @Test
    void testGetUsuarioById_NotFound() {
        int userId = 2;
        String url = "http://localhost:8085/Usuarios/" + userId;

        when(restTemplate.getForObject(eq(url), eq(UsuarioDTO.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            usuarioClient.getUsuarioById(userId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(restTemplate, times(1)).getForObject(eq(url), eq(UsuarioDTO.class));
    }

    @Test
    void testGetUsuarioById_ServerError() {

        int userId = 3;
        String url = "http://localhost:8085/Usuarios/" + userId;


        when(restTemplate.getForObject(eq(url), eq(UsuarioDTO.class)))
            .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        HttpServerErrorException exception = assertThrows(HttpServerErrorException.class, () -> {
            usuarioClient.getUsuarioById(userId);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        verify(restTemplate, times(1)).getForObject(eq(url), eq(UsuarioDTO.class));
    }

}