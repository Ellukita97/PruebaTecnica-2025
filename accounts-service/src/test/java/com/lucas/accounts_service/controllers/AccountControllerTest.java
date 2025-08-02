package com.lucas.accounts_service.controllers;

import com.lucas.accounts_service.exeptions.ResourceNotFoundException;
import com.lucas.accounts_service.model.dtos.AccountRequest;
import com.lucas.accounts_service.model.dtos.AccountResponse;
import com.lucas.accounts_service.model.dtos.ClientResponse;
import com.lucas.accounts_service.model.entities.Account;
import com.lucas.accounts_service.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    private AccountController accountController;

    @BeforeEach
    void setUp() {
        accountController = new AccountController(accountService);
    }

    @Test
    void testAddAccountReturnsCreated() {
        AccountRequest request = new AccountRequest();
        request.setClientId(1L);
        request.setAccountType("Corriente");
        request.setInitialBalance(1000.0);

        ResponseEntity<Void> response = accountController.addAccount(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(accountService).addAccount(request);
    }

    @Test
    void testAddAccountPassesCorrectRequestToService() {
        AccountRequest request = new AccountRequest();
        request.setClientId(42L);
        request.setAccountType("Ahorros");
        request.setInitialBalance(5000.0);

        ArgumentCaptor<AccountRequest> captor = ArgumentCaptor.forClass(AccountRequest.class);

        ResponseEntity<Void> response = accountController.addAccount(request);

        verify(accountService).addAccount(captor.capture());
        AccountRequest capturedRequest = captor.getValue();

        assertEquals(42L, capturedRequest.getClientId());
        assertEquals("Ahorros", capturedRequest.getAccountType());
        assertEquals(5000.0, capturedRequest.getInitialBalance());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testGetAllAccountReturnsEmptyList() {
        when(accountService.getAllAccount()).thenReturn(Collections.emptyList());

        ResponseEntity<List<AccountResponse>> response = accountController.getAllAccount();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(accountService).getAllAccount();
    }

    @Test
    void testGetAllAccountReturnsAccountList() {
        AccountResponse acc1 = new AccountResponse(1L, "Ahorros",1000.0, true
                , new ClientResponse(1L,"Juan","M",21,"123123123","Calle 125","092444444","pass",true)
        );

        AccountResponse acc2 = new AccountResponse(2L, "Corriente",2000.0, true
                , new ClientResponse(1L,"Juan","M",21,"123123123","Calle 125","092444444","pass",true)
        );

        List<AccountResponse> accountList = Arrays.asList(acc1, acc2);
        when(accountService.getAllAccount()).thenReturn(accountList);

        ResponseEntity<List<AccountResponse>> response = accountController.getAllAccount();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Ahorros", response.getBody().get(0).getAccountType());
        assertEquals(2000.0, response.getBody().get(1).getInitialBalance());

        verify(accountService).getAllAccount();
    }

    @Test
    void testGetAccountByIdReturnsAccount() {
        Long accountId = 1L;
        AccountResponse expectedAccount = new AccountResponse(accountId, "Ahorros",1500.0, true
                , new ClientResponse(1L,"Juan","M",21,"123123123","Calle 125","092444444","pass",true)
        );

        when(accountService.getAccountById(accountId)).thenReturn(expectedAccount);

        ResponseEntity<AccountResponse> response = accountController.getAccountById(accountId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1500.0, response.getBody().getInitialBalance());
        assertEquals("Ahorros", response.getBody().getAccountType());

        verify(accountService).getAccountById(accountId);
    }

    @Test
    void testGetAccountByIdThrowsResourceNotFoundExceptionWhenNotFound() {
        Long nonexistentId = 99L;

        when(accountService.getAccountById(nonexistentId))
                .thenThrow(new ResourceNotFoundException("Account not found with id: " + nonexistentId));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountController.getAccountById(nonexistentId);
        });

        assertEquals("Account not found with id: 99", exception.getMessage());
        verify(accountService).getAccountById(nonexistentId);
    }

    @Test
    void testDeleteAccountReturnsNoContent() {
        Long accountId = 1L;
        doNothing().when(accountService).removeAccount(accountId);

        ResponseEntity<Void> response = accountController.deleteAccount(accountId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(accountService).removeAccount(accountId);
    }

    @Test
    void testDeleteAccountPassesCorrectIdToService() {
        Long expectedId = 99L;
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        doNothing().when(accountService).removeAccount(anyLong());

        ResponseEntity<Void> response = accountController.deleteAccount(expectedId);

        verify(accountService).removeAccount(captor.capture());
        Long capturedId = captor.getValue();
        assertEquals(expectedId, capturedId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testUpdateAccountReturnsOk() {
        Long accountId = 1L;
        AccountRequest request = new AccountRequest();
        request.setClientId(10L);
        request.setAccountType("Corriente");
        request.setInitialBalance(2500.0);

        Account updatedAccount = new Account();
        updatedAccount.setAccountNumber(accountId);
        updatedAccount.setAccountType("Corriente");
        updatedAccount.setInitialBalance(2500.0);
        updatedAccount.setStatus(true);
        updatedAccount.setClientId(10L);

        when(accountService.updateAccount(accountId, request)).thenReturn(updatedAccount);

        ResponseEntity<Void> response = accountController.updateAccount(accountId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(accountService).updateAccount(accountId, request);
    }

    @Test
    void testUpdateAccountPassesCorrectParametersToService() {
        Long accountId = 5L;
        AccountRequest request = new AccountRequest();
        request.setClientId(20L);
        request.setAccountType("Ahorros");
        request.setInitialBalance(5000.0);

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<AccountRequest> requestCaptor = ArgumentCaptor.forClass(AccountRequest.class);

        Account updatedAccount = new Account();
        updatedAccount.setAccountNumber(accountId);
        updatedAccount.setAccountType("Ahorros");
        updatedAccount.setInitialBalance(5000.0);
        updatedAccount.setStatus(true);
        updatedAccount.setClientId(20L);

        when(accountService.updateAccount(anyLong(), any(AccountRequest.class))).thenReturn(updatedAccount);

        ResponseEntity<Void> response = accountController.updateAccount(accountId, request);

        verify(accountService).updateAccount(idCaptor.capture(), requestCaptor.capture());

        assertEquals(accountId, idCaptor.getValue());
        assertEquals(20L, requestCaptor.getValue().getClientId());
        assertEquals("Ahorros", requestCaptor.getValue().getAccountType());
        assertEquals(5000.0, requestCaptor.getValue().getInitialBalance());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}