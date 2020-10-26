package com.microservices.example.controllers;

import com.microservices.example.domain.responses.GetMovieByIdResponse;
import com.microservices.example.services.MovieSearchService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
@Log4j2
public class MovieSearchController {

    private final MovieSearchService movieSearchService;

    @Autowired
    public MovieSearchController(final MovieSearchService movieSearchService) {
        this.movieSearchService = movieSearchService;
    }

    @GetMapping("/{imdbId}")
    public ResponseEntity<GetMovieByIdResponse> getMovieById(@PathVariable String imdbId) {
        log.debug("getMovieById: imdbId = {}", imdbId);

        return ResponseEntity.ok(movieSearchService.findMovieByImdbId(imdbId));
    }
}
