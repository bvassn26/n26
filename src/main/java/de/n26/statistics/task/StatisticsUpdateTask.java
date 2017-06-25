package de.n26.statistics.task;

import de.n26.statistics.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by bvass on 24.06.17.
 */
@Component
public class StatisticsUpdateTask {
    private static final Logger log = LoggerFactory.getLogger(StatisticsUpdateTask.class);

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsUpdateTask(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Scheduled(fixedRate = 1000)
    public void updateStatistics() {
        log.debug("Updating statistics.");
        statisticsService.updateStatistics();
    }
}