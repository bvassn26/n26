package de.n26.statistics.service;

import java.time.Instant;
import java.util.List;

import de.n26.statistics.domain.Transaction;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import static de.n26.statistics.utils.Constants.TRANSACTION_EXPIRATION_SEC;
import static org.apache.commons.lang3.RandomUtils.nextDouble;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by bvass on 24.06.17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceIT {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public static final int MAX_SECONDS = 60;

    @After
    public void tearDown() throws Exception {
        jdbcTemplate.update("TRUNCATE TABLE transactions");
    }

    @Test
    public void saveTransaction() {

        // given
        final Transaction transaction = new Transaction(nextDouble(), Instant.now().toEpochMilli());

        // when
        transactionService.createTransaction(transaction);
        final List<Transaction> persistedTransactions = transactionService.readAllTransactions();

        // then
        assertThat(persistedTransactions).hasSize(1);
        assertThat(persistedTransactions.get(0)).isEqualTo(transaction);
    }

    @Test
    public void readTransactionsForStatistics() {

        // given
        final long secondsOlder = TRANSACTION_EXPIRATION_SEC * nextInt(2, MAX_SECONDS);
        final Transaction oldTransaction = new Transaction(nextDouble(), Instant.now().minusSeconds(secondsOlder).toEpochMilli());
        final Transaction transaction = new Transaction(nextDouble(), Instant.now().toEpochMilli());

        // when
        transactionService.createTransaction(oldTransaction);
        transactionService.createTransaction(transaction);
        final List<Transaction> persistedTransactions = transactionService.readTransactionsForStatistics();

        // then
        assertThat(persistedTransactions).hasSize(1);
        assertThat(persistedTransactions.get(0)).isEqualTo(transaction);
    }

    @Test
    public void cleanupTransactions() {

        // given
        final long secondsOlder = TRANSACTION_EXPIRATION_SEC * nextInt(2, MAX_SECONDS);
        final Transaction oldTransaction = new Transaction(nextDouble(), Instant.now().minusSeconds(secondsOlder).toEpochMilli());
        final Transaction transaction = new Transaction(nextDouble(), Instant.now().toEpochMilli());

        // when
        transactionService.createTransaction(oldTransaction);
        transactionService.createTransaction(transaction);
        transactionService.cleanupTransactions();
        final List<Transaction> persistedTransactions = transactionService.readAllTransactions();

        // then
        assertThat(persistedTransactions).hasSize(1);
        assertThat(persistedTransactions.get(0)).isEqualTo(transaction);
    }
}
