package Model;

import Util.Bet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;
import java.util.concurrent.CountDownLatch;


public class HibernateUtil extends Thread{

    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;
    private static Session session;

    public HibernateUtil(){ }

    @Override
    public void run() {
        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
                MetadataSources sources = new MetadataSources(registry);
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory  = metadata.getSessionFactoryBuilder().build();
                session = sessionFactory.openSession();

            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
    }

    public static List<Bet> getData() {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        List<Bet> list;
        try {
//            Query qry = session.createQuery("from Bet b");
            list = session.createQuery("from Bet b").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        }

        return list;
    }

    public static void placeBet(Bet bet) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        try {
            session.save(bet);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        }

    }

    public static void editBet(Bet bet) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        try {

            session.update(bet);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        }
    }

    public static void deleteBet(Bet bet) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        try {

            session.remove(bet);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        }
    }


    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession() {
        return session;
    }

    public void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

}


