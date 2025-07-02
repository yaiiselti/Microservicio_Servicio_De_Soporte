package com.ServicioDeSoporte.Servicio_De_Soporte.DTOAS;


import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.ServicioDeSoporte.Servicio_De_Soporte.controller.SoporteController;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.EstadoSoporte;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.Soporte;

@Component
public class SoporteModelAssembler extends RepresentationModelAssemblerSupport<Soporte, SoporteDTOAS> {

    public SoporteModelAssembler() {
        super(SoporteController.class, SoporteDTOAS.class);
    }

    @Override
    public SoporteDTOAS toModel(Soporte entity) {
        SoporteDTOAS soporteDTO = instantiateModel(entity);

        soporteDTO.setId(entity.getId());
        soporteDTO.setUsuarioId(entity.getUsuarioId());
        soporteDTO.setDescripcion(entity.getDescripcion());
        soporteDTO.setTipoProblema(entity.getTipoProblema());
        soporteDTO.setEstado(entity.getEstado());
        soporteDTO.setFechaCreacionSolicitud(entity.getFechaCreacionSolicitud());
        soporteDTO.setFechaResolucion(entity.getFechaResolucion());

        soporteDTO.add(linkTo(methodOn(SoporteController.class).obtenerSolicitudPorId(entity.getId())).withSelfRel());
        soporteDTO.add(linkTo(methodOn(SoporteController.class).obtenerTodasLasSolicitudes()).withRel("soporte"));
        soporteDTO.add(linkTo(methodOn(SoporteController.class).actualizarEstado(null)).withRel("actualizarEstado"));

        if (entity.getEstado() == EstadoSoporte.ABIERTO) {
            }
        return soporteDTO;
    }
}
