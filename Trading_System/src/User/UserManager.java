package User;

import trade.TradableDTO;

import java.util.TreeMap;


public final class UserManager {
    private static TreeMap<String, User> users = new TreeMap<>();

    private static UserManager instance;

    public static UserManager getInstance(){
        if (instance == null)
             instance = new UserManager();

        return instance;

    }

    private  UserManager()
    {

    }

    public static void init(String[] usersIn) throws InvalidDataException{
        for(String user: usersIn){
           User newUser = new User(user);
           users.put(newUser.getUserId(), newUser);
        }
    }

    public static void updateTradable(String userId, TradableDTO o) throws InvalidDataException{
        if(o == null){
            throw new InvalidDataException("Invalid TradableDTO");
        }
        User user = users.get(userId);
        user.updateTradable(o);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (User user : users.values()) {
            s.append(user.toString());
            s.append("\n");
            s.append("\n");
        }
        return s.toString();
    }

}
