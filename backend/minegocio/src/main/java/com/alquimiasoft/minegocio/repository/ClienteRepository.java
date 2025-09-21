package com.alquimiasoft.minegocio.repository;

import com.alquimiasoft.minegocio.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Cliente
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca un cliente por empresa y número de identificación
     */
    Optional<Cliente> findByEmpresaIdAndNumeroIdentificacion(Long empresaId, String numeroIdentificacion);

    /**
     * Verifica si existe un cliente con el mismo tipo y número de identificación en una empresa
     */
    boolean existsByEmpresaIdAndTipoIdentificacionAndNumeroIdentificacion(
            Long empresaId, String tipoIdentificacion, String numeroIdentificacion);

    /**
     * Verifica si existe otro cliente (diferente al excluido) con el mismo tipo y número de identificación
     */
    boolean existsByEmpresaIdAndTipoIdentificacionAndNumeroIdentificacionAndIdNot(
            Long empresaId, String tipoIdentificacion, String numeroIdentificacion, Long excludeId);

    /**
     * Busca clientes por empresa y número de identificación (coincidencia exacta)
     */
    List<Cliente> findByEmpresaIdAndNumeroIdentificacionContainingIgnoreCase(
            Long empresaId, String numeroIdentificacion);

    /**
     * Busca clientes por empresa y nombres (coincidencia parcial)
     */
    List<Cliente> findByEmpresaIdAndNombresContainingIgnoreCase(Long empresaId, String nombres);

    /**
     * Busca clientes por empresa con paginación
     */
    Page<Cliente> findByEmpresaId(Long empresaId, Pageable pageable);

    /**
     * Busca clientes por múltiples criterios usando Query personalizado
     */
    @Query("SELECT c FROM Cliente c WHERE c.empresaId = :empresaId " +
           "AND (LOWER(c.numeroIdentificacion) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
           "OR LOWER(c.nombres) LIKE LOWER(CONCAT('%', :busqueda, '%')))")
    List<Cliente> buscarClientesPorEmpresaYTexto(@Param("empresaId") Long empresaId, 
                                                @Param("busqueda") String busqueda);

    /**
     * Busca clientes por múltiples criterios con paginación
     */
    @Query("SELECT c FROM Cliente c WHERE c.empresaId = :empresaId " +
           "AND (LOWER(c.numeroIdentificacion) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
           "OR LOWER(c.nombres) LIKE LOWER(CONCAT('%', :busqueda, '%')))")
    Page<Cliente> buscarClientesPorEmpresaYTexto(@Param("empresaId") Long empresaId, 
                                                @Param("busqueda") String busqueda, 
                                                Pageable pageable);

    /**
     * Busca clientes con sus direcciones cargadas
     */
    @Query("SELECT DISTINCT c FROM Cliente c LEFT JOIN FETCH c.direcciones WHERE c.empresaId = :empresaId")
    List<Cliente> findByEmpresaIdWithDirecciones(@Param("empresaId") Long empresaId);

    /**
     * Busca un cliente con sus direcciones cargadas por ID
     */
    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.direcciones WHERE c.id = :id")
    Optional<Cliente> findByIdWithDirecciones(@Param("id") Long id);

    /**
     * Obtiene todos los clientes de una empresa ordenados por nombre
     */
    List<Cliente> findByEmpresaIdOrderByNombresAsc(Long empresaId);

    /**
     * Cuenta el número de clientes por empresa
     */
    long countByEmpresaId(Long empresaId);

}