package hw1;

import java.util.*;

public class CashRegister {

    /**
     * This class contains the methods that must be implemented.
     * You must decide what data structures would work best in this class
     * to support them. You must create the constructor that initialize
     * those data structures and any other class variables.
     */

    /**
     * Records the sale of an item for some amount.
     * The purchase amount should include the cost of this item.
     *
     * In the case of a refund, amount will be negative.
     */
    public void recordPurchase(double amount) {
        /* Implement this */
    }

    /**
     * Enters the payment received from the customer. Returns true if the payment is accepted.
     * If the payment is not enough to cover the current purchase amount, the payment should not be accepted.
     */
    public boolean enterPayment(ArrayList<Currency> currency) {
        /* Implement this */
        return false;
    }

    /**
     * Adds the amount to the cash register based on what's in the map.
     * So a map containing one entry with key Penny and value 100 would
     * add $1 to the cash register.
     * @param moneyCount
     */
    public void addMoney(HashMap<Currency, Integer> moneyCount) {
        /* Implement this */
    }

    /**
     * Returns the amount owed using as few currency items as possible.
     * The amount owed is the payment amount - the total purchase.
     *
     * So if the customer is owed 25 cents, return a quarter.
     * So if the customer is owed 24 cents, return two dimes and four pennies.
     * So if the customer is owed $2.15, return two dollars, one dime, and one nickel.
     * But the change can only be made based on what it is in the cash register.
     * So if the cash register only has two $20 bills and fifty quarters the customer is owed
     * $5, it would return 20 quarters.
     *
     * If the payment was less than the purchase amount, this method should throw an exception.
     * If the cash register does not have enough money to make change, this method should thrown an exception.
     *
     * Tip: Note that doubles are not always exact numbers. When comparing two doubles,
     * instead of doing x == 1.0, use Math.abs(x - 1.0) < DELTA. And DELTA should be no bigger than 1e-9 or 0.000000001
     */
    public ArrayList<Currency> giveChange() {
        /* Implement this */
        return new ArrayList<Currency>();
    }

    /**
     * Returns the number of currency items with the given type.
     * So a cash register with 12 one dollars bills, 8 five dollar bills, and 3 twenty dollar bills
     * would return 23 for type = BILL.
     */
    public int getCount(Currency.Type type) {
        /* Implement this */
        return 0;
    }

    /**
     * Returns the total amount of money in the cash register.
     */
    public double getTotal() {
        /* Implement this */
        return 0.0;
    }

    /**
     * Should print the contents of the cash register and the total in the following format:
     *
     * This cash register has
     * 58 pennies
     * 12 nickels
     * 20 dimes
     * 18 quarters
     * 22 one dollar bills
     * 14 five dollar bills
     * 17 twenty dollar bills
     * 1 hundred dollar bill
     * Total: $639.68
     *
     * All of the contents should be printed., If there are no instances of a certain bill or coin in the cash register,
     * it should not be included in the string. Note the example has no ten or fifty dollar bills.
     * The list starts with the smallest coin and goes up to the most valuable bill.
     * If the coin or bill has a count of 1, then it's name should be singular
     * (see "1 hundred dollar bill" in the example).
     * These are the only currency you should be concerned with. In this world, 50-cent and dollar coins don't exist.
     *
     * Helpful method:
     * String.format(String format, Object... args)
     * Collections.sort(List<T> list, Comparator<? super T> c)
     */
    @Override
    public String toString() {
        /* Implement this */
        return "";
    }
}
