package com.lucas.customers_service.model.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    public void shouldCreateClientWithBuilder() {
        Client client = Client.builder()
                .clientId(1L)
                .name("Lucas")
                .password("secret123")
                .status(true)
                .build();

        assertEquals(1L, client.getClientId());
        assertEquals("Lucas", client.getName());
        assertEquals("secret123", client.getPassword());
        assertTrue(client.getStatus());
    }

    @Test
    public void shouldAllowChangingPassword() {
        Client client = new Client();
        client.setPassword("initial");
        client.setPassword("updated");

        assertEquals("updated", client.getPassword());
    }
}