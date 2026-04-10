package com.example.hospitalsystem.repository;

import com.example.hospitalsystem.model.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByColegiatura(String colegiatura);
    List<Medico> findByEstado(String estado);

    @Query("SELECT m FROM Medico m WHERE " +
           "(:search IS NULL OR LOWER(m.nombres) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(m.apellidos) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(m.colegiatura) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:estado IS NULL OR m.estado = :estado)")
    Page<Medico> search(@Param("search") String search,
                        @Param("estado") String estado,
                        Pageable pageable);
}
