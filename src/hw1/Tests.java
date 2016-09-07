package hw1;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class Tests {
    /**
     * These are some example tests. I will run more tests than this when determining your grade.
     * Feel free to change this file and add your own test cases.
     */

    private static final double DELTA = 1e-8;
    private CashRegister cr;
    private static final HashMap<Currency, Integer> MONEY_MAP = new HashMap<Currency, Integer>();
    static {
        MONEY_MAP.put(Currency.TwentyDollar.get(), 20); // $400
        MONEY_MAP.put(Currency.FiveDollar.get(), 20); // $100
        MONEY_MAP.put(Currency.OneDollar.get(), 25); // $25
        MONEY_MAP.put(Currency.Quarter.get(), 80); // $20
        MONEY_MAP.put(Currency.Nickel.get(), 50); // $2.50
    }
    private static final double MONEY_MAP_TOTAL = 547.5;
    private static final int MONEY_MAP_COIN_COUNT = 130;
    private static final int MONEY_MAP_BILL_COUNT = 65;

    @Before
    public void setUp() {
        cr = new CashRegister();
    }

    @Test
    public void testCreation() throws Exception {
        assertEquals(0.0, cr.getTotal(), DELTA);
        assertEquals(0, cr.getCount(Currency.Type.COIN));
        assertEquals(0, cr.getCount(Currency.Type.BILL));
    }

    @Test
    public void testAddMoney() throws Exception {
        cr.addMoney(MONEY_MAP);
        assertEquals(MONEY_MAP_TOTAL, cr.getTotal(), DELTA);
        assertEquals(MONEY_MAP_COIN_COUNT, cr.getCount(Currency.Type.COIN));
        assertEquals(MONEY_MAP_BILL_COUNT, cr.getCount(Currency.Type.BILL));
        cr.addMoney(MONEY_MAP);
        assertEquals(MONEY_MAP_TOTAL*2.0, cr.getTotal(), DELTA);
        assertEquals(MONEY_MAP_COIN_COUNT*2, cr.getCount(Currency.Type.COIN));
        assertEquals(MONEY_MAP_BILL_COUNT*2, cr.getCount(Currency.Type.BILL));
    }

    @Test
    public void testGiveChange() throws Exception {
        ArrayList<Currency> payment = new ArrayList<Currency>();
        ArrayList<Currency> change = new ArrayList<Currency>();
        cr.addMoney(MONEY_MAP);

        // Purchase - $19.95 Payment - $20 Change - $0.05
        double purchase1 = 19.95;
        cr.recordPurchase(purchase1);
        payment.add(Currency.TwentyDollar.get());
        assertEquals(true, cr.enterPayment(payment));
        change.add(Currency.Nickel.get());
        assertEquals(change, cr.giveChange());
        assertEquals(MONEY_MAP_TOTAL + purchase1, cr.getTotal(), DELTA);
        assertEquals(MONEY_MAP_COIN_COUNT - 1, cr.getCount(Currency.Type.COIN));
        assertEquals(MONEY_MAP_BILL_COUNT + 1, cr.getCount(Currency.Type.BILL));

        // Purchase - $15.85 Payment - $20 Change - $4.15
        payment.clear();
        change.clear();
        double purchase2a = 5.85;
        cr.recordPurchase(purchase2a);
        double purchase2b = 10.00;
        cr.recordPurchase(purchase2b);
        double purchase2 = purchase2a + purchase2b;
        try {
            cr.giveChange();
            fail("Should not be able to make change when payment is less than the purchase amount.");
        } catch (Exception expected) {}

        payment.add(Currency.TenDollar.get());
        assertEquals(false, cr.enterPayment(payment));
        try {
            cr.giveChange();
            fail("Should not be able to make change when payment is less than the purchase amount.");
        } catch (Exception expected) {
            // The total didn't change because the payment wasn't accepted.
            assertEquals(MONEY_MAP_TOTAL + purchase1, cr.getTotal(), DELTA);
            assertEquals(MONEY_MAP_BILL_COUNT + 1, cr.getCount(Currency.Type.BILL));
        }
        payment.add(Currency.TenDollar.get());
        assertEquals(true, cr.enterPayment(payment));
        for (int i = 0; i < 4; i++) {
            // $4 in change
            change.add(Currency.OneDollar.get());
        }
        // 15 cents in change using only what's in the cash register (no dimes)
        for (int i = 0; i < 3; i++) {
            change.add(Currency.Nickel.get());
        }
        assertEquals(change, cr.giveChange());
        assertEquals(MONEY_MAP_TOTAL + purchase1 + purchase2, cr.getTotal(), DELTA);
        assertEquals(MONEY_MAP_COIN_COUNT - 1 /* previous transaction */ - 3, cr.getCount(Currency.Type.COIN));
        assertEquals(MONEY_MAP_BILL_COUNT + 1 /* previous transaction */ + 2 - 4, cr.getCount(Currency.Type.BILL));

        // Purchase - $400 Payment - $1000 Change - $600
        payment.clear();
        change.clear();
        cr.recordPurchase(400.0);
        for (int i = 0; i < 10; i++) {
            payment.add(Currency.HundredDollar.get());
        }
        assertEquals(true, cr.enterPayment(payment));
        assertEquals(MONEY_MAP_TOTAL + purchase1 + purchase2 + 1000.0, cr.getTotal(), DELTA);
        assertEquals(MONEY_MAP_BILL_COUNT - 1 /* previous transactions */ + 10, cr.getCount(Currency.Type.BILL));
        for (int i = 0; i < 6; i++) {
            change.add(Currency.HundredDollar.get());
        }
        assertEquals(change, cr.giveChange());
        assertEquals(MONEY_MAP_TOTAL + + purchase1 + purchase2 + 400.0, cr.getTotal(), DELTA);
        assertEquals(MONEY_MAP_COIN_COUNT - 4 /* previous transactions */, cr.getCount(Currency.Type.COIN));
        assertEquals(MONEY_MAP_BILL_COUNT - 1 /* previous transactions */ + 4, cr.getCount(Currency.Type.BILL));
    }

    @Test
    public void testUnableToMakeChange() throws Exception {
        HashMap<Currency, Integer> littleMoneyMap = new HashMap<Currency, Integer>();
        littleMoneyMap.put(Currency.Penny.get(), 500);
        cr.addMoney(littleMoneyMap);
        assertEquals(5.0, cr.getTotal(), DELTA);
        assertEquals(500, cr.getCount(Currency.Type.COIN));
        assertEquals(0, cr.getCount(Currency.Type.BILL));

        // Need more money to make change
        cr.recordPurchase(1.0);
        cr.recordPurchase(2.0);
        cr.recordPurchase(1.0);
        ArrayList<Currency> payment = new ArrayList<Currency>();
        payment.add(Currency.TenDollar.get());
        assertEquals(true, cr.enterPayment(payment));
        try {
            cr.giveChange();
            fail("This cash register does not contain enough money to make change.");
        } catch (Exception expected) {
            assertEquals(15.0, cr.getTotal(), DELTA);
            assertEquals(500, cr.getCount(Currency.Type.COIN));
            assertEquals(1, cr.getCount(Currency.Type.BILL));
        }

        littleMoneyMap.clear();
        littleMoneyMap.put(Currency.OneDollar.get(), 1);
        cr.addMoney(littleMoneyMap);
        assertEquals(16.0, cr.getTotal(), DELTA);
        assertEquals(500, cr.getCount(Currency.Type.COIN));
        assertEquals(2, cr.getCount(Currency.Type.BILL));

        ArrayList<Currency> change = new ArrayList<Currency>();
        change.add(Currency.OneDollar.get());
        for (int i = 0; i < 500; i++) {
            change.add(Currency.Penny.get());
        }
        assertEquals(change, cr.giveChange());
        assertEquals(10.0, cr.getTotal(), DELTA);
        assertEquals(0, cr.getCount(Currency.Type.COIN));
        assertEquals(1, cr.getCount(Currency.Type.BILL));

        // Need smaller currency to make change
        payment.clear();
        change.clear();
        cr.recordPurchase(4.0);
        payment.add(Currency.TenDollar.get());
        assertEquals(true, cr.enterPayment(payment));
        try {
            cr.giveChange();
            fail("This cash register does not have small enough bills to make change.");
        } catch (Exception expected) {
            assertEquals(20.0, cr.getTotal(), DELTA);
            assertEquals(0, cr.getCount(Currency.Type.COIN));
            assertEquals(2, cr.getCount(Currency.Type.BILL));
        }
        littleMoneyMap.clear();
        littleMoneyMap.put(Currency.OneDollar.get(), 5);
        littleMoneyMap.put(Currency.Quarter.get(), 12);
        cr.addMoney(littleMoneyMap);
        assertEquals(28.0, cr.getTotal(), DELTA);
        assertEquals(12, cr.getCount(Currency.Type.COIN));
        assertEquals(7, cr.getCount(Currency.Type.BILL));
        for (int i = 0; i < 5; i++) {
            change.add(Currency.OneDollar.get());
        }
        for (int i = 0; i < 4; i++) {
            change.add(Currency.Quarter.get());
        }
        assertEquals(change, cr.giveChange());
        assertEquals(22.0, cr.getTotal(), DELTA);
        assertEquals(8, cr.getCount(Currency.Type.COIN));
        assertEquals(2, cr.getCount(Currency.Type.BILL));
    }

    @Test
    public void testToString() throws Exception {
        String emptyCashRegister = "This cash register has\nTotal: $0.00\n";
        assertEquals(emptyCashRegister, cr.toString());

        String moneyMapString = "This cash register has\n" +
                "50 nickels\n80 quarters\n25 one dollar bills\n20 five dollar bills\n20 twenty dollar bills\n" +
                "Total: $547.50\n";
        cr.addMoney(MONEY_MAP);
        assertEquals(moneyMapString, cr.toString());

        HashMap<Currency, Integer> moneyMapCopy = new HashMap<Currency, Integer>(MONEY_MAP);
        moneyMapCopy.put(Currency.FiftyDollar.get(), 1);
        cr.addMoney(moneyMapCopy);
        moneyMapString = "This cash register has\n" +
                "100 nickels\n160 quarters\n50 one dollar bills\n40 five dollar bills\n" +
                "40 twenty dollar bills\n1 fifty dollar bill\n" +
                "Total: $1145.00\n";
        assertEquals(moneyMapString, cr.toString());
    }

    @Test
    public void testBadData() throws Exception {
        // Passing a null value for any of the data structures
        try {
            cr.enterPayment(null);
        } catch (Exception ex) {
            fail("Enter payment should be able to handle bad data.");
        }

        // passing a null value inside the data structures
        ArrayList<Currency> payment = new ArrayList<Currency>();
        payment.add(Currency.TenDollar.get());
        payment.add(null);
        try {
            assertEquals(true, cr.enterPayment(payment));
        } catch (Exception ex) {
            fail("Enter payment should ignore null currency.");
        }
        assertEquals(10.0, cr.getTotal(), DELTA);
    }

    @Test
    public void testRefund() throws Exception {
        cr.addMoney(MONEY_MAP);
        assertEquals(MONEY_MAP_TOTAL, cr.getTotal(), DELTA);
        assertEquals(MONEY_MAP_COIN_COUNT, cr.getCount(Currency.Type.COIN));
        assertEquals(MONEY_MAP_BILL_COUNT, cr.getCount(Currency.Type.BILL));
        cr.recordPurchase(-15.85);

        ArrayList<Currency> change = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            change.add(Currency.FiveDollar.get());
        for (int i = 0; i < 3; i++)
            change.add(Currency.Quarter.get());
        for (int i = 0; i < 2; i++)
            change.add(Currency.Nickel.get());
        assertEquals(change, cr.giveChange());
        assertEquals(MONEY_MAP_TOTAL - 15.85, cr.getTotal(), DELTA);
        assertEquals(MONEY_MAP_COIN_COUNT - 5, cr.getCount(Currency.Type.COIN));
        assertEquals(MONEY_MAP_BILL_COUNT - 3, cr.getCount(Currency.Type.BILL));
    }
}
