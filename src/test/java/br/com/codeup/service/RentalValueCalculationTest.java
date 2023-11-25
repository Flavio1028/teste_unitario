package br.com.codeup.service;


import br.com.codeup.entities.Location;
import br.com.codeup.entities.Movie;
import br.com.codeup.entities.User;
import br.com.codeup.exceptions.MovieWithoutStockException;
import br.com.codeup.exceptions.OfficeException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static br.com.codeup.builder.MovieBuilder.movie;
import static br.com.codeup.builder.UserBuilder.user;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class RentalValueCalculationTest {

    @InjectMocks
    private LocationService service;

    @Mock
    private br.com.codeup.dao.Location dao;

    @Mock
    private SPCService spc;

    @Parameter
    public static List<Movie> movie;

    @Parameter(value = 1)
    public Double valueLocation;

    @Parameter(value = 2)
    public String scenario;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        System.out.println("Iniciando 3...");
        CalculatorTest.order.append(3);
    }

    @After
    public void tearDown() {
        System.out.println("finalizando 3...");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println(CalculatorTest.order.toString());
    }

    private static final Movie movie1 = movie().builder();
    private static final Movie movie2 = movie().builder();
    private static final Movie movie3 = movie().builder();
    private static final Movie movie4 = movie().builder();
    private static final Movie movie5 = movie().builder();
    private static final Movie movie6 = movie().builder();
    private static final Movie movie7 = movie().builder();

    @Parameters(name = "{2}")
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList(movie1, movie2), 8.0, "2 movies: Sem Desconto"},
                {Arrays.asList(movie1, movie2, movie3), 11.0, "3 movies: 25%"},
                {Arrays.asList(movie1, movie2, movie3, movie4), 13.0, "4 movies: 50%"},
                {Arrays.asList(movie1, movie2, movie3, movie4, movie5), 14.0, "5 movies: 75%"},
                {Arrays.asList(movie1, movie2, movie3, movie4, movie5, movie6), 14.0, "6 movies: 100%"},
                {Arrays.asList(movie1, movie2, movie3, movie4, movie5, movie6, movie7), 18.0, "7 movies: Sem Desconto"}
        });
    }

    @Test
    public void calculateRentalValueConsideringDiscounts() throws OfficeException, MovieWithoutStockException {
        //cenario
        User usuario = user().builder();

        //acao
        Location resultado = service.rentMovie(usuario, movie);

        //verificacao
        assertThat(resultado.getValue(), is(valueLocation));
    }

}