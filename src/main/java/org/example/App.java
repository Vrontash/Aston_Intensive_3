package org.example;

import org.example.dao.UserDaoImpl;
import org.example.service.UserService;
import org.example.util.HibernateUtil;

import java.util.Scanner;

public class App
{
    public static void main( String[] args ) {
        UserService us = new UserService(new UserDaoImpl(), new Scanner(System.in));
        us.run();
        HibernateUtil.closeSessionFactory();
    }

}
