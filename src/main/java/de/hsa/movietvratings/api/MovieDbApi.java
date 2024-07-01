package de.hsa.movietvratings.api;

import info.movito.themoviedbapi.TmdbApi;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class MovieDbApi {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public TmdbApi tmdbApi() {
        return new TmdbApi("f090bb54758cabf231fb605d3e3e0468");
    }
}
