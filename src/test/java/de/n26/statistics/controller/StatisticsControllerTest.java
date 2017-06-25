package de.n26.statistics.controller;

import de.n26.statistics.domain.Statistics;
import de.n26.statistics.service.StatisticsService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.apache.commons.lang3.RandomUtils.nextDouble;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

/**
 * Created by bvass on 24.06.17.
 */
public class StatisticsControllerTest {

    private final StatisticsService statisticsServiceMock = createMock(StatisticsService.class);
    private StatisticsController statisticsController;

    @Before
    public void setUp() throws Exception {
        statisticsController = new StatisticsController(statisticsServiceMock);
    }

    @Test
    public void transactionOlderThanLimit() throws Exception {

        // given
        final Statistics statistics = new Statistics(nextDouble(), nextDouble(), nextDouble(), nextDouble(), nextInt());
        expect(statisticsServiceMock.getStatistics()).andReturn(statistics);
        replay(statisticsServiceMock);

        // when
        final ResponseEntity responseEntity = statisticsController.getStatistics();

        // then
        verify(statisticsServiceMock);
        assertThat(responseEntity.getBody()).isEqualTo(statistics);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
