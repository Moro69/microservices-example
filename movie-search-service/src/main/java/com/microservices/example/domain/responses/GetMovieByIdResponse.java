package com.microservices.example.domain.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMovieByIdResponse {

    private String imdbId;

    private String title;

    private String year;

    private String genre;

    private String director;

    private String poster;

    private String plot;

    private String imdbRating;

    private String type;
}
