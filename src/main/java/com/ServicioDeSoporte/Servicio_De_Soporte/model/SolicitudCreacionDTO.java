package com.ServicioDeSoporte.Servicio_De_Soporte.model;

import lombok.Data; 

@Data 
public class SolicitudCreacionDTO {
    private int usuarioId;
    private String descripcion;
    private tipo_problema tipoProblema; 
}