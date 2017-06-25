package de.n26.statistics.controller;

import java.time.Instant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerIT {

    @Autowired
    private MockMvc mvc;

    @Test
    public void returns204ForOldTransaction() throws Exception {

        final String requestBody = "{\"amount\":1.0,\"timestamp\":1478192204000}";

        this.mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isNoContent());
    }

    @Test
    public void returns201() throws Exception {

        final String requestBody = "{\"amount\":1.0,\"timestamp\":" + Instant.now().toEpochMilli() + "}";

        this.mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isCreated());
    }
}
