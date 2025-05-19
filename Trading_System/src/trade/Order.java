package trade;


import prices.*;

public class Order implements Tradable {
    private String user;
    private String product;
    private Price price;
    private BookSide side;
    private int originalVolume;
    private int remainingVolume;
    private int cancelledVolume = 0;
    private int filledVolume = 0;
    private final String id;

    public Order(String user, String product, Price price,int originalVolume, BookSide side) throws InvalidOrderException {
        setUser(user);
        setProduct(product);
        setPrice(price);
        setSide(side);
        setOriginalVolume(originalVolume);
        setRemainingVolume(originalVolume);
        this.id = user + product + price + System.nanoTime();
    }


    @Override
    public String getUser() {
        return user;
    }

    private void setUser (String user) throws InvalidOrderException {
        if(user.length() != 3){
            throw new InvalidOrderException("incorrect userID format");
        }
        for(char x : user.toCharArray()){
            if(!Character.isAlphabetic(x)){
                throw new InvalidOrderException("Incorrect userID format");
            }
        }
        this.user = user;
    }

    @Override
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

    @Override
    public Price getPrice() {
        return price;
    }

    private void setPrice(Price price)throws InvalidOrderException {
        if(price == null){
            throw new InvalidOrderException("invalid Price");
        }
        this.price = price;
    }

    @Override
    public BookSide getSide() {
        return side;
    }

    private void setSide(BookSide side) {
        this.side = side;
    }

    @Override
    public int getOriginalVolume() {
        return originalVolume;
    }

    private void setOriginalVolume(int originalVolume) throws InvalidOrderException {
        if(originalVolume <= 0 || originalVolume >= 10000){
            throw new InvalidOrderException("Invalid Volume");
        }
        this.originalVolume = originalVolume;
    }

    @Override
    public int getRemainingVolume() {
        return remainingVolume;
    }

    @Override
    public void setRemainingVolume(int remainingVolume) {
        this.remainingVolume = remainingVolume;
    }

    @Override
    public int getCancelledVolume() {
        return cancelledVolume;
    }

    @Override
    public void setCancelledVolume(int cancelledVolume) {
        this.cancelledVolume = cancelledVolume;
    }

    @Override
    public int getFilledVolume() {
        return filledVolume;
    }

    @Override
    public void setFilledVolume(int filledVolume) {
        this.filledVolume = filledVolume;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public TradableDTO makeTradableDTO() {
        return new TradableDTO(this.user, this.product, this.price, this.originalVolume,
        this.remainingVolume, this.cancelledVolume,
        this.filledVolume, this.side, this.id);
    }

    @Override
    public String toString(){

        return getUser() + " " + getSide() + " order: " + getProduct() + " at " + getPrice().toString() + ", Orig Vol: " +
                getOriginalVolume() + ", Rem Vol: " + getRemainingVolume() + ",Fill Vol: "+ getFilledVolume()+
                ", CXL Vol: " + getCancelledVolume() + ", ID: " + getId();
    }
}


