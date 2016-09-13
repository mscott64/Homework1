package hw1solution;

import java.util.*;

public class CashRegister {

    /**
     * This class contains the methods that must be implemented.
     * You must decide what data structures would work best in this class
     * to support them. You must create the constructor that initializes
     * those data structures and any other class variables.
     */
    private static final double DELTA = 1e-9;

    private HashMap<Currency, Integer> moneyMap;
    private double purchaseAmount;
    private double paymentAmount;

    public CashRegister() {
        moneyMap = new HashMap<Currency, Integer>();
        purchaseAmount = 0.0;
        paymentAmount = 0.0;
    }

    /**
     * Records the sale of an item for some amount.
     * The purchase amount should include the cost of this item.
     *
     * In the case of a refund, amount will be negative.
     */
    public void recordPurchase(double amount) {
        // Add this purchase amount to the total purchase amount.
        purchaseAmount += amount;
    }

    /**
     * Enters the payment received from the customer. Returns true if the payment is accepted.
     * If the payment is not enough to cover the current purchase amount, the payment should not be accepted.
     */
    public boolean enterPayment(ArrayList<Currency> currency) {
        // Don't accept bad data
        if (currency == null) {
            return false;
        }
        // Compute payment total and create a map of the elements in the payment list
        HashMap<Currency, Integer> paymentMap = new HashMap<Currency, Integer>();
        double paymentTotal = 0.0;
        for (Currency c : currency) {
            if (c == null) {
                continue;
            }
            paymentTotal += c.getValue();
            if (paymentMap.containsKey(c)) {
                paymentMap.put(c, paymentMap.get(c) + 1);
            } else {
                paymentMap.put(c, 1);
            }
        }
        // Check that the payment was sufficient
        if ((paymentAmount + paymentTotal) < purchaseAmount && Math.abs(paymentTotal - purchaseAmount) > DELTA) {
            return false;
        }
        // Update payment variable
        paymentAmount += paymentTotal;

        // Add currency to the register's moneyMap
        addMoney(paymentMap);
        return true;
    }

    /**
     * Adds the amount to the cash register based on what's in the map.
     * So a map containing one entry with key Penny and value 100 would
     * add $1 to the cash register.
     * @param moneyCount
     */
    public void addMoney(HashMap<Currency, Integer> moneyCount) {
        // Ignore bad data
        if (moneyCount == null || moneyCount.isEmpty()) {
            return;
        }
        // Add currency to the register's moneyMap
        for (Map.Entry<Currency, Integer> entry : moneyCount.entrySet()) {
            Currency key = entry.getKey();
            // Skip entries with null values.
            if (key == null || entry.getValue() == null) {
                continue;
            }
            if (moneyMap.containsKey(key)) {
                // Add the amount in currency to the already existing count
                moneyMap.put(key, entry.getValue() + moneyMap.get(key));
            } else {
                // Add a new entry for this currency
                moneyMap.put(key, entry.getValue());
            }
        }
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
    public ArrayList<Currency> giveChange() throws Exception {
        double amountOwed = Math.abs(paymentAmount - purchaseAmount);
        // Check that there was sufficient payment.
        if (paymentAmount < purchaseAmount && amountOwed > DELTA) {
            throw new Exception("Insufficient payment");
        }

        // Sort the available currencies from largest to smallest.
        ArrayList<Currency> currencies = new ArrayList<Currency>(moneyMap.keySet());
        Collections.sort(currencies, new Comparator<Currency>() {
            @Override
            public int compare(Currency o1, Currency o2) {
                return getIntegerValue(o2) - getIntegerValue(o1);
            }
        });

        long changeAmount = Math.round(amountOwed * 100);
        // Add currency objects to the change list.
        ArrayList<Currency> change = new ArrayList<Currency>();
        for (Currency key : currencies) {
            if (changeAmount == 0) {
                break; // The necessary amount of change has been collected.
            }

            int intValue = getIntegerValue(key);
            if (intValue <= changeAmount) {
                long needed = changeAmount / intValue;
                long count = moneyMap.get(key); // Only use as many currency objects as are available.
                if (needed < count) {
                    count = needed; // Only use as many currency objects as are necessary.
                }
                for (int i = 0; i < count; i++) {
                    change.add(key);
                }
                // Reduce change amount by the amount of currency used.
                changeAmount -= intValue * count;
            }
        }
        // After going through all of the available currency objects, there wasn't enough change to
        // add up to the amountOwed, so throw an exception.
        if (changeAmount > 0) {
            throw new Exception("Unable to make change with the currency in the cash register.");
        }

        // Remove the change given from the moneyMap.
        for (Currency key : change) {
            moneyMap.put(key, moneyMap.get(key) - 1);
        }

        // Reset the purchase and payment variables as this transaction is complete.
        purchaseAmount = 0.0;
        paymentAmount = 0.0;

        return change;
    }

    /**
     * Returns the number of currency items with the given type.
     * So a cash register with 12 one dollars bills, 8 five dollar bills, and 3 twenty dollar bills
     * would return 23 for type = BILL.
     */
    public int getCount(Currency.Type type) {
        int count = 0;
        // Check each available currency item's type.
        for (Map.Entry<Currency, Integer> entry : moneyMap.entrySet()) {
            if (entry.getKey().getType() == type) {
                // Add the number of currency items to the count.
                count += entry.getValue();
            }
        }
        return count;
    }

    /**
     * Returns the total amount of money in the cash register.
     */
    public double getTotal() {
        double total = 0.0;
        for (Map.Entry<Currency, Integer> entry : moneyMap.entrySet()) {
            // Get the value of this currency item.
            double value = entry.getKey().getValue();
            // Add the total of that currency item to the cash register total.
            total += value * (double) entry.getValue();
        }
        return total;
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
        // Sort the list of currencies from smallest to largest.
        ArrayList<Currency> currencies = new ArrayList<Currency>(moneyMap.keySet());
        Collections.sort(currencies, new Comparator<Currency>() {
            @Override
            public int compare(Currency o1, Currency o2) {
                return getIntegerValue(o1) - getIntegerValue(o2);
            }
        });

        StringBuilder sb = new StringBuilder();
        // Instead of using the getTotal method, I compute the total separately here
        // since I have to iterate through the map anyway.
        double total = 0.0;

        sb.append("This cash register has\n");
        // Add a line for each currency item
        for (Currency key : currencies) {
            int count = moneyMap.get(key);
            if (count < 1) {
                continue;
            }

            // If there's more than 1, use the plural name.
            String name = count == 1 ? key.getName() : key.getNamePlural();
            sb.append(String.format("%d %s\n", count, name));
            total += key.getValue() * (double) count;
        }

        // Add a line for total.
        sb.append(String.format("Total: $%.2f\n", total));
        return sb.toString();
    }

    // Returns the number of cents for a currency object as an int.
    private static int getIntegerValue(Currency c) {
        return (int) (c.getValue() * 100);
    }
}
