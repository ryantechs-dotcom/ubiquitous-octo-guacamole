package trade;


import User.InvalidDataException;
import prices.*;

public class ProductBook {
    private String product;
    private final ProductBookSide buySide;
    private final ProductBookSide sellSide;
    public ProductBook(String product) throws InvalidOrderException {
        setProduct(product);
        this.buySide = new ProductBookSide(BookSide.BUY);
        this.sellSide = new ProductBookSide(BookSide.SELL);
    }

    public String getProduct() {
        return product;
    }

    private void setProduct(String product) throws InvalidOrderException {
        if(product.length() > 5 || product.isEmpty()){
            throw new InvalidOrderException("incorrect product format");
        }
        String newProduct = product.replace(".", "");
        for(char x : newProduct.toCharArray()){
            if(!Character.isAlphabetic(x) || Character.isDigit(x)){
                throw new InvalidOrderException("Incorrect product format");
            }
        }
        this.product = product;
    }

    public TradableDTO add(Tradable t) throws InvalidOrderException, InvalidPriceException, InvalidDataException {
        TradableDTO tradable = null;
        System.out.println("**ADD: " + t);
        if(t == null){
            throw new InvalidOrderException("invalid tradable");
        }
        if(t.getSide() == BookSide.BUY){
            tradable = buySide.add(t);

        } else if (t.getSide() == BookSide.SELL) {
            tradable = sellSide.add(t);
        }
        this.tryTrade();
        return tradable;


    }

    public TradableDTO[] add(Quote qte) throws InvalidOrderException, InvalidPriceException, InvalidDataException {
        removeQuotesForUser(qte.getUser());
        TradableDTO buy = buySide.add(qte.getQuoteSide(BookSide.BUY));
        TradableDTO sell = sellSide.add(qte.getQuoteSide(BookSide.SELL));
        this.tryTrade();
        System.out.println("**ADD: " + qte.getQuoteSide(BookSide.BUY).toString());
        System.out.println("**ADD: " + qte.getQuoteSide(BookSide.SELL).toString());
        return new TradableDTO[]{buy,sell};

    }

    public TradableDTO cancel(BookSide side, String orderId) throws InvalidDataException{
        TradableDTO canceledOrder = null;
        if(side == BookSide.BUY){
            canceledOrder = buySide.cancel(orderId);
        } else if (side == BookSide.SELL) {
            canceledOrder =  sellSide.cancel(orderId);
        }

        return canceledOrder;
    }

    public TradableDTO[] removeQuotesForUser(String userName) throws InvalidOrderException, InvalidDataException{
        TradableDTO buy = null;
        TradableDTO sell = null;

        buy = buySide.removeQuotesForUser(userName);
        sell = sellSide.removeQuotesForUser(userName);

        return new TradableDTO[]{buy,sell};
    }

    public void tryTrade() throws InvalidPriceException, InvalidDataException{
        Price topBuyPrice = buySide.topOfBookPrice();
        Price topSellPrice = sellSide.topOfBookPrice();
        if(topBuyPrice == null || topSellPrice == null){
            return;
        }
        int totalToTrade = Math.max(buySide.topOfBookVolume(), sellSide.topOfBookVolume());
        while(totalToTrade > 0) {
            topBuyPrice = buySide.topOfBookPrice();
            topSellPrice = sellSide.topOfBookPrice();
            if(topBuyPrice == null || topSellPrice == null){
                return;
            }
            if (topSellPrice.lessOrEqual(topBuyPrice)) {
                int toTrade = Math.min(buySide.topOfBookVolume(), sellSide.topOfBookVolume());
                buySide.tradeOut(topBuyPrice, toTrade);
                sellSide.tradeOut(topBuyPrice, toTrade);
                totalToTrade -= toTrade;
            } else{
                return;
            }
        }
    }

    public String getTopOfBookString(BookSide side){
        if(this.buySide.topOfBookPrice() == null && side == BookSide.BUY){
            return "Top of BUY book: $0.00 x 0";
        }
        if(this.sellSide.topOfBookPrice() == null && side == BookSide.SELL){
            return "Top of SELL book: $0.00 x 0";
        }
        if(side == BookSide.BUY){
            return(this.buySide.topOfBookPrice().toString() + " x " + this.buySide.topOfBookVolume());
        }else{
            return(this.sellSide.topOfBookPrice().toString() + " x " + this.sellSide.topOfBookVolume());
        }
    }

    @Override
    public String toString() {
        String result = "Product: " + this.product + "\n" +
                buySide.toString() + "\n" +
                sellSide.toString();
        return result;
    }
}
