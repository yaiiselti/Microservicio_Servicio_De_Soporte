package com.ServicioDeSoporte.Servicio_De_Soporte.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ServicioDeSoporte.Servicio_De_Soporte.model.EstadoSoporte;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.Soporte;
import com.ServicioDeSoporte.Servicio_De_Soporte.model.tipo_problema;

public interface SoporteRepository extends JpaRepository<Soporte, Integer> {
    List<Soporte> findAll();
    List<Soporte> findByUsuarioId(int usuarioId);

    @Query("SELECT s FROM Soporte s WHERE s.tipoProblema = :tipoProblema")
    List<Soporte> findByTipoProblema(tipo_problema tipoProblema);

    List<Soporte> findByEstado(EstadoSoporte estado);

    List<Soporte> findByTipoProblemaAndEstado(tipo_problema tipoProblema, EstadoSoporte estado);
}