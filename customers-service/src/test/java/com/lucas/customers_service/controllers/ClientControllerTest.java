package com.lucas.customers_service.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.customers_service.exeptions.ResourceNotFoundException;
import com.lucas.customers_service.model.dtos.ClientRequest;
import com.lucas.customers_service.model.dtos.ClientResponse;
import com.lucas.customers_service.model.entities.Client;
import com.lucas.customers_service.services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
class ClientControllerTest {
    private final static String BASE_URL = "/api/clientes";

    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private ClientService clientService;

    private ClientController clientController;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        clientController = new ClientController(clientService);
    }

    @Test
    void addClient() throws Exception {
        ClientRequest clientRequest = new ClientRequest("Juan","Male",23,"1231231","Direccion", "094555555", "1234");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL).accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(clientRequest)))
                .andReturn();

        assertEquals(201, result.getResponse().getStatus());
    }

    @Test
    void getAllClients() throws Exception {
        MvcResult mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(200, mockMvcResult.getResponse().getStatus());
    }

    private String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    //----------------------------------------------------------------------------------------------------------

    @Test
    void testAddClientReturnsCreated() {
        ClientRequest request = new ClientRequest();
        request.setName("juan");
        request.setGender("Male");
        request.setAge(50);
        request.setIdentification("25323425");
        request.setAddress("Calle 1234");
        request.setPhoneNumber("091232423");
        request.setPassword("Password");

        ResponseEntity<Void> response = clientController.addClient(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(clientService).addClient(request);
    }

    @Test
    void testAddClientPassesCorrectRequestToService() {
        ClientRequest request = new ClientRequest();
        request.setName("juan");
        request.setGender("Male");
        request.setAge(50);
        request.setIdentification("25323425");
        request.setAddress("Calle 1234");
        request.setPhoneNumber("091232423");
        request.setPassword("Password");

        ArgumentCaptor<ClientRequest> captor = ArgumentCaptor.forClass(ClientRequest.class);

        ResponseEntity<Void> response = clientController.addClient(request);

        verify(clientService).addClient(captor.capture());
        ClientRequest capturedRequest = captor.getValue();

        assertEquals("juan", capturedRequest.getName());
        assertEquals("Male", capturedRequest.getGender());
        assertEquals(50, capturedRequest.getAge());
        assertEquals("25323425", capturedRequest.getIdentification());
        assertEquals("Calle 1234", capturedRequest.getAddress());
        assertEquals("091232423", capturedRequest.getPhoneNumber());
        assertEquals("Password", capturedRequest.getPassword());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testGetClientByIdReturnsClient() {
        Long clientId = 1L;
        ClientResponse expectedClient = new ClientResponse(clientId, "Jose","Male", 23,"1234244","Calle 34", "123765767","password",true );

        when(clientService.getClientById(clientId)).thenReturn(expectedClient);

        ResponseEntity<ClientResponse> response = clientController.getClientById(clientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Jose", response.getBody().getName());
        assertEquals("Male", response.getBody().getGender());
        assertEquals(23, response.getBody().getAge());
        assertEquals("1234244", response.getBody().getIdentification());
        assertEquals("Calle 34", response.getBody().getAddress());
        assertEquals("123765767", response.getBody().getPhoneNumber());
        assertEquals("password", response.getBody().getPassword());

        verify(clientService).getClientById(clientId);
    }

    @Test
    void testGetClientByIdThrowsResourceNotFoundExceptionWhenNotFound() {
        Long nonexistentId = 99L;

        when(clientService.getClientById(nonexistentId))
                .thenThrow(new ResourceNotFoundException("Account not found with id: " + nonexistentId));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            clientController.getClientById(nonexistentId);
        });

        assertEquals("Account not found with id: 99", exception.getMessage());
        verify(clientService).getClientById(nonexistentId);
    }

    @Test
    void testDeleteClientReturnsNoContent() {
        Long clientId = 1L;
        doNothing().when(clientService).removeClient(clientId);

        ResponseEntity<Void> response = clientController.deleteClient(clientId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clientService).removeClient(clientId);
    }

    @Test
    void testDeleteClientPassesCorrectIdToService() {
        Long expectedId = 99L;
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        doNothing().when(clientService).removeClient(anyLong());

        ResponseEntity<Void> response = clientController.deleteClient(expectedId);

        verify(clientService).removeClient(captor.capture());
        Long capturedId = captor.getValue();
        assertEquals(expectedId, capturedId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testUpdateClientReturnsOk() {
        Long clientId = 1L;
        ClientRequest request = new ClientRequest();
        request.setName("juan");
        request.setGender("Male");
        request.setAge(50);
        request.setIdentification("25323425");
        request.setAddress("Calle 1234");
        request.setPhoneNumber("091232423");
        request.setPassword("Password");

        Client updatedClient = new Client();
        updatedClient.setClientId(clientId);
        updatedClient.setName("Mario");
        updatedClient.setGender("Male");
        updatedClient.setAge(29);
        updatedClient.setIdentification("8787889");
        updatedClient.setAddress("Calle 307");
        updatedClient.setPhoneNumber("988238823");
        updatedClient.setPassword("1234");

        when(clientService.updateClient(clientId, request)).thenReturn(updatedClient);

        ResponseEntity<Void> response = clientController.updateClient(clientId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(clientService).updateClient(clientId, request);
    }

    @Test
    void testUpdateClientPassesCorrectParametersToService() {
        Long clientId = 5L;
        ClientRequest request = new ClientRequest();
        request.setName("Mario");
        request.setGender("Male");
        request.setAge(29);
        request.setIdentification("8787889");
        request.setAddress("Calle 307");
        request.setPhoneNumber("988238823");
        request.setPassword("1234");

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<ClientRequest> requestCaptor = ArgumentCaptor.forClass(ClientRequest.class);

        Client updatedClient = new Client();
        updatedClient.setClientId(clientId);
        updatedClient.setName("Mario");
        updatedClient.setGender("Male");
        updatedClient.setAge(29);
        updatedClient.setIdentification("8787889");
        updatedClient.setAddress("Calle 307");
        updatedClient.setPhoneNumber("988238823");
        updatedClient.setPassword("1234");


        when(clientService.updateClient(anyLong(), any(ClientRequest.class))).thenReturn(updatedClient);

        ResponseEntity<Void> response = clientController.updateClient(clientId, request);

        verify(clientService).updateClient(idCaptor.capture(), requestCaptor.capture());

        assertEquals(clientId, idCaptor.getValue());
        assertEquals("Mario", requestCaptor.getValue().getName());
        assertEquals("Male", requestCaptor.getValue().getGender());
        assertEquals(29, requestCaptor.getValue().getAge());
        assertEquals("8787889", requestCaptor.getValue().getIdentification());
        assertEquals("Calle 307", requestCaptor.getValue().getAddress());
        assertEquals("988238823", requestCaptor.getValue().getPhoneNumber());
        assertEquals("1234", requestCaptor.getValue().getPassword());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }










}