package com.lucas.transactions_service.controllers;

import com.lucas.transactions_service.model.dtos.ReportRequest;
import com.lucas.transactions_service.model.dtos.TransactionRequest;
import com.lucas.transactions_service.model.dtos.TransactionResponse;
import com.lucas.transactions_service.model.entities.Transaction;
import com.lucas.transactions_service.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        transactionController = new TransactionController(transactionService);
    }

    @Test
    void testAddTransactionReturnsCreated() {
        LocalDate date = LocalDate.of(2020, 2, 7);
        TransactionRequest request = new TransactionRequest();
        request.setDate(date);
        request.setType("Corriente");
        request.setInitialbalance(1000);
        request.setState(true);
        request.setTransactionAmount(1000);
        request.setAvailableBalance(2000);

        ResponseEntity<Void> response = transactionController.addTransaction(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(transactionService).addTransaction(request);
    }

    @Test
    void testAddTransactionPassesCorrectRequestToService() {
        LocalDate date = LocalDate.of(2020, 2, 7);
        TransactionRequest request = new TransactionRequest();
        request.setDate(date);
        request.setType("Corriente");
        request.setInitialbalance(1000);
        request.setState(true);
        request.setTransactionAmount(1000);
        request.setAvailableBalance(2000);

        ArgumentCaptor<TransactionRequest> captor = ArgumentCaptor.forClass(TransactionRequest.class);

        ResponseEntity<Void> response = transactionController.addTransaction(request);

        verify(transactionService).addTransaction(captor.capture());
        TransactionRequest capturedRequest = captor.getValue();

        assertEquals(date, capturedRequest.getDate());
        assertEquals("Corriente", capturedRequest.getType());
        assertEquals(1000, capturedRequest.getInitialbalance());
        assertEquals(true, capturedRequest.getState());
        assertEquals(1000, capturedRequest.getTransactionAmount());
        assertEquals(2000, capturedRequest.getAvailableBalance());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testGetAllTransactionReturnsEmptyList() {
        when(transactionService.getAllTransactions()).thenReturn(Collections.emptyList());

        ResponseEntity<List<TransactionResponse>> response = transactionController.getAllTransactions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(transactionService).getAllTransactions();
    }

    @Test
    void testGetAllTransactionReturnsTransactionList() {
        LocalDate date = LocalDate.of(2020, 2, 7);
        TransactionResponse acc1 = new TransactionResponse(1L, date, "Juan", 1L, "Ahorros", 1000, true, 1000,2000 );
        TransactionResponse acc2 = new TransactionResponse(2L, date, "Mario", 1L, "Corriente", 600, true, 400,1000 );

        List<TransactionResponse> transactionList = Arrays.asList(acc1, acc2);
        when(transactionService.getAllTransactions()).thenReturn(transactionList);

        ResponseEntity<List<TransactionResponse>> response = transactionController.getAllTransactions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());


        assertEquals("Ahorros", response.getBody().get(0).getType());
        assertEquals(600, response.getBody().get(1).getInitialbalance());

        verify(transactionService).getAllTransactions();
    }

    @Test
    void testDeleteTransactionReturnsNoContent() {
        Long transactionId = 1L;
        doNothing().when(transactionService).removeTransaction(transactionId);

        ResponseEntity<Void> response = transactionController.deleteTransaction(transactionId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(transactionService).removeTransaction(transactionId);
    }

    @Test
    void testDeleteTransactionPassesCorrectIdToService() {
        Long expectedId = 99L;
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        doNothing().when(transactionService).removeTransaction(anyLong());

        ResponseEntity<Void> response = transactionController.deleteTransaction(expectedId);

        verify(transactionService).removeTransaction(captor.capture());
        Long capturedId = captor.getValue();
        assertEquals(expectedId, capturedId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testUpdateTransactionReturnsOk() {
        Long transactionId = 1L;
        LocalDate date = LocalDate.of(2020, 2, 7);
        TransactionRequest request = new TransactionRequest();
        request.setDate(date);
        request.setType("Corriente");
        request.setInitialbalance(1000);
        request.setState(true);
        request.setTransactionAmount(1000);
        request.setAvailableBalance(2000);

        LocalDate date2 = LocalDate.of(2017, 8, 23);
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setDate(date2);
        updatedTransaction.setType("Ahorro");
        updatedTransaction.setInitialbalance(230);
        updatedTransaction.setState(true);
        updatedTransaction.setTransactionAmount(70);
        updatedTransaction.setAvailableBalance(300);

        when(transactionService.updateTransaction(transactionId, request)).thenReturn(updatedTransaction);

        ResponseEntity<Void> response = transactionController.updateTransaction(transactionId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(transactionService).updateTransaction(transactionId, request);
    }

    @Test
    void testUpdateAccountPassesCorrectParametersToService() {
        Long accountId = 5L;
        LocalDate date = LocalDate.of(2020, 2, 7);
        TransactionRequest request = new TransactionRequest();
        request.setDate(date);
        request.setType("Corriente");
        request.setInitialbalance(1000);
        request.setState(true);
        request.setTransactionAmount(1000);
        request.setAvailableBalance(2000);

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TransactionRequest> requestCaptor = ArgumentCaptor.forClass(TransactionRequest.class);

        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setDate(date);
        updatedTransaction.setType("Corriente");
        updatedTransaction.setInitialbalance(1000);
        updatedTransaction.setState(true);
        updatedTransaction.setTransactionAmount(1000);
        updatedTransaction.setAvailableBalance(2000);

        when(transactionService.updateTransaction(anyLong(), any(TransactionRequest.class))).thenReturn(updatedTransaction);

        ResponseEntity<Void> response = transactionController.updateTransaction(accountId, request);

        verify(transactionService).updateTransaction(idCaptor.capture(), requestCaptor.capture());

        assertEquals(accountId, idCaptor.getValue());
        assertEquals(date, requestCaptor.getValue().getDate());
        assertEquals("Corriente", requestCaptor.getValue().getType());
        assertEquals(1000, requestCaptor.getValue().getInitialbalance());
        assertEquals(true, requestCaptor.getValue().getState());
        assertEquals(1000, requestCaptor.getValue().getTransactionAmount());
        assertEquals(2000, requestCaptor.getValue().getAvailableBalance());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetReportsBetweenDatesSuccessfully() {
        ReportRequest request = new ReportRequest();
        request.setStartDate(LocalDate.of(2024, 1, 1));
        request.setEndDate(LocalDate.of(2024, 1, 31));

        List<TransactionResponse> mockResponse = List.of(
                new TransactionResponse(1L, LocalDate.of(2024, 1, 10), "Juan", 1L, "Ahorros", 1000, true, 1000,2000 ),
                new TransactionResponse(2L, LocalDate.of(2024, 1, 20), "Mario", 1L, "Corriente", 600, true, 400,1000 )
        );

        when(transactionService.getReportsBetweenDates(request)).thenReturn(mockResponse);

        ResponseEntity<List<TransactionResponse>> response = transactionController.getReportsBetweenDates(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Ahorros", response.getBody().get(0).getType());

        verify(transactionService).getReportsBetweenDates(request);
    }

    @Test
    void testGetReportsBetweenDatesNoResults() {
        ReportRequest request = new ReportRequest();
        request.setStartDate(LocalDate.of(2025, 1, 1));
        request.setEndDate(LocalDate.of(2025, 1, 31));

        when(transactionService.getReportsBetweenDates(request)).thenReturn(Collections.emptyList());

        ResponseEntity<List<TransactionResponse>> response = transactionController.getReportsBetweenDates(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(transactionService).getReportsBetweenDates(request);
    }

    @Test
    void getReportsBetweenDates() {
    }
}