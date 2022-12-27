package com.mindhub.homebanking.models;

import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class CardUtilsTests {

    @Test
    public void cardNumberIsCreated(){
        String cardNumber = CardUtils.getCardNumber();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    public void cardNumberHasSeparators() {
        String cardNumber = CardUtils.getCardNumber();
        assertThat(cardNumber, containsString("-"));
    }

    @Test
    public void cvvLessThan1000() {
        int cvv = CardUtils.getCvv();
        assertThat(cvv, lessThan(1000));
    }

    @Test
    public void cvvMoreThan99() {
        int cvv = CardUtils.getCvv();
        assertThat(cvv, greaterThan(99));
    }
}
