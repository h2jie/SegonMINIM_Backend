package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface Manager {

    //User related
    User addUser(User t) throws SQLException;
    User addUser(String username, String password, String email) throws SQLException;
    User getUser(String username) throws UserNotFoundException, SQLException;
    User getUserByID(String userID) throws UserNotFoundException, SQLException;
    User getMail (String mail) throws MailNotFoundException, SQLException;

    void addPuntos(String userID, int puntos) throws UserNotFoundException, SQLException;



    //Store related
    StoreObject addToStore(StoreObject object) throws SQLException;
    StoreObject addToStore(String name, double price, String URL, String description) throws SQLException;
    StoreObject getObject(String name) throws ObjectNotFoundException, SQLException;

    void buyObject(String userID, String objectID, int quantity)
            throws UserNotFoundException, ObjectNotFoundException, NotEnoughMoneyException, SQLException;

    //Register
    User register(String username, String password, String mail) throws SQLException;

    //Login through username and through mail
    User login1(String username, String password) throws UserNotFoundException, WrongPasswordException, SQLException;
    User login2(String mail, String password) throws UserNotFoundException, WrongPasswordException, SQLException;
    User login3(String userID, String password) throws UserNotFoundException, WrongPasswordException, SQLException;

    //List of Users
    List<User> findAllUsers() throws SQLException;
    void deleteUser(String username) throws SQLException;
    User updateUsername(String userID, String username) throws SQLException;
    User updateEmail(String userID, String email) throws SQLException;
    User updatePassword(String userID, PasswordChangeRequest r) throws SQLException, WrongPasswordException;

    //Get the Objects of a User and the Store
    List<Inventory> getUserObjects(String userID) throws UserNotFoundException, SQLException;
    List<StoreObject> findAllObjects() throws SQLException;

    //Token related
    UserToken generateToken(String username);
    boolean validateToken(String username, String token);
    void deleteToken(String username);

    //Clear and Size of lists used (Users & StoreObjects)
    int sizeUsers();
    int sizeObjects();
}
