package com.microservices.example.service;

import com.microservices.example.domain.responses.GetMovieByIdResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class MovieSearchService {

    public GetMovieByIdResponse findMovieByImdbId(String imdbId) {
        log.debug("findMovieByImdbId: imdbId = {}", imdbId);

        return GetMovieByIdResponse.builder()
                .imdbId("tt0462538")
                .title("The Simpsons Movie")
                .year("2007")
                .genre("Animation, Adventure, Comedy")
                .director("David Silverman")
                .poster("https://m.media-amazon.com/images/M/MV5BMTgxMDczMTA5N15BMl5BanBnXkFtZTcwMzk1MzMzMw@@._V1_SX300.jpg")
                .plot("Homer adopts a pig who's run away from Krusty Burger after Krusty tried to have him slaughtered, " +
                        "naming the pig \"Spider Pig.\" At the same time, the lake is protected after the audience sink " +
                        "the barge Green Day are on with garbage after they mention the environment. Meanwhile, " +
                        "Spider Pig's waste has filled up a silo in just 2 days, apparently with Homer's help. " +
                        "Homer can't get to the dump quickly so dumps the silo in the lake, polluting it. Russ Cargill, " +
                        "the villainous boss of the EPA, gives Arnold Schwarzenegger 5 options, forcing him to choose 4 " +
                        "(which is, unfortunately, to destroy Springfield) and putting a dome over Springfield to prevent evacuation. " +
                        "Homer, however, has escaped, along with his family. Can he stop the evil Cargill from annihilating his home town, " +
                        "and his family, who have been forced to return to Springfield?")
                .imdbRating("7.3")
                .type("movie")
                .build();
    }
}
