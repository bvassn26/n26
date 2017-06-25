package de.n26.statistics.controller;

import de.n26.statistics.domain.Statistics;
import de.n26.statistics.service.StatisticsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StatisticsControllerIT {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StatisticsService statisticsService;

    @Test
    public void getStatistics() throws Exception {

        final String expectedBody = "{\"sum\":1.0,\"avg\":2.0,\"max\":3.0,\"min\":4.0,\"count\":5}";
        final Statistics statistics = new Statistics(1, 2, 3, 4, 5);
        given(this.statisticsService.getStatistics()).willReturn(statistics);

        this.mvc.perform(get("/statistics")).andExpect(status().isOk()).andExpect(content().json(expectedBody));
    }
}
