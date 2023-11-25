package br.com.codeup.service;

import br.com.codeup.entities.Location;
import br.com.codeup.entities.Movie;
import br.com.codeup.entities.User;
import br.com.codeup.utils.DateUtil;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static br.com.codeup.builder.MovieBuilder.movie;
import static br.com.codeup.builder.UserBuilder.user;
import static br.com.codeup.matchers.MatchersProprios.caiNumaSegunda;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(PowerMockRunner.class)
@PrepareForTest({LocationService.class})
public class LocacaoServiceTest_PowerMock {

    @InjectMocks
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
        service = PowerMockito.spy(service);
        System.out.println("Iniciando 4...");
        CalculatorTest.order.append(4);
    }

    @After
    public void tearDown() {
        System.out.println("finalizando 4...");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println(CalculatorTest.order.toString());
    }

    @Test
    public void deveAlugarFilme() throws Exception {
        //cenario
        User usuario = user().builder();
        List<Movie> filmes = Arrays.asList(movie().withValue(5.0).builder());

        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DateUtil.getDate(28, 4, 2017));

        //acao
        Location locacao = service.rentMovie(usuario, filmes);

        //verificacao
        error.checkThat(locacao.getValue(), is(equalTo(5.0)));
        error.checkThat(DateUtil.isSameDate(locacao.getRentalDate(), DateUtil.getDate(28, 4, 2017)), is(true));
        error.checkThat(DateUtil.isSameDate(locacao.getRentalDate(), DateUtil.getDate(29, 4, 2017)), is(true));
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
        //cenario
        User usuario = user().builder();
        List<Movie> filmes = Arrays.asList(movie().builder());

        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DateUtil.getDate(29, 4, 2017));

        //acao
        Location retorno = service.rentMovie(usuario, filmes);

        //verificacao
        assertThat(retorno.getReturnDate(), caiNumaSegunda());
    }

    @Test
    public void deveAlugarFilme_SemCalcularValor() throws Exception {
        //cenario
        User usuario = user().builder();
        List<Movie> filmes = Arrays.asList(movie().builder());

        PowerMockito.doReturn(1.0).when(service, "calcularValorLocacao", filmes);

        //acao
        Location locacao = service.rentMovie(usuario, filmes);

        //verificacao
        Assert.assertThat(locacao.getValue(), is(1.0));
        PowerMockito.verifyPrivate(service).invoke("calcularValorLocacao", filmes);
    }

    @Test
    public void deveCalcularValorLocacao() throws Exception {
        //cenario
        List<Movie> filmes = Arrays.asList(movie().builder());

        //acao
        Double valor = (Double) Whitebox.invokeMethod(service, "calcularValorLocacao", filmes);

        //verificacao
        Assert.assertThat(valor, is(4.0));
    }
}
