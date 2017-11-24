package com.oakley8sam.thriftez;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.oakley8sam.thriftez.BudgetClasses.BudgetCategory;
import com.oakley8sam.thriftez.BudgetClasses.Expenditure;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SmallTest

/**
 * Created by oakle on 11/5/2017.
 */

public class BudgetCategoryInstTest {
    public float TEST_MAX = 1000;
    public float TEST_CURR = 1000;
    public String TEST_NAME = "Rent";
    private BudgetCategory testCat;
    private Expenditure testExp1, testExp2, testExp3;

    @Before
    public void createCat(){
        testCat = new BudgetCategory();
        testExp1 = new Expenditure();
        testExp2 = new Expenditure("Rent", "June", 30, 2015, 500);
        testExp3 = new Expenditure();
    }

    @Test
    public void testBudgetCatFxns(){
        //test getters and setters
        testCat.setCurrBalance(TEST_CURR);
        testCat.setMaxBalance(TEST_MAX);
        testCat.setName(TEST_NAME);
        assertThat(testCat.getCurrBalance(), is(TEST_CURR));
        assertThat(testCat.getMaxBalance(), is(TEST_MAX));
        assertThat(testCat.getName(), is(TEST_NAME));

        //test getters and setters for expenditures
        testExp1.setNote("FOOD");
        testExp1.setYear(2012);
        testExp1.setDay(20);
        testExp1.setAmtSpent(20);
        testExp1.setMonth("January");

        assertThat(testExp1.getNote(), is ("FOOD"));
        assertThat(testExp1.getYear(), is (2012));
        assertThat(testExp1.getDay(), is(20));
        assertThat(testExp1.getAmtSpent(), is(20.f));
        assertThat(testExp1.getMonth(), is ("January"));

        /*
        testExp2.setNote("RENT");
        testExp2.setYear(2015);
        testExp2.setDay(30);
        testExp2.setAmtSpent(500);
        testExp2.setMonth("June");
        */

        testExp3.setNote("COFFEE");
        testExp3.setYear(2010);
        testExp3.setDay(15);
        testExp3.setAmtSpent(5);
        testExp3.setMonth("AUGUST");

        //test budgetCat add
        testCat.addExpenditure(testExp1);
        testCat.addExpenditure(testExp2);
        testCat.addExpenditure(testExp3);

        assertThat(testCat.getExpenditureList().size(), is(3));
        assertThat(testCat.getMaxBalance(), is(TEST_MAX));
        assertThat(testCat.getCurrBalance(), is(TEST_CURR - testExp1.getAmtSpent() - testExp2.getAmtSpent() - testExp3.getAmtSpent()));

        //test toString
        String toStringTest = testCat.toString();
        assert(toStringTest.toLowerCase().contains(testExp1.getNote().toLowerCase()));
        assert(toStringTest.toLowerCase().contains(testExp2.getNote().toLowerCase()));
        assert(toStringTest.toLowerCase().contains(testExp3.getNote().toLowerCase()));

    }
}
