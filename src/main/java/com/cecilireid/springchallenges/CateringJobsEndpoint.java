package com.cecilireid.springchallenges;

import com.cecilireid.springchallenges.models.CateringJob;
import com.cecilireid.springchallenges.models.Status;
import com.cecilireid.springchallenges.repositories.CateringJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;

/**
 * See <https://www.baeldung.com/spring-boot-actuators>.
 */

@Component
@Endpoint(id = "catering-jobs")
public class CateringJobsEndpoint {

    /**
     * version-01:
     * private final CateringJobRepository cateringJobRepository;
     *
     * public CateringJobsEndpoint(CateringJobRepository cateringJobRepository) {
     *      this.cateringJobRepository = cateringJobRepository;
     * }
     */
    @Autowired
    private CateringJobRepository cateringJobRepository;

    @ReadOperation
    Map<Status, Long> getCateringJobsStatusMetrics() {
        return cateringJobRepository.findAll()
                .stream()
                .collect(groupingBy(CateringJob::getStatus, Collectors.counting()));
    }

}
