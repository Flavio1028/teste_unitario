package br.com.codeup.service;


import br.com.codeup.entities.Location;
import br.com.codeup.entities.Movie;
import br.com.codeup.entities.User;
import br.com.codeup.exceptions.MovieWithoutStockException;
import br.com.codeup.exceptions.OfficeException;
import br.com.codeup.utils.DateUtil;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static br.com.codeup.builder.LocationBuilder.location;
import static br.com.codeup.builder.MovieBuilder.aFilmOutOfStock;
import static br.com.codeup.builder.MovieBuilder.movie;
import static br.com.codeup.builder.UserBuilder.user;
import static br.com.codeup.matchers.MatchersProprios.*;
import static br.com.codeup.utils.DateUtil.isSameDate;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


public class LocationServiceTest {

    @InjectMocks
    @Spy
    private LocationService service;

    @Mock
    private SPCService spc;
    @Mock
    private br.com.codeup.dao.Location dao;
    @Mock
    private EmailService email;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        System.out.println("Iniciando 2...");
        CalculatorTest.order.append("2");
    }

    @After
    public void tearDown() {
        System.out.println("finalizando 2...");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println(CalculatorTest.order.toString());
    }

    @Test
    public void mustRentMovie() throws Exception {
        //cenario
        User user = user().builder();
        List<Movie> movies = Collections.singletonList(movie().withValue(5.0).builder());

        Mockito.doReturn(DateUtil.getDate(28, 4, 2017)).when(service).getCurrentDate();

        //acao
        Location location = service.rentMovie(user, movies);

        //verificacao
        error.checkThat(location.getValue(), is(equalTo(5.0)));
        error.checkThat(isSameDate(location.getRentalDate(), DateUtil.getDate(28, 4, 2017)), is(true));
        error.checkThat(isSameDate(location.getReturnDate(), DateUtil.getDate(29, 4, 2017)), is(true));
    }

    @Test(expected = MovieWithoutStockException.class)
    public void mustNotRentMovieWithoutStock() throws Exception {
        //cenario
        User user = user().builder();
        List<Movie> movies = Collections.singletonList(aFilmOutOfStock().builder());

        //acao
        service.rentMovie(user, movies);
    }

    @Test
    public void mustNotRentMovieWithoutUser() throws MovieWithoutStockException {
        //cenario
        List<Movie> movies = Collections.singletonList(movie().builder());

        //acao
        try {
            service.rentMovie(null, movies);
            Assert.fail();
        } catch (OfficeException e) {
            assertThat(e.getMessage(), is("Usuario vazio"));
        }
    }

    @Test
    public void mustNotRentMovieWithoutMovie() throws MovieWithoutStockException, OfficeException {
        //cenario
        User user = user().builder();

        exception.expect(OfficeException.class);
        exception.expectMessage("Filme vazio");

        //acao
        service.rentMovie(user, null);
    }

    @Test
    public void mustReturnOnMondayWhenRentingOnSaturday() throws Exception {
        //cenario
        User user = user().builder();
        List<Movie> movie = Collections.singletonList(movie().builder());

        Mockito.doReturn(DateUtil.getDate(29, 4, 2017)).when(service).getCurrentDate();

        //acao
        Location retorno = service.rentMovie(user, movie);

        //verificacao
        assertThat(retorno.getReturnDate(), caiNumaSegunda());
    }

    @Test
    public void shouldNotRentFilmForNegativeSPC() throws Exception {
        //cenario
        User user = user().builder();
        List<Movie> movie = Collections.singletonList(movie().builder());

        when(spc.hasNegative(Mockito.any(User.class))).thenReturn(true);

        //acao
        try {
            service.rentMovie(user, movie);
            //verificacao
            Assert.fail();
        } catch (OfficeException e) {
            Assert.assertThat(e.getMessage(), is("Usuário Negativado"));
        }

        verify(spc).hasNegative(user);
    }

    @Test
    public void mustEmailLateLocations() {
        //cenario
        User user = user().builder();
        User user2 = user().withName("Usuario em dia").builder();
        User user3 = user().withName("Outro atrasado").builder();
        List<Location> locations = Arrays.asList(
                location().delay().withUser(user).builder(),
                location().withUser(user2).builder(),
                location().delay().withUser(user3).builder(),
                location().delay().withUser(user3).builder());

        when(dao.obtainPendingLease()).thenReturn(locations);

        //acao
        service.notifyDelays();

        //verificacao
        verify(email, times(3)).notifyDelay(Mockito.any(User.class));
        verify(email).notifyDelay(user);
        verify(email, Mockito.atLeastOnce()).notifyDelay(user3);
        verify(email, never()).notifyDelay(user2);
        verifyNoMoreInteractions(email);
    }

    @Test
    public void shouldHandleErrorInSPC() throws Exception {
        //cenario
        User user = user().builder();
        List<Movie> movie = Arrays.asList(movie().builder());

        when(spc.hasNegative(user)).thenThrow(new Exception("Falha catratrófica"));

        //verificacao
        exception.expect(OfficeException.class);
        exception.expectMessage("Problemas com SPC, tente novamente");

        //acao
        service.rentMovie(user, movie);

    }

    @Test
    public void mustExtendALocation() {
        //cenario
        Location location = location().builder();

        //acao
        service.extendLease(location, 3);

        //verificacao
        ArgumentCaptor<Location> argCapt = ArgumentCaptor.forClass(Location.class);
        Mockito.verify(dao).save(argCapt.capture());
        Location locationReturned = argCapt.getValue();

        error.checkThat(locationReturned.getValue(), is(12.0));
        error.checkThat(locationReturned.getRentalDate(), ehHoje());
        error.checkThat(locationReturned.getReturnDate(), ehHojeComDiferencaDias(3));
    }

    @Test
    public void mustCalculatesLocationValue() throws Exception {
        //cenario
        List<Movie> movies = Collections.singletonList(movie().builder());

        //acao
        Class<LocationService> clazz = LocationService.class;
        Method method = clazz.getDeclaredMethod("calculateRentalValue", List.class);
        method.setAccessible(true);
        Double valor = (Double) method.invoke(service, movies);

        //verificacao
        Assert.assertThat(valor, is(4.0));
    }
}
