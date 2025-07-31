package com.lucas.customers_service.controllers;

import com.lucas.customers_service.model.dtos.ClientRequest;
import com.lucas.customers_service.model.dtos.ClientResponse;
import com.lucas.customers_service.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addClient(@RequestBody ClientRequest clientRequest){
        this.clientService.addClient(clientRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ClientResponse> getAllClients(){
        return this.clientService.getAllClients();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClientResponse getClientById(@PathVariable Long id){
        return this.clientService.getClientById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable Long id){
        this.clientService.removeClient(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateClient(@PathVariable Long id, @RequestBody ClientRequest clientRequest){
        this.clientService.updateClient(id, clientRequest);
    }
}
