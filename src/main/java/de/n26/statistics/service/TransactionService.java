package de.n26.statistics.service;

import java.time.Instant;
import java.util.List;

import de.n26.statistics.domain.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import static de.n26.statistics.utils.Constants.TRANSACTION_EXPIRATION_SEC;

/**
 * Created by bvass on 24.06.17.
 */
@Service
public class TransactionService {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    public TransactionService(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTransaction(Transaction transaction) {
        log.info("Create transaction: " + transaction);
        jdbcTemplate.update("INSERT INTO transactions(amount, timestamp) VALUES (?,?)", transaction.getAmount(), transaction.getTimestamp());
    }

    public List<Transaction> readAllTransactions() {
        return jdbcTemplate.query(
                "SELECT amount, timestamp FROM transactions",
                (rs, rowNum) -> new Transaction(rs.getDouble("amount"), rs.getLong("timestamp"))
        );
    }

    public List<Transaction> readTransactionsForStatistics() {
        final long oldestTimestamp = Instant.now().minusSeconds(TRANSACTION_EXPIRATION_SEC).toEpochMilli();
        return jdbcTemplate.query(
                "SELECT amount, timestamp FROM transactions WHERE timestamp > ?", new Object[] { oldestTimestamp },
                (rs, rowNum) -> new Transaction(rs.getDouble("amount"), rs.getLong("timestamp"))
        );
    }

    public void cleanupTransactions() {
        log.debug("Transaction cleanup");
        final long lastTimestamp = Instant.now().minusSeconds(TRANSACTION_EXPIRATION_SEC).toEpochMilli();
        jdbcTemplate.update("DELETE FROM transactions WHERE timestamp < ?", lastTimestamp);
    }
}
