package com.ServicioDeSoporte.Servicio_De_Soporte.DTOAS;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.ServicioDeSoporte.Servicio_De_Soporte.model.EstadoSoporte;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.tipo_problema;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "soporte", itemRelation = "solicitud")
public class SoporteDTOAS extends RepresentationModel<SoporteDTOAS>{

    private Integer id;
    private Integer usuarioId;
    private String descripcion;
    private tipo_problema tipoProblema;
    private EstadoSoporte estado;
    private LocalDateTime fechaCreacionSolicitud;
    private LocalDateTime fechaResolucion;
}


