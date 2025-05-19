package User;

import prices.InvalidPriceException;
import trade.*;

import java.util.Random;
import java.util.ArrayList;
import java.util.TreeMap;


public final class ProductManager {
    private static TreeMap<String, ProductBook> productBooks = new TreeMap<>();

    private static ProductManager instance;

    public static ProductManager getInstance(){
        if (instance == null)
            instance = new ProductManager();

        return instance;

    }

    private  ProductManager()
    {

    }


    public void addProduct(String symbol) throws InvalidDataException, InvalidOrderException{
        if(symbol == null){
            throw new InvalidDataException("Invalid symbol");
        }
        try{
            getProductBook(symbol);
        } catch (InvalidDataException e) {
            ProductBook productBook =  new ProductBook(symbol);
            productBooks.put(symbol, productBook);
        }
        productBooks.put(symbol,getProductBook(symbol));

    }

    public  ProductBook getProductBook(String symbol) throws InvalidDataException{
        if(productBooks.get(symbol) == null){
            throw new InvalidDataException("DNE");
        }
        return productBooks.get(symbol);
    }

    public String getRandomProduct(){
        Random random = new Random();
        int randIndex = random.nextInt(productBooks.size());
        ArrayList<ProductBook> productBookList = new ArrayList<>(productBooks.values());
        return productBookList.get(randIndex).getProduct();
    }

    public TradableDTO addTradable(Tradable o) throws InvalidOrderException, InvalidPriceException, InvalidDataException {
        String symbol = o.getProduct();
        ProductBook productBook = productBooks.get(symbol);
        UserManager.updateTradable(o.getUser(), o.makeTradableDTO());
        return productBook.add(o);
    }

    public TradableDTO[] addQuote(Quote q) throws InvalidOrderException, InvalidPriceException, InvalidDataException {
        String symbol = q.getSymbol();
        ProductBook productBook = productBooks.get(symbol);
        productBook.removeQuotesForUser(q.getUser());
        TradableDTO buy = productBook.add(q.getQuoteSide(BookSide.BUY));
        TradableDTO sell = productBook.add(q.getQuoteSide(BookSide.SELL));
        return new TradableDTO[]{buy,sell};
    }

    public TradableDTO cancel(TradableDTO o){
        String symbol = o.product();
        ProductBook productBook = productBooks.get(symbol);
        try {
            return productBook.cancel(o.side(),o.tradableId());
        } catch (Exception e) {
            System.out.println("Failure to Cancel");
        }
        return null;
    }

    public TradableDTO[] cancelQuote(String symbol, String user) throws InvalidOrderException, InvalidDataException{
        if(symbol == null || user == null){
            throw new InvalidDataException("Invaid Symbol");
        }

        ProductBook productBook = productBooks.get(symbol);
        if(productBook == null){
            throw new InvalidDataException(("No ProductBook"));
        }
        return productBook.removeQuotesForUser(user);

    }





    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (ProductBook productBook : productBooks.values()) {
            s.append("Product Book: ").append(productBook.getProduct());
            s.append("\n");
            s.append(productBook);
        }
        return s.toString();
    }

}
