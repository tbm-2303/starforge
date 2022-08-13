/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.DriverDTO;
import dtos.RenameMeDTO;
import entities.Driver;
import entities.RenameMe;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import entities.Role;
import entities.User;
import utils.EMF_Creator;

/**
 *
 * @author tha
 */
public class Populator {


    public static void populate(){
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        FacadeExample fe = FacadeExample.getFacadeExample(emf);
        fe.create(new RenameMeDTO(new RenameMe("First 1", "Last 1")));
        fe.create(new RenameMeDTO(new RenameMe("First 2", "Last 2")));
        fe.create(new RenameMeDTO(new RenameMe("First 3", "Last 3")));
    }
    public static void populate2(){
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        CarFacade carFacade = CarFacade.getCarFacadeExample(emf);
        carFacade.addDriverToCar(1,2);
    }


    public static void populate3(){
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();

        EntityManager em = emf.createEntityManager();

        User user = new User("user", "test123");
        User driver = new User("driver", "test123");
        User admin = new User("admin", "test123");

        if(admin.getUserPass().equals("test")||user.getUserPass().equals("test"))
            throw new UnsupportedOperationException("You have not changed the passwords");

        em.getTransaction().begin();
        Role userRole = new Role("user");
        Role driverRole = new Role("driver");
        Role adminRole = new Role("admin");
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
        System.out.println("PW: " + user.getUserPass());
        System.out.println("Testing user with OK password: " + user.verifyPassword("test"));
        System.out.println("Testing user with wrong password: " + user.verifyPassword("test1"));
        System.out.println("Created TEST Users");

    }
    
    public static void main(String[] args) {
        //populate();
        populate2();
        populate3();
    }
}
