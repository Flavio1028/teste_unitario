package br.com.codeup.service;

import br.com.codeup.entities.Movie;
import br.com.codeup.entities.Location;
import br.com.codeup.entities.User;
import br.com.codeup.exceptions.MovieWithoutStockException;
import br.com.codeup.exceptions.OfficeException;
import br.com.codeup.utils.DateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.com.codeup.utils.DateUtil.addDays;

public class LocationService {

    private br.com.codeup.dao.Location dao;
    private SPCService spcService;
    private EmailService emailService;

    public Location rentMovie(User user, List<Movie> movies) throws MovieWithoutStockException, OfficeException {

        if (user == null) {
            throw new OfficeException("Usuario vazio");
        }

        if (movies == null || movies.isEmpty()) {
            throw new OfficeException("Filme vazio");
        }

        for (Movie movie : movies) {
            if (movie.getStock() == 0) {
                throw new MovieWithoutStockException();
            }
        }

        boolean negative;
        try {
            negative = spcService.hasNegative(user);
        } catch (Exception e) {
            throw new OfficeException("Problemas com SPC, tente novamente");
        }

        if (negative) {
            throw new OfficeException("Usuário Negativado");
        }

        Location location = new Location();
        location.setMovies(movies);
        location.setUser(user);
        location.setRentalDate(this.getCurrentDate());
        location.setValue(calculateRentalValue(movies));

        //Entrega no dia seguinte
        Date dataEntrega = this.getCurrentDate();
        dataEntrega = addDays(dataEntrega, 1);
        if (DateUtil.checkDayWeek(dataEntrega, Calendar.SUNDAY)) {
            dataEntrega = addDays(dataEntrega, 1);
        }
        location.setReturnDate(dataEntrega);

        dao.save(location);

        return location;
    }

    protected Date getCurrentDate() {
        return new Date();
    }

    private Double calculateRentalValue(List<Movie> movies) {
        double amount = 0d;
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            Double movieValue = movie.getRentalPrice();
            switch (i) {
                case 2:
                    movieValue = movieValue * 0.75;
                    break;
                case 3:
                    movieValue = movieValue * 0.5;
                    break;
                case 4:
                    movieValue = movieValue * 0.25;
                    break;
                case 5:
                    movieValue = 0d;
            }
            amount += movieValue;
        }
        return amount;
    }

    public void notifyDelays() {
        List<Location> locations = dao.obtainPendingLease();
        for (Location location : locations) {
            if (location.getReturnDate().before(this.getCurrentDate())) {
                emailService.notifyDelay(location.getUser());
            }
        }
    }

    public void extendLease(Location location, int days) {
        Location novaLocation = new Location();
        novaLocation.setUser(location.getUser());
        novaLocation.setMovies(location.getMovies());
        novaLocation.setRentalDate(this.getCurrentDate());
        novaLocation.setReturnDate(DateUtil.getDateWithDaysDifference(days));
        novaLocation.setValue(location.getValue() * days);
        dao.save(novaLocation);
    }
}