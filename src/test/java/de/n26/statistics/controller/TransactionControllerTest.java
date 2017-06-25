package de.n26.statistics.controller;

import java.time.Instant;

import de.n26.statistics.domain.Transaction;
import de.n26.statistics.service.TransactionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static de.n26.statistics.utils.Constants.TRANSACTION_EXPIRATION_SEC;
import static org.apache.commons.lang3.RandomUtils.nextDouble;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

/**
 * Created by bvass on 24.06.17.
 */
public class TransactionControllerTest {

    private final TransactionService transactionServiceMock = createMock(TransactionService.class);
    private TransactionController transactionController;

    @Before
    public void setUp() throws Exception {
        transactionController = new TransactionController(transactionServiceMock);
    }

    @After
    public void tearDown() throws Exception {
        verify(transactionServiceMock);
    }

    @Test
    public void transactionOlderThanLimit() throws Exception {

        // given
        final int maxSeconds = 60;
        final long secondsOlder = TRANSACTION_EXPIRATION_SEC * nextInt(2, maxSeconds);
        final long transactionTimestamp = Instant.now().minusSeconds(secondsOlder).toEpochMilli();
        final Transaction transaction = new Transaction(nextDouble(1, Double.MAX_VALUE), transactionTimestamp);

        replay(transactionServiceMock);

        // when
        final ResponseEntity responseEntity = transactionController.save(transaction);

        // then
        assertThat(responseEntity.getBody()).isNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void transactionYoungerThanLimit() throws Exception {

        // given
        final long secondsYounger = TRANSACTION_EXPIRATION_SEC - nextInt(2, TRANSACTION_EXPIRATION_SEC - 1);
        final long transactionTimestamp = Instant.now().plusSeconds(secondsYounger).toEpochMilli();
        final Transaction transaction = new Transaction(nextDouble(1, Double.MAX_VALUE), transactionTimestamp);

        transactionServiceMock.createTransaction(eq(transaction));
        expectLastCall();
        replay(transactionServiceMock);

        // when
        final ResponseEntity responseEntity = transactionController.save(transaction);

        // then
        assertThat(responseEntity.getBody()).isNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
