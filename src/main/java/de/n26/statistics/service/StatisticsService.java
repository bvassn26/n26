package de.n26.statistics.service;

import java.util.List;
import java.util.concurrent.atomic.DoubleAdder;

import de.n26.statistics.domain.Statistics;
import de.n26.statistics.domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by bvass on 24.06.17.
 */
@Service
@Scope("prototype")
public class StatisticsService {

    private static Statistics statistics;

    private final TransactionService transactionService;

    @Autowired
    public StatisticsService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public void updateStatistics() {
        transactionService.cleanupTransactions();
        final DoubleAdder sum = new DoubleAdder();
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;


        final List<Transaction> transactions = transactionService.readTransactionsForStatistics();
        for (final Transaction transaction : transactions) {
            sum.add(transaction.getAmount());
            if (max < transaction.getAmount()) {
                max = transaction.getAmount();
            }
            if (min > transaction.getAmount()) {
                min = transaction.getAmount();
            }
        }

        long count = transactions.size();

        statistics = new Statistics(sum.doubleValue(), sum.doubleValue() / count, max, min, count);
    }

    public Statistics getStatistics() {
        return statistics;
    }
}
