package br.com.codeup.service;

import br.com.codeup.exceptions.CannotDivideZeroException;
import org.junit.*;

public class CalculatorTest {

    public static StringBuffer order = new StringBuffer();

    private Calculadora calc;

    @Before
    public void setup() {
        calc = new Calculadora();
        System.out.println("iniciando...");
        order.append("1");
    }

    @After
    public void tearDown() {
        System.out.println("finalizando...");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println(order.toString());
    }

    @Test
    public void mustAddTwoValues() {
        //cenario
        int a = 5;
        int b = 3;

        //acao
        int result = calc.somar(a, b);

        //verificacao
        Assert.assertEquals(8, result);
    }

    @Test
    public void mustSubtractTwoValues() {
        //cenario
        int a = 8;
        int b = 5;

        //acao
        int result = calc.subtrair(a, b);

        //verificacao
        Assert.assertEquals(3, result);
    }

    @Test
    public void mustSplitTwoValues() throws CannotDivideZeroException {
        //cenario
        int a = 6;
        int b = 3;

        //acao
        int result = calc.divide(a, b);

        //verificacao
        Assert.assertEquals(2, result);
    }

    @Test(expected = CannotDivideZeroException.class)
    public void shouldThrowExceptionWhenDividingByZero() throws CannotDivideZeroException {
        int a = 10;
        int b = 0;

        calc.divide(a, b);
    }

    @Test
    public void mustSplit() {
        String a = "6";
        String b = "3";

        int result = calc.divide(a, b);

        Assert.assertEquals(2, result);
    }

}