package com.ServicioDeSoporte.Servicio_De_Soporte.service;



import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ServicioDeSoporte.Servicio_De_Soporte.model.UsuarioDTO;

@Service
public class UsuarioClient {
    private final RestTemplate restTemplate = new RestTemplate();

    public UsuarioDTO getUsuarioById(int usuarioId) {
        String url = "http://192.168.56.1:8081/api/usuarios" + usuarioId;
        return restTemplate.getForObject(url, UsuarioDTO.class);
    }
}


