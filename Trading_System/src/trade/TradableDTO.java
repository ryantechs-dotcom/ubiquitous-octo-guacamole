package trade;

import prices.Price;

public record TradableDTO(String user, String product, Price price, int originalVolume,
                          int remainingVolume, int cancelledVolume,
                          int filledVolume, BookSide side, String tradableId) {

    public TradableDTO(Tradable tradable){
        this(tradable.getUser(), tradable.getProduct(),
                tradable.getPrice(),tradable.getOriginalVolume(),
                tradable.getRemainingVolume(), tradable.getCancelledVolume(),
                tradable.getFilledVolume(), tradable.getSide(), tradable.getId());
    }

    @Override
    public String toString(){
        return String.format(
                "Product: %s, Price: $%s, OriginalVolume: %d, RemainingVolume: %d, " +
                        "CancelledVolume: %d, FilledVolume: %d, User: %s, Side: %s, Id: %s",
                product, price, originalVolume, remainingVolume,
                cancelledVolume, filledVolume, user, side, tradableId
        );
    }
}
