package com.sooraj.transaction.statistics.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;

import com.sooraj.transaction.statistics.exception.OutOfTimeRangeException;
import com.sooraj.transaction.statistics.model.Transaction;
import com.sooraj.transaction.statistics.service.StatisticService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(value =TransactionController.class, secure = false)
public class TransactionControllerTest {
	
	@Autowired
    private MockMvc mvc;

    @MockBean
    private StatisticService statisticService;
    
    @Test
    public void transactionValidationTest() throws Exception {
        mvc.perform(post("/transactions")
                            .contentType("application/json")
                            .content("{\"timestamp\": 0}"))
                .andExpect(status().isNoContent())
                .andExpect(content().bytes(new byte[0]));

        verifyZeroInteractions(statisticService);
    }

    @Test
    public void addValidTransactionTest() throws Exception {
    	long epochCurrentMilli = Instant.now().toEpochMilli();
        mvc.perform(post("/transactions")
                            .contentType("application/json")
                            .content("{\"amount\": 250.3,\"timestamp\": "+epochCurrentMilli+"}"));

       verify(statisticService).updateStatisticForTransaction(new Transaction(250.3, epochCurrentMilli));
    }

   

    @Test
    public void outOfRangeTimeTest() throws Exception {
        doThrow(new OutOfTimeRangeException(""))
                .when(statisticService).updateStatisticForTransaction(any(Transaction.class));

        mvc.perform(post("/transactions")
                            .contentType("application/json")
                            .content("{\"amount\": 12.3,\"timestamp\": 123445567778}"))
                .andExpect(status().isNoContent())
                .andExpect(content().bytes(new byte[0]));
    }

}
