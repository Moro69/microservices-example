package com.microservices.example.service;

import com.microservices.example.domain.responses.GetMovieByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "omdb-client", url = "${movie.search.uri}")
public interface OmdbApiClient {

    @GetMapping
    GetMovieByIdResponse getMovieById(@RequestParam(value = "i") String imdbId,
                                      @RequestParam(value = "apikey") String apiKey);
}
