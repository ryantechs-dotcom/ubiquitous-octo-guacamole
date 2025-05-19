package prices;

import java.util.HashMap;
import java.util.Objects;



public abstract class PriceFactory {

    private static HashMap<Integer, Price> prices = new HashMap<>();




    public static Price makePrice(int value) {
        if(prices.get(value) == null){
            Price newPrice = new Price(value);
            prices.put(value, newPrice);
        }
        return prices.get(value);
    }

    public static Price makePrice(String stringValueIn) throws InvalidPriceException {

        if (Objects.equals(stringValueIn, "")) {
            throw new InvalidPriceException("Invalid price String value: " + stringValueIn );
        }

        if (containsAnyMultiples(stringValueIn)) {
            throw new InvalidPriceException("Invalid price String value: " + stringValueIn );
        }

        String newStringValueIn = stringValueIn.replaceFirst("\\$", "");
        newStringValueIn = newStringValueIn.replaceAll(",", "");

        int decimalPos = newStringValueIn.indexOf(".");

        if (decimalPos != -1) {
            int valuesAfterDecimal = newStringValueIn.length() - decimalPos - 1;

            if (valuesAfterDecimal == 0) {
                newStringValueIn = newStringValueIn + "00";
            }

            if (valuesAfterDecimal == 1 || valuesAfterDecimal > 2) {
                throw new InvalidPriceException("Invalid price String value: " + stringValueIn);
            }
            newStringValueIn = newStringValueIn.replaceFirst("\\.", "");

            try{
                Integer.parseInt(newStringValueIn);
            } catch (NumberFormatException e) {
                throw new InvalidPriceException("Invalid price String value: " + stringValueIn);
            }

            int cents = Integer.parseInt(newStringValueIn);
            return makePrice(cents);
        }

        int cents = Integer.parseInt(newStringValueIn);
        return makePrice(cents * 100);

    }


    private static boolean containsAnyMultiples(String stringValueIn) throws InvalidPriceException {
        return containsMultiple(stringValueIn, '$') || containsMultiple(stringValueIn, '.') ||
                containsMultiple(stringValueIn, '-');
    }


    private static boolean containsMultiple(String str, char x) {
        int count = 0;
        char[] newStr = str.toCharArray();
        for (char c : newStr) {
            if (c == x) {
                count += 1;
            }
        }
        return count > 1;
    }

}


