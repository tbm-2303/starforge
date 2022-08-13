package rest;

import com.google.gson.Gson;
import entities.Role;
import entities.User;

import java.io.IOException;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;
import utils.HttpUtils;

/**
 * @author lam@cphbusiness.dk
 */
@Path("info")
public class DemoResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery ("select u from User u",entities.User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("setup")
    public String setup() {
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = new User("user", "user123");
            User admin = new User("admin", "admin123");
            User driver = new User("driver", "driver123");
            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            Role driverRole = new Role("driver");
            user.setRole(userRole);
            admin.setRole(adminRole);
            driver.setRole(driverRole);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(driverRole);
            em.persist(user);
            em.persist(driver);
            em.persist(admin);
            em.getTransaction().commit();
            return "ok! setup";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }
}