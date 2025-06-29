package com.ServicioDeSoporte.Servicio_De_Soporte.service;



import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ServicioDeSoporte.Servicio_De_Soporte.model.UsuarioDTO;


@Service
public class UsuarioClient {
    private final RestTemplate restTemplate;

    public UsuarioClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UsuarioDTO getUsuarioById(int usuarioId) {
        String url = "http://localhost:8085/Usuarios/" + usuarioId;
        return restTemplate.getForObject(url, UsuarioDTO.class);
    }
}


