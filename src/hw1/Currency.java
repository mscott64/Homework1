package hw1;

public abstract class Currency {
    enum Type {COIN, BILL};

    private double value; // The value of the currency in dollars. So a quarter has a value of .25
    private Type type;

    protected Currency() {
        this.value = 0.0;
        this.type = Type.COIN;
    }

    public Currency(double value, Type type) {
        this.value = value;
        this.type = type;
    }

    public double getValue() {
        return this.value;
    }

    public Type getType() {
        return this.type;
    }

    public abstract String getName(); // Returns the name associated with currency (e.g. penny or one dollar bill)

    // Returns the name for multiple of this currency (e.g. nickels or one dollar bills)
    public String getNamePlural() {
        return getName().concat("s");
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Currency currency = (Currency) o;

        if (Double.compare(currency.getValue(), value) != 0) return false;
        return type == currency.getType();

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(value);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + type.hashCode();
        return result;
    }

    public static class Penny extends Currency {
        public Penny() {
            super(0.01, Currency.Type.COIN);
        }

        @Override
        public String getName() {
            return "penny";
        }

        @Override
        public String getNamePlural() {
            return "pennies";
        }

        public static Penny get() {
            return new Penny();
        }
    }

    public static class Nickel extends Currency {
        public Nickel() {
            super(0.05, Currency.Type.COIN);
        }

        @Override
        public String getName() {
            return "nickel";
        }

        public static Nickel get() {
            return new Nickel();
        }
    }

    public static class Dime extends Currency {
        public Dime() {
            super(0.1, Currency.Type.COIN);
        }

        @Override
        public String getName() {
            return "dime";
        }

        public static Dime get() {
            return new Dime();
        }
    }

    public static class Quarter extends Currency {
        public Quarter() {
            super(0.25, Currency.Type.COIN);
        }

        @Override
        public String getName() {
            return "quarter";
        }

        public static Quarter get() {
            return new Quarter();
        }
    }

    public static class OneDollar extends Currency {
        public OneDollar() {
            super(1.0, Currency.Type.BILL);
        }

        @Override
        public String getName() {
            return "one dollar bill";
        }

        public static OneDollar get() {
            return new OneDollar();
        }
    }

    public static class FiveDollar extends Currency {
        public FiveDollar() {
            super(5.0, Currency.Type.BILL);
        }

        @Override
        public String getName() {
            return "five dollar bill";
        }

        public static FiveDollar get() {
            return new FiveDollar();
        }
    }

    public static class TenDollar extends Currency {
        public TenDollar() {
            super(10.0, Currency.Type.BILL);
        }

        @Override
        public String getName() {
            return "ten dollar bill";
        }

        public static TenDollar get() {
            return new TenDollar();
        }
    }

    public static class TwentyDollar extends Currency {
        public TwentyDollar() {
            super(20.0, Currency.Type.BILL);
        }

        @Override
        public String getName() {
            return "twenty dollar bill";
        }

        public static TwentyDollar get() {
            return new TwentyDollar();
        }
    }

    public static class FiftyDollar extends Currency {
        public FiftyDollar() {
            super(50.0, Currency.Type.BILL);
        }

        @Override
        public String getName() {
            return "fifty dollar bill";
        }

        public static FiftyDollar get() {
            return new FiftyDollar();
        }
    }

    public static class HundredDollar extends Currency {
        public HundredDollar() {
            super(100.0, Currency.Type.BILL);
        }

        @Override
        public String getName() {
            return "hundred dollar bill";
        }

        public static HundredDollar get() {
            return new HundredDollar();
        }
    }
}
