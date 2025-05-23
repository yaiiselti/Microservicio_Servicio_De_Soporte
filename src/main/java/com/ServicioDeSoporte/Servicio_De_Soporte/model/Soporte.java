package com.ServicioDeSoporte.Servicio_De_Soporte.model;



import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "soporte")
public class Soporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "usuario_id", nullable = false)
    private int usuarioId;

    @Column(name = "fecha_creacion_solicitud", nullable = false)
    private LocalDateTime fechaCreacionSolicitud;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 50)
    private EstadoSoporte estado;

    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_problema", nullable = false, length = 50)
    private tipo_problema tipoProblema;

    @Column(name = "respuesta", length = 500)
    private String respuesta;

    
    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;
}

