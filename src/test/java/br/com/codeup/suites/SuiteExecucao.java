package br.com.codeup.suites;

import br.com.codeup.service.CalculatorTest;
import br.com.codeup.service.LocationServiceTest;
import org.junit.runners.Suite.SuiteClasses;

//@RunWith(Suite.class)
@SuiteClasses({
        CalculatorTest.class,
        LocationServiceTest.class
})
public class SuiteExecucao {
    //Remova se puder!
}
