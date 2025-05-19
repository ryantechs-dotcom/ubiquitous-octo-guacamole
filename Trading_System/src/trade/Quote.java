package trade;

import prices.Price;

public class Quote {
    private final String user;
    private final String product;
    private final QuoteSide buySide;
    private final QuoteSide sellSide;

    public Quote(String symbol, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume, String userName) throws InvalidOrderException {
        this.product = symbol;
        this.user = userName;
        this.buySide = new QuoteSide(userName, symbol, buyPrice, buyVolume, BookSide.BUY);
        this.sellSide = new QuoteSide(userName, symbol, sellPrice, sellVolume, BookSide.SELL);
    }

    public QuoteSide getQuoteSide(BookSide sideIn){
        if(sideIn == BookSide.BUY){
          return buySide;
        }
        return sellSide;
    }

    public String getSymbol(){
        return this.product;
    }

    public String getUser(){
        return this.user;
    }
}
