package com.oakley8sam.thriftez;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.oakley8sam.thriftez.BudgetClasses.Budget;
import com.oakley8sam.thriftez.BudgetClasses.BudgetCategory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class BudgetInstTest {
    public float TEST_MAX = 1000;
    public float TEST_CURR = 1000;
    private Budget budget;
    private BudgetCategory testCat1, testCat2, testCat3;

    @Before
    public void createBudget(){
        budget = new Budget();

        testCat1 = new BudgetCategory("Rent", 500);
        testCat2 = new BudgetCategory("Food", 300);
        testCat3 = new BudgetCategory("Books", 1000);
    }

    @Test
    public void testBudgetFxns(){
        //test budget getters and setters
        budget.setTotalCurrFunds(TEST_CURR);
        budget.setTotalMaxFunds(TEST_MAX);
        assertThat(budget.getTotalCurrFunds(), is(TEST_CURR));
        assertThat(budget.getTotalMaxFunds(), is(TEST_MAX));

        //test addCat and update funds
        budget.addCategory(testCat1);
        budget.addCategory(testCat2);
        budget.addCategory(testCat3);
        float test = 1800;
        assertThat(budget.getCatList().size(), is(3));
        assertThat(budget.getTotalCurrFunds(), is(test));
        assertThat(budget.getTotalMaxFunds(), is(test));

        //test removeCat and update funds
        budget.removeCategory(testCat1);
        test = 1300;
        assertThat(budget.getCatList().size(), is(2));
        assertThat(budget.getTotalCurrFunds(), is(test));
        assertThat(budget.getTotalMaxFunds(), is(test));

        //test editCat and update funds
        budget.editCategory(testCat2, 200);
        test = 1200;
        assertThat(budget.getCatList().size(), is(2));
        assertThat(budget.getTotalCurrFunds(), is(test));
        assertThat(budget.getTotalMaxFunds(), is(test));

        //test toString
        String toStringTest = budget.toString();
        assert(toStringTest.toLowerCase().contains(testCat2.getName().toLowerCase()));
        assert(toStringTest.toLowerCase().contains(testCat3.getName().toLowerCase()));

    }

}
