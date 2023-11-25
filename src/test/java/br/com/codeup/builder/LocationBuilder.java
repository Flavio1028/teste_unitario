package br.com.codeup.builder;

import br.com.codeup.entities.Location;
import br.com.codeup.entities.Movie;
import br.com.codeup.entities.User;
import br.com.codeup.utils.DateUtil;

import java.util.Arrays;
import java.util.Date;

import static br.com.codeup.builder.MovieBuilder.movie;
import static br.com.codeup.builder.UserBuilder.user;
import static br.com.codeup.utils.DateUtil.getDateWithDaysDifference;

public class LocationBuilder {

    private Location entity;

    private LocationBuilder() {
    }

    public static LocationBuilder location() {
        LocationBuilder builder = new LocationBuilder();
        initializeDefaultData(builder);
        return builder;
    }

    public static void initializeDefaultData(LocationBuilder builder) {
        builder.entity = new Location();
        Location entity = builder.entity;

        entity.setUser(user().builder());
        entity.setMovies(Arrays.asList(movie().builder()));
        entity.setRentalDate(new Date());
        entity.setReturnDate(DateUtil.getDateWithDaysDifference(1));
        entity.setValue(4.0);
    }

    public LocationBuilder withUser(User param) {
        entity.setUser(param);
        return this;
    }

    public LocationBuilder withMoviesList(Movie... params) {
        entity.setMovies(Arrays.asList(params));
        return this;
    }

    public LocationBuilder withLocationDate(Date param) {
        entity.setRentalDate(param);
        return this;
    }

    public LocationBuilder withReturnDate(Date param) {
        entity.setReturnDate(param);
        return this;
    }

    public LocationBuilder delay() {
        entity.setRentalDate(getDateWithDaysDifference(-4));
        entity.setReturnDate(getDateWithDaysDifference(-2));
        return this;
    }

    public LocationBuilder withValue(Double param) {
        entity.setValue(param);
        return this;
    }

    public Location builder() {
        return entity;
    }

}