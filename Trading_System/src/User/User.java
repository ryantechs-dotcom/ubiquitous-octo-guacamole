package User;

import trade.InvalidOrderException;
import trade.TradableDTO;

import java.util.ArrayList;
import java.util.TreeMap;

public class User {
    private String userId;
    private TreeMap<String, TradableDTO> tradables = new TreeMap<>();

    public User(String userIdIn) throws InvalidDataException{
        setUserId(userIdIn);
    }

    public String getUserId() {
        return userId;
    }

    private void setUserId (String userIdIn) throws InvalidDataException {
        if(userIdIn.length() != 3){
            throw new InvalidDataException("incorrect userID format");
        }
        for(char x : userIdIn.toCharArray()){
            if(!Character.isAlphabetic(x)){
                throw new InvalidDataException("Incorrect userID format");
            }
        }
        this.userId = userIdIn;
    }

    public void updateTradable (TradableDTO o){
        if(o == null){
            return;
        }
        tradables.put(o.tradableId(),o);
    }

    @Override
    public String toString(){
        String s = "User Id: " + getUserId();
        for(TradableDTO tradable: tradables.values()){
            s += "\n";
            s += tradable.toString();
        }
        return s;
    }
}
