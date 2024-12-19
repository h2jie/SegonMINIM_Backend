package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.*;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

public class ManagerImpl implements Manager {
    private static Manager instance;
    protected List<User> users;
    protected List<StoreObject> objects;
    protected List<Inventory> inventory;
    protected TreeMap<String, UserToken> tokens;
    protected Store store;

    final static Logger logger = Logger.getLogger(ManagerImpl.class);

    private ManagerImpl() {
        this.users = new LinkedList<>();
        this.objects = new LinkedList<>();
        this.inventory = new ArrayList<>();
        this.tokens = new TreeMap<>();
    }

    public static Manager getInstance() {
        if (instance==null) instance = new ManagerImpl();
        return instance;
    }

    //User related
    public User addUser(User user){
        logger.info("Adding User: "+user);
        this.users.add(user);
        logger.info("User added");
        return user;
    }

    public User addUser(String username, String password, String mail){
        User user = new User(username, password,mail);
        return this.addUser(user);
    }

    public User getUser(String username) throws UserNotFoundException{
        for (User u:users){
            if(u.getUsername().equals(username)){
                logger.info("Returning User: "+u);
                return u;
            }
        }
        logger.warn("User not found");
        throw new UserNotFoundException();
    }

    public User getUserByID(String userID) throws UserNotFoundException, SQLException{
        for (User u:users){
            if(u.getId().equals(userID)){
                logger.info("Returning User: "+u);
                return u;
            }
        }
        logger.warn("User not found");
        throw new UserNotFoundException();
    }

    public User getMail(String mail) throws MailNotFoundException, SQLException {
        for(User u:users){
            if(u.getMail().equals(mail)){
                logger.info("Returning User: "+u);
                return u;
            }
        }
        logger.warn("Mail not found");
        throw new MailNotFoundException();
    }
    public void addPuntos(String userID, int puntos) throws UserNotFoundException, SQLException {
        logger.info("Adding points: "+puntos);
        this.getUserByID(userID).incrementarPuntos(puntos);
        logger.info("Points added");
    }

    //Store related

    public StoreObject addToStore(StoreObject object){
        logger.info("Adding Object: "+object);
        this.objects.add(object);
        logger.info("Object added");
        return object;
    }

    public StoreObject addToStore(String name, double price, String URL, String description){
        StoreObject object = new StoreObject(name,price, URL, description);
        return this.addToStore(object);
    }

    public StoreObject getObject(String name) throws ObjectNotFoundException{
        for(StoreObject o : objects){
            if (o.getName().equals(name)){
                logger.info("Returning Object:" +o);
                return o;
            }
        }
        logger.warn("Object not found");
        throw new ObjectNotFoundException();
    }

    @Override
    public void buyObject(String username, String objectName, int quantity)
            throws UserNotFoundException, ObjectNotFoundException, NotEnoughMoneyException {
        User user = getUser(username);
        StoreObject object = getObject(objectName);

        double requiredMoney = object.getPrice() * quantity;
        if(user.getMoney() < requiredMoney){
            throw new NotEnoughMoneyException("Not enough money");
        }

        user.money -= requiredMoney;
        for(InventoryObject inventoryObject : user.getMyObjects()){
            if(inventoryObject.getName().equals(objectName)){
                inventoryObject.quantity += quantity;
                return;
            }
        }

        user.getMyObjects().add(new InventoryObject(objectName, quantity));
    }

    //Register

    public User register(String username, String password, String mail){
        try{
            getUser(username);
            return null;
        }catch(UserNotFoundException e){
            logger.info("Adding user with credentials: Username="+username+", Password="+password+", Mail:"+mail);
            return addUser(username,password,mail);

        }
    }

    //Login through username and through email

    private User login(User u, String password){
        if(u.getPassword().equals(password)){
            logger.info("Login successful");
            return u;
        }else{
            logger.warn("Wrong password");
            throw new WrongPasswordException();
        }
    }

    public User login1(String username, String password) throws UserNotFoundException, WrongPasswordException, SQLException{
        User u = getUser(username);
        return login(u, password);
    }

    public User login2(String mail, String password) throws MailNotFoundException, WrongPasswordException, SQLException{
        User u = getMail(mail);
        return login(u, password);
    }

    public User login3(String userID, String password) throws UserNotFoundException, WrongPasswordException, SQLException{
        User u = getUserByID(userID);
        return login(u, password);
    }

    //List of Users

    public List<User> findAllUsers(){
        return this.users;
    }

    public void deleteUser(String username){
        users.removeIf(u -> u.getUsername().equals(username));
    }

    public User updateUsername(String userID, String username) throws SQLException{ return null; };
    public User updateEmail(String userID, String email) throws SQLException{ return null; };
    public User updatePassword(String userID, PasswordChangeRequest r) throws SQLException, WrongPasswordException{ return null; };

    //Get list of objects of a User and the Store

    public List<Inventory> getUserObjects(String userID) throws UserNotFoundException, SQLException{
        return inventory.stream().filter(inventory -> inventory.getUserID().equals(userID)).collect(Collectors.toList());
    }

    public List<StoreObject> findAllObjects(){
        return this.objects;
    }

    public UserToken generateToken(String username){
        UserToken token = new UserToken();
        tokens.put(username, token);
        return token;
    }

    public boolean validateToken(String username, String token){
        if(token == null) return false;
        UserToken userToken = tokens.get(username);
        if(userToken == null) return false;
        if(!userToken.getToken().equals(token)) return false;
        return !userToken.hasExpired();
    }

    public void deleteToken(String username){
        tokens.remove(username);
    }

    //Clear & Size

    public void clearUsers(){
        users.clear();
    }

    public void clearObjects(){
        objects.clear();
    }

    public void clear(){
        clearUsers();
        clearObjects();
    }

    public int sizeUsers(){
        return users.size();
    }

    public int sizeObjects(){
        return objects.size();
    }

}