package br.com.codeup.matchers;

import br.com.codeup.utils.DateUtil;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataDiferencaDiasMatcher extends TypeSafeMatcher<Date> {

    private Integer qtdDias;

    public DataDiferencaDiasMatcher(Integer qtdDias) {
        this.qtdDias = qtdDias;
    }

    public void describeTo(Description desc) {
        Date dataEsperada = DateUtil.getDateWithDaysDifference(qtdDias);
        DateFormat format = new SimpleDateFormat("dd/MM/YYYY");
        desc.appendText(format.format(dataEsperada));
    }

    @Override
    protected boolean matchesSafely(Date data) {
        return DateUtil.isSameDate(data, DateUtil.getDateWithDaysDifference(qtdDias));
    }

}
