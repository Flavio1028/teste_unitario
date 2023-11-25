package br.com.codeup.builder;

import br.com.codeup.entities.Movie;

public class MovieBuilder {
    private Movie entity;

    private MovieBuilder() {
    }

    public static MovieBuilder movie() {
        MovieBuilder builder = new MovieBuilder();
        builder.entity = new Movie();
        builder.entity.setStock(2);
        builder.entity.setName("Filme 1");
        builder.entity.setRentalPrice(4.0);
        return builder;
    }

    public static MovieBuilder aFilmOutOfStock() {
        MovieBuilder builder = new MovieBuilder();
        builder.entity = new Movie();
        builder.entity.setStock(0);
        builder.entity.setName("Filme 1");
        builder.entity.setRentalPrice(4.0);
        return builder;
    }

    public MovieBuilder outOfStock() {
        entity.setStock(0);
        return this;
    }

    public MovieBuilder withValue(Double valor) {
        entity.setRentalPrice(valor);
        return this;
    }

    public Movie builder() {
        return entity;
    }

}