package prices;

import java.util.Comparator;
import java.util.Objects;

public class Price implements Comparable<Price>{
    private final int cents;

    Price(int cents){
        this.cents = cents;
    }

    public boolean isNegative(){
        return this.cents < 0;
    }

    public Price add(Price p) throws InvalidPriceException{
        if(p == null){
            throw new InvalidPriceException("Cannot add null to a Price object");
        }
        return new Price(this.cents + p.cents);
    }

    public Price subtract(Price p) throws InvalidPriceException{
        if(p == null){
            throw new InvalidPriceException("Cannot subtract null from a Price object");
        }
        return new Price(this.cents - p.cents);
    }

    public Price multiply(int n){

        return new Price(this.cents * n);
    }

    public boolean greaterOrEqual(Price p) throws InvalidPriceException{
        if(p == null){
            throw new InvalidPriceException("Cannot check greater than or equal with a null");
        }
        return this.cents >= p.cents;
    }

    public boolean lessOrEqual(Price p) throws InvalidPriceException{
        if(p == null){
            throw new InvalidPriceException("Cannot check less than or equal with a null");
        }
        return this.cents <= p.cents;
    }

    public boolean greaterThan(Price p) throws InvalidPriceException{
        if(p == null){
            throw new InvalidPriceException("Cannot check greater than with a null");
        }
        return this.cents > p.cents;
    }

    public boolean lessThan(Price p) throws InvalidPriceException{
        if(p == null){
            throw new InvalidPriceException("Cannot check less than with a null");
        }
        return this.cents < p.cents;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return cents == price.cents;
    }

    @Override
    public int compareTo(Price p){
        if(p == null){
            return -1000000000;
        }
        return this.cents - p.cents;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cents);
    }

    @Override
    public String toString(){
        return String.format("$%,.2f", cents / 100.0);
    }
}
