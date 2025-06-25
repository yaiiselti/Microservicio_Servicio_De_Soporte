package com.ServicioDeSoporte.Servicio_De_Soporte.model;

import lombok.Data; 

@Data
public class EstadoActualizacionDTO {

    private int soporteId;
    private EstadoSoporte nuevoEstado;
    
}