package com.microservices.example.service;

import com.microservices.example.domain.responses.GetMovieByIdResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class MovieSearchService {

    @Value("${movie.search.apikey}")
    private String apiKey;

    private final OmdbApiClient omdbApiClient;

    @Autowired
    public MovieSearchService(final OmdbApiClient omdbApiClient) {
        this.omdbApiClient = omdbApiClient;
    }

    public GetMovieByIdResponse findMovieByImdbId(String imdbId) {
        log.debug("findMovieByImdbId: imdbId = {}", imdbId);

        return omdbApiClient.getMovieById(imdbId, apiKey);
    }
}
