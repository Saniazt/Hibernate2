package org.example;

import org.example.model.Item;
import org.example.model.Person;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.*;


public class App {
    public static void main(String[] args) {
        Configuration configuration = new Configuration().addAnnotatedClass(Person.class)
                .addAnnotatedClass(Item.class);

        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        try {
            session.beginTransaction();

            Person person = session.get(Person.class, 8);
            System.out.println("Got the person from session1");
            session.getTransaction().commit();
            // session.close() - auto
            System.out.println("Session is closed");

            // открываем сессию2 и транзакцию еще раз(можно делать в любом месте)
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            System.out.println("Inside the second transaction");
            person = (Person) session.merge(person);
            Hibernate.initialize(person.getItems()); // явно указываем что мы хоти подгрузить опдвязаные сущности
            session.getTransaction().commit();
            System.out.println("Out of session2");

            System.out.println(person.getItems()); // после initialize можно вызывать вне транзакции

        } finally {
            sessionFactory.close();
        }
    }
}
