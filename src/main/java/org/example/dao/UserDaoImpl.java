package org.example.dao;


import org.example.model.User;
import org.example.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
    @Override
    public Optional<User> findById(int id) {
        Transaction tran = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            tran = session.beginTransaction();
            User user = session.get(User.class, id);
            tran.commit();
            logger.info("User found by Id: {}", id);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            if (tran != null)
                tran.rollback();
            logger.error("Error finding user by Id: {}", id, e);
            throw new HibernateException(e);
        }
    }

    @Override
    public List<User> findAll() {
        Transaction tran = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            tran = session.beginTransaction();
            List<User> users = session.createSelectionQuery("FROM User", User.class).getResultList();
            tran.commit();
            logger.info("Users found");
            return users;
        } catch (Exception e) {
            if (tran != null)
                tran.rollback();
            logger.error("Error retrieving all users", e);
            throw new HibernateException(e);
        }
    }

    @Override
    public void save(User user) {
        Transaction tran = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            tran = session.beginTransaction();
            session.persist(user);
            tran.commit();
            logger.info("Users saved: {}", user);
        } catch (Exception e) {
            if (tran != null)
                tran.rollback();
            logger.error("Error saving user", e);
            throw new HibernateException(e);
        }
    }

    @Override
    public void update(User user) {
        Transaction tran = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            tran = session.beginTransaction();
            session.merge(user);
            tran.commit();
            logger.info("Users updated: {}", user);
        } catch (Exception e) {
            if (tran != null)
                tran.rollback();
            logger.error("Error updating user: {}", user.getId(), e);
            throw new HibernateException(e);
        }
    }

    @Override
    public void delete(int id) {
        Transaction tran = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            tran = session.beginTransaction();
            User user = session.get(User.class, id);
            session.remove(user);
            tran.commit();
            logger.info("Users deleted: {}", user);
        } catch (Exception e) {
            if (tran != null)
                tran.rollback();
            logger.error("Error deleting user: {}", id, e);
            throw new HibernateException(e);
        }
    }
}
