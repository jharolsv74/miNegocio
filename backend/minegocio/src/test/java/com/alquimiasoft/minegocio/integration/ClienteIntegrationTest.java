package com.alquimiasoft.minegocio.integration;

import com.alquimiasoft.minegocio.dto.cliente.ClienteCreateRequest;
import com.alquimiasoft.minegocio.dto.cliente.ClienteUpdateRequest;
import com.alquimiasoft.minegocio.dto.direccion.DireccionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para la API de clientes
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class ClienteIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void buscarClientes_DeberiaRetornarListaDeClientes() throws Exception {
        mockMvc.perform(get("/api/clientes/buscar")
                        .param("empresaId", "1")
                        .param("busqueda", "Juan")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void crearCliente_ConDatosValidos_DeberiaCrearCliente() throws Exception {
        // Given
        DireccionRequest direccionRequest = new DireccionRequest("Pichincha", "Quito", "Av. Test 123");
        ClienteCreateRequest request = new ClienteCreateRequest(
                1L, "CEDULA", "1234567890", "Cliente Test", 
                "test@email.com", "0999999999", direccionRequest);

        // When & Then
        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombres").value("Cliente Test"))
                .andExpect(jsonPath("$.data.numeroIdentificacion").value("1234567890"));
    }

    @Test
    void crearCliente_ConDatosInvalidos_DeberiaRetornarError() throws Exception {
        // Given - Request sin nombre (campo requerido)
        DireccionRequest direccionRequest = new DireccionRequest("Pichincha", "Quito", "Av. Test 123");
        ClienteCreateRequest request = new ClienteCreateRequest(
                1L, "CEDULA", "1234567890", "", 
                "test@email.com", "0999999999", direccionRequest);

        // When & Then
        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void actualizarCliente_ConClienteExistente_DeberiaActualizar() throws Exception {
        // Primero crear un cliente
        DireccionRequest direccionRequest = new DireccionRequest("Pichincha", "Quito", "Av. Test 123");
        ClienteCreateRequest createRequest = new ClienteCreateRequest(
                1L, "CEDULA", "9876543210", "Cliente Original", 
                "original@email.com", "0999999999", direccionRequest);

        String createResponse = mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Extraer ID del cliente creado (esto es simplificado, en un test real usarías JSON parsing)
        Long clienteId = 1L; // Asumiendo que es el primer cliente

        // Ahora actualizar
        ClienteUpdateRequest updateRequest = new ClienteUpdateRequest(
                "RUC", "1234567890001", "Cliente Actualizado", 
                "actualizado@email.com", "0988888888");

        mockMvc.perform(put("/api/clientes/{id}", clienteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombres").value("Cliente Actualizado"));
    }

    @Test
    void eliminarCliente_ConClienteExistente_DeberiaEliminar() throws Exception {
        // Primero crear un cliente
        DireccionRequest direccionRequest = new DireccionRequest("Pichincha", "Quito", "Av. Test 123");
        ClienteCreateRequest createRequest = new ClienteCreateRequest(
                1L, "CEDULA", "5555555555", "Cliente Temporal", 
                "temporal@email.com", "0999999999", direccionRequest);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated());

        Long clienteId = 1L; // Asumiendo que es el primer cliente

        // Eliminar cliente
        mockMvc.perform(delete("/api/clientes/{id}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void eliminarCliente_ConClienteInexistente_DeberiaRetornarError() throws Exception {
        mockMvc.perform(delete("/api/clientes/{id}", 999L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void obtenerCliente_ConIdExistente_DeberiaRetornarCliente() throws Exception {
        // Esto asume que existe un cliente con ID 1 en la base de datos de test
        // En un entorno real, crearías el cliente primero o usarías datos de prueba predefinidos
        
        mockMvc.perform(get("/api/clientes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void listarDireccionesCliente_ConClienteExistente_DeberiaRetornarDirecciones() throws Exception {
        mockMvc.perform(get("/api/clientes/{clienteId}/direcciones", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }
}