package com.ServicioDeSoporte.Servicio_De_Soporte.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private int id;
    private String nombre;
    private String tipo;
}