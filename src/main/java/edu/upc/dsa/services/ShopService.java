package edu.upc.dsa.services;

import com.google.common.annotations.GwtCompatible;
import edu.upc.dsa.Manager;
import edu.upc.dsa.ManagerImpl;
import edu.upc.dsa.dao.DAO;
import edu.upc.dsa.exceptions.NotEnoughMoneyException;
import edu.upc.dsa.exceptions.ObjectNotFoundException;
import edu.upc.dsa.exceptions.UserNotFoundException;
import edu.upc.dsa.models.StoreObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Api("/shop")
@Path("/shop")
public class ShopService {
    private final Manager manager = DAO.getInstance();

    public ShopService() {
    }

    @GET
    @Path("/listObjects")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = StoreObject.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItems(){
        try{
            GenericEntity<List<StoreObject>> entity = new GenericEntity<List<StoreObject>>(manager.findAllObjects()) {};
            return Response.ok(entity).build();
        }catch(SQLException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }


    @POST
    @Path("/buy/{objectID}/{userID}/{quantity}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 403, message = "Incorrect credentials"),
            @ApiResponse(code = 404, message = "Object not found"),
            @ApiResponse(code = 402, message = "Not enough money"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response buyObject(@PathParam("objectID") String objectID,
                              @PathParam("userID") String userID,
                              @PathParam("quantity") int quantity,
                              @CookieParam("token") Cookie token) {
        if(token == null || !manager.validateToken(userID, token.getValue())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        try{
            manager.buyObject(userID, objectID, quantity);
        }catch (ObjectNotFoundException e){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }catch(UserNotFoundException e){
            // in theory, it's impossible to reach this
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }catch(NotEnoughMoneyException e){
            return Response
                    .status(Response.Status.PAYMENT_REQUIRED)
                    .build();
        }catch(SQLException e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok().build();
    }


    @GET
    @Path("/money/{userID}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 403, message = "Incorrect credentials")
    })
    @Produces(MediaType.TEXT_PLAIN)
    public Response getMoney(@PathParam("userID") String userID, @CookieParam("token") Cookie token){
        if(token == null || !manager.validateToken(userID, token.getValue())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        try{
            GenericEntity<Double> entity = new GenericEntity<>(manager.getUserByID(userID).getMoney()) {};
            return Response.ok(entity).build();
        }catch(UserNotFoundException ignored){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }catch(SQLException ignored){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }


}
