package com.lucas.customers_service.services;

import com.lucas.customers_service.model.dtos.ClientRequest;
import com.lucas.customers_service.model.dtos.ClientResponse;
import com.lucas.customers_service.model.entities.Client;
import com.lucas.customers_service.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    @Autowired
    private final ClientRepository clientRepository;

    public void addClient(ClientRequest clientRequest){
        var client = Client.builder()
                .name(clientRequest.getName())
                .gender(clientRequest.getGender())
                .age(clientRequest.getAge())
                .identification(clientRequest.getIdentification())
                .address(clientRequest.getAddress())
                .phoneNumber(clientRequest.getPhoneNumber())
                .password(clientRequest.getPassword())
                .status(true)
                .build();

        clientRepository.save(client);

        log.info("Client added: {}", client);
    }

    public void removeClient(Long id){
        clientRepository.deleteById(id);
    }

    public Client updateClient(Long id, ClientRequest clientRequestUpdated) {
        return clientRepository.findById(id).map(client -> {
            client.setName(clientRequestUpdated.getName());
            client.setGender(clientRequestUpdated.getGender());
            client.setAge(clientRequestUpdated.getAge());
            client.setIdentification(clientRequestUpdated.getIdentification());
            client.setAddress(clientRequestUpdated.getAddress());
            client.setPhoneNumber(clientRequestUpdated.getPhoneNumber());
            client.setPassword(clientRequestUpdated.getPassword());
            client.setStatus(true);
            return clientRepository.save(client);
        }).orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
    }



    public List<ClientResponse> getAllClients(){
        var clients = clientRepository.findAll();

        return clients.stream().map(this::mapToClientResponse).toList();
    }

    public ClientResponse getClientById(Long id){
        var client = clientRepository.findById(id);

        return mapToClientResponse(client.get());
    }


    private ClientResponse mapToClientResponse(Client client){
        return ClientResponse.builder()
                .id(client.getClientId())
                .name(client.getName())
                .gender(client.getGender())
                .age(client.getAge())
                .identification(client.getIdentification())
                .address(client.getAddress())
                .phoneNumber(client.getPhoneNumber())
                .password(client.getPassword())
                .status(client.getStatus())
                .build();
    }


}
