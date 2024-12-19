package edu.upc.dsa.services;

import edu.upc.dsa.models.FAQ;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Api("/faqs")
@Path("/faqs")
public class FAQService {
    final static Logger logger = Logger.getLogger("FAQService");
    private static List<FAQ> faqs = null;

    public FAQService() {
        if (faqs == null) {
            faqs = new ArrayList<>();

            faqs.add(new FAQ("19/02/2024", "How to start the game?",
                    "Click the 'Start Game' button on the main screen to start a new game.", "Hangjie"));
            faqs.add(new FAQ("19/03/2024", "How to save game progress?",
                    "The game will save automatically, or you can save manually by selecting 'Save Game' in the menu.", "Admin"));
            faqs.add(new FAQ("30/06/2024", "How to buy game props？",
                    "Select the desired item on the store page，Just click the buy button and confirm payment. Please make sure your account balance is sufficient.", "Hangjie"));

            faqs.add(new FAQ("19/12/2024", "How do I view my inventory?",
                    "Click the 'My Items' button on the main interface to view all purchased props and items.", "Admin"));

            logger.info("FAQ service initialized with " + faqs.size() + " default entries");
        }
    }

    @GET
    @ApiOperation(value = "Get all the FAQs", notes = "Return to all Frequently Asked Questions in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successes", response = FAQ.class, responseContainer = "List")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFAQs() {
        logger.info("Getting all FAQs, total count: " + faqs.size());
        GenericEntity<List<FAQ>> entity = new GenericEntity<List<FAQ>>(faqs) {};
        return Response.ok(entity).build();
    }

    @POST
    @ApiOperation(value = "Add new FAQ", notes = "Add a new FAQ to the system")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "FAQ created successfully"),
            @ApiResponse(code = 400, message = "Invalid input\n")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFAQ(FAQ faq) {
        if (faq.getQuestion() == null || faq.getQuestion().isEmpty() ||
                faq.getAnswer() == null || faq.getAnswer().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Question and answer cannot be empty")
                    .build();
        }

        faqs.add(faq);
        logger.info("New FAQ added: " + faq.getQuestion());
        logger.info("Current FAQ count: " + faqs.size());

        return Response.status(Response.Status.CREATED)
                .entity(faq)
                .build();
    }
}