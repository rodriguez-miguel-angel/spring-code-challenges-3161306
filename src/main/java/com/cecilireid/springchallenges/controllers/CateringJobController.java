package com.cecilireid.springchallenges.controllers;

import com.cecilireid.springchallenges.exceptions.CateringJobNotFoundException;
import com.cecilireid.springchallenges.repositories.CateringJobRepository;
import com.cecilireid.springchallenges.models.Status;
import com.cecilireid.springchallenges.models.CateringJob;
import com.cecilireid.springchallenges.services.CateringJobService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * See <https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/boot-features-webclient.html>.
 */

@RestController
@RequestMapping("cateringJobs")
public class CateringJobController {

    /**
     * version-01:
     * private final CateringJobRepository cateringJobRepository;
     *     WebClient client;
     *
     *     public CateringJobController(CateringJobRepository cateringJobRepository, WebClient.Builder webClientBuilder) {
     *         this.cateringJobRepository = cateringJobRepository;
     *     }
     *
     *     @GetMapping
     *     @ResponseBody
     *     public List<CateringJob> getCateringJobs() {
     *         return cateringJobRepository.findAll();
     *     }
     *
     *     @GetMapping("/{id}")
     *     @ResponseBody
     *     public CateringJob getCateringJobById(@PathVariable Long id) {
     *         if (cateringJobRepository.existsById(id)) {
     *             return cateringJobRepository.findById(id).get();
     *         } else {
     *             throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
     *         }
     *     }
     *
     *     @GetMapping("/findByStatus")
     *     @ResponseBody
     *     public List<CateringJob> getCateringJobsByStatus(@RequestParam Status status) {
     *         if (cateringJobRepository.existsByStatus(status)) {
     *             return cateringJobRepository.findByStatus(status);
     *         } else {
     *             throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
     *         }
     *     }
     *
     *     @PostMapping(consumes = "application/json")
     *     @ResponseBody
     *     public CateringJob createCateringJob(@RequestBody CateringJob job) {
     *         return cateringJobRepository.save(job);
     *     }
     *
     *     @PutMapping(value = "/{id}")
     *     public CateringJob updateCateringJob(@RequestBody CateringJob cateringJob, @PathVariable("id") Long id) {
     *         if (cateringJobRepository.existsById(id)) {
     *             cateringJob.setId(id);
     *             return cateringJobRepository.save(cateringJob);
     *         } else {
     *             throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
     *         }
     *     }
     *
     *     @PatchMapping(path = "/{id}")
     *     @ResponseBody
     *     public CateringJob patchCateringJob(@PathVariable("id") Long id, @RequestBody JsonNode json) {
     *         Optional<CateringJob> optionalJob = cateringJobRepository.findById(id);
     *         if (optionalJob.isPresent()) {
     *             CateringJob cateringJob = optionalJob.get();
     *             JsonNode menu = json.get("menu");
     *             if(menu != null) {
     *                 cateringJob.setMenu(menu.asText());
     *                 return cateringJobRepository.save(cateringJob);
     *             } else {
     *                 throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
     *             }
     *         } catch (CateringJobNotFoundException e) {
     *             throw new CateringJobNotFoundException();
     *         }
     *     }
     */

    /**
     * version-02:
     */
    @Autowired
    private CateringJobService cateringJobService;

    @GetMapping
    @ResponseBody
    public List<CateringJob> getCateringJobs() {
        return cateringJobService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public CateringJob getCateringJobById(@PathVariable Long id) {
        return cateringJobService.getCateringJobById(id);
    }

    @GetMapping("/findByStatus")
    @ResponseBody
    public List<CateringJob> getCateringJobsByStatus(@RequestParam Status status) {
        return cateringJobService.getCateringJobsByStatus(status);
    }

    @PostMapping(consumes = "application/json")
    @ResponseBody
    public CateringJob createCateringJob(@RequestBody CateringJob job) {
        return cateringJobService.addCateringJob(job);
    }

    @PutMapping(value = "/{id}")
    public CateringJob updateCateringJob(@RequestBody CateringJob cateringJob, @PathVariable("id") Long id) {
        return cateringJobService.updateCateringJob(cateringJob, id);
    }

    /**
     * See <https://www.baeldung.com/spring-rest-json-patch>.
     */

    @PatchMapping(path = "/{id}")
    @ResponseBody
    public CateringJob patchCateringJob(@PathVariable("id") Long id, @RequestBody JsonNode json) {
        try {
            CateringJob cateringJob = cateringJobService.getCateringJobById(id);
            JsonNode menu = json.get("menu");
            if(menu != null) {
                cateringJob.setMenu(menu.asText());
                return cateringJobService.updateCateringJob(cateringJob);
            } else {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }
        } catch (CateringJobNotFoundException e) {
            throw new CateringJobNotFoundException();
        }
    }

    @RequestMapping("/surpriseMe")
    public Mono<String> getSurpriseImage() {
        return this.cateringJobService.getSurpriseImage();
    }

}
