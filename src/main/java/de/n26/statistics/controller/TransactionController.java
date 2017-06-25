package de.n26.statistics.controller;

import java.time.Instant;

import de.n26.statistics.domain.Transaction;
import de.n26.statistics.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import static de.n26.statistics.utils.Constants.TRANSACTION_EXPIRATION_SEC;

/**
 * Created by bvass on 24.06.17.
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody final Transaction transaction) {
        log.debug("Trying to save transaction: " + transaction);
        final Instant transactionTimestamp = Instant.ofEpochMilli(transaction.getTimestamp());
        final Instant limit = Instant.now().minusSeconds(TRANSACTION_EXPIRATION_SEC);
        if (transactionTimestamp.compareTo(limit) > 0) {
            transactionService.createTransaction(transaction);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }
}
