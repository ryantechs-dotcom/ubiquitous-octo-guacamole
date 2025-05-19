package trade;

import User.InvalidDataException;
import User.UserManager;
import prices.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class ProductBookSide {
    BookSide side;
    private final TreeMap<Price, ArrayList<Tradable>> bookEntries;


    public ProductBookSide(BookSide side) throws InvalidOrderException {
        setSide(side);
        if(side == BookSide.BUY) {
            this.bookEntries = new TreeMap<>(Collections.reverseOrder());
        }else{
            this.bookEntries = new TreeMap<>();
        }
    }

    public BookSide getSide() {
        return side;
    }

    private void setSide(BookSide side) throws InvalidOrderException {
        if (side == null){
            throw new InvalidOrderException("Invalid");
        }
        this.side = side;
    }

    public TradableDTO add(Tradable o) throws InvalidDataException{
        if(!bookEntries.containsKey(o.getPrice())){
            ArrayList<Tradable> tradableArrayList = new ArrayList<Tradable>();
            tradableArrayList.add(o);
            bookEntries.put(o.getPrice(),tradableArrayList);
        }else
        {
            bookEntries.get(o.getPrice()).add(o);
        }
        UserManager.updateTradable(o.getUser(), o.makeTradableDTO());
        return new TradableDTO(o);
    }

    public TradableDTO cancel (String tradableId) throws InvalidDataException {
        Price removedArrayPrice = null;
        int removedIndex = 0;
        Tradable removed = null;
        if(!bookEntries.isEmpty()) {
            for (Price key : bookEntries.keySet()) {
                ArrayList<Tradable> value = bookEntries.get(key);
                for (int i = 0; i < value.size(); i++) {
                    if (value.get(i).getId().equals(tradableId)) {
                        System.out.println("**CANCEL: " + value.get(i));
                        removed = value.get(i);
                        removedArrayPrice = key;
                        removedIndex = i;
                        break;
                    }
                }
            }
            if(removedArrayPrice != null && removed  != null) {
                bookEntries.get(removedArrayPrice).remove(removedIndex);
                if (bookEntries.get(removedArrayPrice).isEmpty()) {
                    bookEntries.remove(removedArrayPrice);
                }
            }
        }
        if(removed == null){
            return null;
        }
        removed.setCancelledVolume(removed.getCancelledVolume()+removed.getRemainingVolume());
        removed.setRemainingVolume(0);
        UserManager.updateTradable(removed.getUser(), removed.makeTradableDTO());

        return removed.makeTradableDTO();
    }

    public TradableDTO removeQuotesForUser(String userName) throws InvalidDataException{
        TradableDTO removed = null;
        String canceled = "";
        if(!bookEntries.isEmpty()) {
            for (Price key : bookEntries.keySet()) {
                ArrayList<Tradable> value = bookEntries.get(key);
                for (Tradable tradable : value) {
                    if (tradable.getUser().equals(userName)) {
                        canceled = tradable.getId();
                        removed = tradable.makeTradableDTO();
                        break;
                    }
                }
            }
        }
        cancel(canceled);
        return removed;
    }

    public Price topOfBookPrice(){
        if(bookEntries.isEmpty()){
            return null;
        }
        return bookEntries.firstEntry().getKey();
    }

    public int topOfBookVolume(){
        if(bookEntries.isEmpty()){
            return 0;
        }
        int totalVolume = 0;
        Price firstKey = bookEntries.firstKey();
        ArrayList<Tradable> volumes = bookEntries.get(firstKey);
        for(Tradable trade : volumes){
            totalVolume += trade.getRemainingVolume();
        }
        return totalVolume;
    }

    public void tradeOut(Price price, int vol) throws InvalidPriceException, InvalidDataException {
        int totalVolAtPrice = 0;
        Price topPrice = bookEntries.firstKey();
        if(topPrice == null){
            return;
        }
        ArrayList<Tradable> atPrice = bookEntries.get(topPrice);
        if(!topPrice.greaterThan(price)){
            for(Tradable x: atPrice){
                totalVolAtPrice += x.getRemainingVolume();
            }
        }
        if(vol >= totalVolAtPrice){
            for(Tradable t: atPrice){
               int rv =  t.getRemainingVolume();
               t.setFilledVolume(t.getOriginalVolume());
               t.setRemainingVolume(0);
                String formattedString = String.format(
                        "FULL FILL: (%s %d) %s %s order: %s at %s, Orig Vol: %d, Rem Vol: %d, Fill Vol: %d, CXL Vol: %d, ID: %s",
                        t.getSide(), t.getOriginalVolume(), t.getUser(), t.getSide(), t.getProduct(),
                        t.getPrice(), t.getOriginalVolume(), t.getRemainingVolume(),
                        t.getFilledVolume(), t.getCancelledVolume(), t.getId()
                );

                System.out.println(formattedString);
                UserManager.updateTradable(t.getUser(), t.makeTradableDTO());
            }
            bookEntries.remove(topPrice);

        }else{
            int remainder = vol;
            for(Tradable t: atPrice){
               double ratio = (double) t.getRemainingVolume() / totalVolAtPrice;
               int toTrade = (int)Math.ceil(vol * ratio);
               toTrade = Math.min(toTrade,remainder);
               t.setFilledVolume(t.getFilledVolume() + toTrade);
               t.setRemainingVolume(t.getRemainingVolume()-toTrade);
                String formattedString = String.format(
                        "PARTIAL FILL: (%s %d) %s %s order: %s at %s, Orig Vol: %d, Rem Vol: %d, Fill Vol: %d, CXL Vol: %d, ID: %s",
                        t.getSide(), t.getFilledVolume(), t.getUser(), t.getSide(), t.getProduct(),
                        t.getPrice(), t.getOriginalVolume(), t.getRemainingVolume(),
                        t.getFilledVolume(), t.getCancelledVolume(), t.getId()
                );
                System.out.println(formattedString);
                remainder -= toTrade;
                UserManager.updateTradable(t.getUser(), t.makeTradableDTO());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if(side == BookSide.BUY){
            result.append("Side: BUY ").append("\n");
        }
        if(side == BookSide.SELL){
            result.append("Side: SELL ").append("\n");
        }
        if(bookEntries.isEmpty() ){
           result.append("     <Empty>");
           return result.toString();
        }
        for (Price key : bookEntries.keySet()) {
            for(Tradable tradable: bookEntries.get(key)) {
                result.append("Price: ").append(tradable.toString()).append("\n").append("           ")
                        .append(tradable.getId()).append("   order: ").append(this.side).append(" ").append(tradable.getUser())
                        .append(", Orig Vol: ").append(tradable.getOriginalVolume()).append(", Rem Vol: ")
                        .append(tradable.getRemainingVolume()).append(", Fill Vol: ").append(tradable.getFilledVolume())
                        .append(", CXL Vol: ").append(tradable.getCancelledVolume()).append(", ID: ")
                        .append(tradable.getId()).append("\n");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
