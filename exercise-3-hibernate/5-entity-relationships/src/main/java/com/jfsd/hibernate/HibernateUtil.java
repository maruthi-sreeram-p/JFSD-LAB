package com.jfsd.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Builds the SessionFactory once; it is expensive, so the whole program shares it.
 */
public class HibernateUtil {

    // Hibernate prints a lot of INFO text at start-up; only show real errors
    private static final Logger HIBERNATE_LOG = Logger.getLogger("org.hibernate");

    private static final SessionFactory FACTORY = build();

    private static SessionFactory build() {
        HIBERNATE_LOG.setLevel(Level.SEVERE);
        return new Configuration().configure().buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return FACTORY;
    }

    public static void shutdown() {
        FACTORY.close();
    }
}
