package com.microservices.example.domain.responses;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMovieByIdResponse {

    @JsonAlias("imdbID")
    private String imdbId;

    @JsonAlias("Title")
    private String title;

    @JsonAlias("Year")
    private String year;

    @JsonAlias("Genre")
    private String genre;

    @JsonAlias("Director")
    private String director;

    @JsonAlias("Poster")
    private String poster;

    @JsonAlias("Plot")
    private String plot;

    private String imdbRating;

    @JsonAlias("Type")
    private String type;
}
