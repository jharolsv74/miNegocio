package com.alquimiasoft.minegocio.repository;

import com.alquimiasoft.minegocio.entity.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Direccion
 */
@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {

    /**
     * Busca todas las direcciones de un cliente
     */
    List<Direccion> findByClienteId(Long clienteId);

    /**
     * Busca todas las direcciones de un cliente ordenadas por tipo (matriz primero)
     */
    List<Direccion> findByClienteIdOrderByEsMatrizDescCreadoEnAsc(Long clienteId);

    /**
     * Busca la dirección matriz de un cliente
     */
    Optional<Direccion> findByClienteIdAndEsMatrizTrue(Long clienteId);

    /**
     * Busca las direcciones adicionales (no matriz) de un cliente
     */
    List<Direccion> findByClienteIdAndEsMatrizFalse(Long clienteId);

    /**
     * Verifica si un cliente tiene dirección matriz
     */
    boolean existsByClienteIdAndEsMatrizTrue(Long clienteId);

    /**
     * Cuenta el número de direcciones de un cliente
     */
    long countByClienteId(Long clienteId);

    /**
     * Cuenta el número de direcciones adicionales de un cliente
     */
    long countByClienteIdAndEsMatrizFalse(Long clienteId);

    /**
     * Busca direcciones por ciudad
     */
    List<Direccion> findByCiudadContainingIgnoreCase(String ciudad);

    /**
     * Busca direcciones por provincia
     */
    List<Direccion> findByProvinciaContainingIgnoreCase(String provincia);

    /**
     * Busca direcciones por texto de dirección
     */
    List<Direccion> findByDireccionTextoContainingIgnoreCase(String direccionTexto);

    /**
     * Busca direcciones de un cliente por criterios de ubicación
     */
    @Query("SELECT d FROM Direccion d WHERE d.cliente.id = :clienteId " +
           "AND (LOWER(d.provincia) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
           "OR LOWER(d.ciudad) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
           "OR LOWER(d.direccionTexto) LIKE LOWER(CONCAT('%', :busqueda, '%')))")
    List<Direccion> buscarDireccionesPorClienteYTexto(@Param("clienteId") Long clienteId, 
                                                     @Param("busqueda") String busqueda);

    /**
     * Elimina todas las direcciones de un cliente
     */
    void deleteByClienteId(Long clienteId);

    /**
     * Busca direcciones con información del cliente
     */
    @Query("SELECT d FROM Direccion d JOIN FETCH d.cliente WHERE d.cliente.id = :clienteId")
    List<Direccion> findByClienteIdWithCliente(@Param("clienteId") Long clienteId);
}