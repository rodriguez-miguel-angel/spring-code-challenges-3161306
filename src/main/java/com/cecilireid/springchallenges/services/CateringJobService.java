package com.cecilireid.springchallenges.services;

import com.cecilireid.springchallenges.exceptions.CateringJobNotFoundException;
import com.cecilireid.springchallenges.models.CateringJob;
import com.cecilireid.springchallenges.models.Status;
import com.cecilireid.springchallenges.repositories.CateringJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * See <https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/boot-features-webclient.html>.
 * See <https://docs.spring.io/spring-framework/docs/5.0.13.RELEASE/spring-framework-reference/web.html#web-reactive-client>.
 * See <https://docs.spring.io/spring-framework/docs/5.0.13.RELEASE/spring-framework-reference/web-reactive.html#webflux-client>.
 */

@Service
public class CateringJobService {

    private static final Logger logger = LoggerFactory.getLogger(CateringJobService.class);
    @Autowired
    private CateringJobRepository cateringJobRepository;

    public List<CateringJob> getAll() {
        logger.info("retrieving catering jobs ...");
        return cateringJobRepository.findAll();
    }

    public CateringJob getCateringJobById(Long id)  {
        if (cateringJobRepository.existsById(id)) {
            logger.info("retrieving catering job by id: {} ...", id);
            return cateringJobRepository.findById(id).orElseThrow();
        } else {
            /**
             * version-01:
             * throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
             */
            /**
             * version-02:
             */
            logger.info("retrieving catering job by id: {} ... NOT_FOUND", id);
            throw new CateringJobNotFoundException();
        }
    }

    public List<CateringJob> getCateringJobsByStatus(Status status) {
        logger.info("retrieving catering jobs by status: {} ...", status);
        if (cateringJobRepository.existsByStatus(status)) {
            return cateringJobRepository.findByStatus(status);
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }

    public CateringJob addCateringJob(CateringJob cateringJob) {
        logger.info("creating catering job ...");
        return cateringJobRepository.save(cateringJob);
    }

    public CateringJob updateCateringJob(CateringJob cateringJob) {
        logger.info("updating catering job ...");
        return cateringJobRepository.save(cateringJob);
    }

    /**
     * @param id the id of the CateringJob
     * @param newCateringJob the newCateringJob of the CateringJob
     */
    public CateringJob updateCateringJob(CateringJob newCateringJob, long id) {
        logger.info("updating catering job with id: {} ...", id);
        if (cateringJobRepository.existsById(id)) {
            return cateringJobRepository.findById(id)
                    .map(job -> {
                        job.setCustomerName(newCateringJob.getCustomerName());
                        job.setPhoneNumber(newCateringJob.getPhoneNumber());
                        job.setEmail(newCateringJob.getEmail());
                        job.setMenu(newCateringJob.getMenu());
                        job.setNoOfGuests(newCateringJob.getNoOfGuests());
                        job.setStatus(newCateringJob.getStatus());
                        return cateringJobRepository.save(job);
                    })
                    .orElseGet(() -> {
                        newCateringJob.setId(id);
                        return cateringJobRepository.save(newCateringJob);
                    });
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }

    public void deleteCateringJob(long id) {
        logger.info("deleting catering job by id: {} ...", id);
        if (cateringJobRepository.existsById(id)) {
            cateringJobRepository.deleteById(id);
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }

    private final WebClient webClient;

    /**
     * version-01:
     * private static final String IMAGE_API = "https://foodish-api.herokuapp.com";
     * version-02:
     * private static final String IMAGE_API = "https://picsum.photos/200/300";
     */
    private static final String IMAGE_API = "https://picsum.photos";

    public CateringJobService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(IMAGE_API).build();
    }

    public Mono<String> getSurpriseImage() {
        return this.webClient.get().uri("/v2/list")
                .retrieve().bodyToMono(String.class);
    }
}
