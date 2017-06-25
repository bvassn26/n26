package de.n26.statistics.service;

import java.time.Instant;

import de.n26.statistics.domain.Statistics;
import de.n26.statistics.domain.Transaction;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

/**
 * Created by bvass on 25.06.17.
 */
public class StatisticsServiceTest {

    private final TransactionService transactionServiceMock = createMock(TransactionService.class);
    private StatisticsService statisticsService;

    @Before
    public void setUp() throws Exception {
        statisticsService = new StatisticsService(transactionServiceMock);
    }

    @Test
    public void updateStatistics() throws Exception {

        // given
        final double amount1 = 1.0;
        final double amount2 = 2.0;
        final double amount3 = 3.0;

        final Transaction transaction1 = new Transaction(amount1, Instant.now().toEpochMilli());
        final Transaction transaction2 = new Transaction(amount2, Instant.now().toEpochMilli());
        final Transaction transaction3 = new Transaction(amount3, Instant.now().toEpochMilli());

        transactionServiceMock.cleanupTransactions();
        expectLastCall();

        expect(transactionServiceMock.readTransactionsForStatistics()).andReturn(Lists.newArrayList(transaction1, transaction2, transaction3));
        replay(transactionServiceMock);

        // when
        statisticsService.updateStatistics();
        final Statistics statistics = statisticsService.getStatistics();

        // then
        verify(transactionServiceMock);
        assertThat(statistics.getMin()).isEqualTo(amount1);
        assertThat(statistics.getMax()).isEqualTo(amount3);
        assertThat(statistics.getCount()).isEqualTo(3);
        assertThat(statistics.getAvg()).isEqualTo(2);
        assertThat(statistics.getSum()).isEqualTo(6);
    }
}
