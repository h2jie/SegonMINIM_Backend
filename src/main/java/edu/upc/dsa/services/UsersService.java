package edu.upc.dsa.services;

import edu.upc.dsa.Manager;
import edu.upc.dsa.dao.DAO;
import edu.upc.dsa.exceptions.UserNotFoundException;
import edu.upc.dsa.exceptions.WrongPasswordException;
import edu.upc.dsa.models.*;
import edu.upc.dsa.util.PasswordUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.SQLException;
import java.util.List;

@Api("/users")
@Path("/users")
public class UsersService {

    Manager manager = DAO.getInstance();
    final static Logger logger = Logger.getLogger(UsersService.class);

    public UsersService()  {
    }

    @PUT
    @ApiOperation("Create and logs in a new user")
    @Path("/register")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created", response = User.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 409, message = "User already exists"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {
        if(!user.validate()){
            logger.warn("Error validating user " + user);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        User result;
        // only add the user if it doesn't exist
        try{
            manager.getUser(user.getUsername());
            return Response.status(Response.Status.CONFLICT).build();
        } catch (UserNotFoundException e) {
            try{
                result = manager.register(user.getUsername(), PasswordUtils.getPasswordHash(user.getPassword()), user.getMail());
                if(result == null) throw new RuntimeException();
            }catch(SQLException ee){
                ee.printStackTrace();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }

            // creating a user also logs in
            UserToken token = manager.generateToken(result.getId());
            NewCookie cookie = new NewCookie("token", token.getToken(), "/", null, null, UserToken.MAX_AGE, false);
            GenericEntity<User> entity = new GenericEntity<User>(result){};
            return Response
                    .status(Response.Status.CREATED)
                    .cookie(cookie)
                    .entity(entity)
                    .build();


        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @ApiOperation("Attempts to log in")
    @Path("/login")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class),
            @ApiResponse(code = 403, message = "Incorrect Credentials"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest login) {
        User user;
        try{
            if(login.getUsername() != null){
                user = manager.login1(login.getUsername(), PasswordUtils.getPasswordHash(login.getPassword()));
            }else if(login.getPassword() != null){
                user = manager.login2(login.getEmail(), PasswordUtils.getPasswordHash(login.getPassword()));
            }else{
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }catch(UserNotFoundException | WrongPasswordException e){
            return Response.status(Response.Status.FORBIDDEN).build();
        }catch(SQLException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        UserToken token = manager.generateToken(user.getId());
        NewCookie cookie = new NewCookie("token", token.getToken(), "/", null, null, UserToken.MAX_AGE, false);
        GenericEntity<User> entity = new GenericEntity<User>(user){};
        return Response
                .status(Response.Status.OK)
                .cookie(cookie)
                .entity(entity)
                .build();
    }


    @GET
    @ApiOperation("Get user information")
    @Path("userinfo/{userID}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class),
            @ApiResponse(code = 403, message = "Incorrect Credentials"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInfo(@PathParam("userID") String userID, @CookieParam("token") Cookie token) {
        if(token == null || !manager.validateToken(userID, token.getValue())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        try{
            User u = manager.getUserByID(userID);
            GenericEntity<User> entity = new GenericEntity<>(u){};
            return Response.ok(entity).build();
        }catch(UserNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }catch(SQLException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @ApiOperation("Gets objects from user")
    @Path("/getObjects/{userID}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Inventory.class, responseContainer = "List"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObjects(@PathParam("userID") String userID, @CookieParam("token") Cookie token) {
        if(token == null || !manager.validateToken(userID, token.getValue())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        try{
            GenericEntity<List<Inventory>> entity = new GenericEntity<>(manager.getUserObjects(userID)) {};
            return Response
                    .status(Response.Status.OK)
                    .entity(entity)
                    .build();
        }catch(UserNotFoundException e){
            // crec que mai hauriem d'arribar aquí
            return Response.status(Response.Status.NOT_FOUND).build();
        }catch(SQLException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GET
    @Path("/puntos/{userID}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 403, message = "Incorrect credentials"),
            @ApiResponse(code = 500, message = "Internal server error"),
    })
    @Produces(MediaType.TEXT_PLAIN)
    public Response getPuntos(@PathParam("userID") String userID, @CookieParam("token") Cookie token){
        if(token == null || !manager.validateToken(userID, token.getValue())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        try{
            GenericEntity<Integer> entity = new GenericEntity<>(manager.getUserByID(userID).getPuntos()) {};
            return Response.ok(entity).build();
        }catch(UserNotFoundException ignored){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }catch(SQLException ignored){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PUT
    @Path("/changeUsername/{userID}")
    @ApiOperation("Change username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucess", response = User.class),
            @ApiResponse(code = 403, message = "Incorrect credentials"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeUsername(@PathParam("userID") String userID, @CookieParam("token") Cookie token, String newUsername){
        if(token == null || !manager.validateToken(userID, token.getValue())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        try{
            User result = manager.updateUsername(userID, newUsername);
            GenericEntity<User> entity = new GenericEntity<User>(result){};
            return Response.ok(entity).build();
        }catch(SQLException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PUT
    @Path("/changeEmail/{userID}")
    @ApiOperation("Change email")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucess", response = User.class),
            @ApiResponse(code = 403, message = "Incorrect credentials"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeEmail(@PathParam("userID") String userID, @CookieParam("token") Cookie token, String newEmail){
        if(token == null || !manager.validateToken(userID, token.getValue())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        try{
            User result = manager.updateEmail(userID, newEmail);
            GenericEntity<User> entity = new GenericEntity<User>(result){};
            return Response.ok(entity).build();
        }catch(SQLException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/changePassword/{userID}")
    @ApiOperation("Change password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucess", response = User.class),
            @ApiResponse(code = 403, message = "Incorrect password"),
            @ApiResponse(code = 401, message = "Incorrect userID or token"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(@PathParam("userID") String userID, @CookieParam("token") Cookie token, PasswordChangeRequest req){
        if(token == null || !manager.validateToken(userID, token.getValue())){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try{
            User result = manager.updatePassword(userID, req);
            if(result == null) return Response.status(Response.Status.UNAUTHORIZED).build();
            GenericEntity<User> entity = new GenericEntity<User>(result){};
            return Response.ok(entity).build();
        }catch(SQLException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }catch(WrongPasswordException e){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

}
