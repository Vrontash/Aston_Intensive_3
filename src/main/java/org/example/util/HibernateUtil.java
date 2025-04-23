package org.example.util;

import org.example.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    public static void createSessionFactory(Properties prop){
        try {
            StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder();
            if (prop != null) //Если prop не указан, то в ssrb.build() возьмётся автоматически hibernate.properties
                ssrb.applySettings(prop);
            ServiceRegistry sr = ssrb.build();
            MetadataSources metadataSources = new MetadataSources(sr);
            metadataSources.addAnnotatedClass(User.class);
            Metadata metadata = metadataSources.buildMetadata();
            sessionFactory = metadata.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static void createSessionFactory(String url, String username, String password){
        closeSessionFactory();
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.url", url);
        properties.setProperty("hibernate.connection.username", username);
        properties.setProperty("hibernate.connection.password", password);
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        createSessionFactory(properties);
    }
    public static SessionFactory getSessionFactory(){
        if(sessionFactory == null)
            createSessionFactory(null);
        return sessionFactory;
    }
    public static void closeSessionFactory(){
        if (sessionFactory != null)
            sessionFactory.close();
    }

}
