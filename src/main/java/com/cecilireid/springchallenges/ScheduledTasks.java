package com.cecilireid.springchallenges;

import com.cecilireid.springchallenges.models.CateringJob;
import com.cecilireid.springchallenges.repositories.CateringJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;



@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private CateringJobRepository cateringJobRepository;

    /**
     * See <https://www.baeldung.com/cron-expressions>.
     */
    @Scheduled(cron = "0/10 * * * * *")
    public void reportOrderStats() {
        List<CateringJob> jobs = cateringJobRepository.findAll();
        logger.info("retrieving catering jobs ... {} jobs ...", jobs.size());
    }
}
