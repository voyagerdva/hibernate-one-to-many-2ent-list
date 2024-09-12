package nn.ru;

import nn.ru.entity.Doc;
import nn.ru.entity.Item;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HibernateTest1 {
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    // Настраиваем SessionFactory и открываем сессию перед каждым тестом
    @BeforeEach
    void setUp() {
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(Doc.class);
        configuration.addAnnotatedClass(Item.class);

        sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
    }

    // Закрываем сессию и фабрику после каждого теста
    @AfterEach
    void tearDown() {
        if (transaction != null) {
            transaction.commit();
        }
        if (session != null) {
            session.close();
        }
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    void test_add_2docs_and_save() {
        Doc doc1 = new Doc();
        Doc doc2 = new Doc();

        Item item = new Item();

        item.addDocToItem(doc1);
        item.addDocToItem(doc2);

        session.save(item);
    }

    @Test
    void test_add_2docs_and_get() {
        Doc doc1 = session.get(Doc.class, 1L);
        Doc doc2 = session.get(Doc.class, 2L);

        Item item = session.get(Item.class, 7L);

        System.out.println("\n\n\n\n\n\n\n==================");
        System.out.println("\n" + doc1 + "\n" + doc2);
        System.out.println("\n\n" + item);
        System.out.println("\n==================\n\n\n\n\n\n\n");
    }

    @Test
    void test_add_2docs_and_update() {
        Doc doc1 = session.get(Doc.class, 1L);
        Doc doc2 = session.get(Doc.class, 2L);

        Item item = session.get(Item.class, 7L);


        item.addDocToItem(doc1);
        item.addDocToItem(doc2);

        session.save(item);

        System.out.println(item);
    }

    @Test
    void test_delete_doc1() {
        Doc doc1 = session.get(Doc.class, 2L);
        session.delete(doc1);

        Item item = session.get(Item.class, 7L);
        System.out.println(item);
    }

    @Test
    void test_delete_doc1_doc2() {
        Doc doc1 = session.get(Doc.class, 3L);
        Doc doc2 = session.get(Doc.class, 4L);

        session.delete(doc1);
        session.delete(doc2);

        Item item = session.get(Item.class, 7L);

        System.out.println(item);
    }

    @Test
    void test_delete_item() {
        Item item = session.get(Item.class, 11L);
        session.delete(item);
    }
}
