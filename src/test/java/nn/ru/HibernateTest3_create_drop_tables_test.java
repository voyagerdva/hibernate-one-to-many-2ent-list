package nn.ru;

import nn.ru.entity.Doc;
import nn.ru.entity.Item;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HibernateTest3_create_drop_tables_test {
    private static SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    // Выполняется один раз перед всеми тестами
    @BeforeAll
    static void setUpAll() {
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(Doc.class);
        configuration.addAnnotatedClass(Item.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    // Выполняется перед каждым тестом для открытия сессии и транзакции
    @BeforeEach
    void setUp() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();

        // Создание таблиц перед тестом
        createTables();
    }

    // Создание таблиц
    private void createTables() {
        // SQL для создания таблицы items
        String createItemTable = "CREATE TABLE IF NOT EXISTS items (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(255))";

        // SQL для создания таблицы docs с внешним ключом на items
        String createDocTable = "CREATE TABLE IF NOT EXISTS docs (" +
                "id SERIAL PRIMARY KEY, " +
                "title VARCHAR(255), " +
                "item_id INTEGER, " +
                "CONSTRAINT fk_item " +
                "FOREIGN KEY (item_id) REFERENCES items(id) " +
                "ON DELETE CASCADE)"; // Внешний ключ с каскадным удалением

        // Выполняем SQL-запросы через Hibernate
        session.createNativeQuery(createItemTable).executeUpdate();
        session.createNativeQuery(createDocTable).executeUpdate();
    }


    // Выполняется после каждого теста для завершения транзакции, удаления таблиц и закрытия сессии
    @AfterEach
    void tearDown() {
        if (transaction != null) {
            transaction.commit();
        }

        // Удаление таблиц после теста
        dropTables();

        if (session != null) {
            session.close();
        }
    }

    // Удаление таблиц
    private void dropTables() {
        String dropDocTable = "DROP TABLE IF EXISTS docs";
        String dropItemTable = "DROP TABLE IF EXISTS items";

        // Выполняем SQL-запросы через Hibernate
        transaction = session.beginTransaction();
        session.createNativeQuery(dropDocTable).executeUpdate();
        session.createNativeQuery(dropItemTable).executeUpdate();
        transaction.commit();
    }


    // Выполняется один раз после всех тестов для закрытия фабрики сессий
    @AfterAll
    static void tearDownAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }


    @Test
    @Order(1)
    void test_add_2docs_and_save() {
        Doc doc1 = new Doc();
        Doc doc2 = new Doc();

        Item item = new Item();

        item.addDocToItem(doc1);
        item.addDocToItem(doc2);

        // эти сейвы нужны только если НЕ выставлен cascade=ALL. Если выставлен, то можно без них
        session.save(doc1);
        session.save(doc2);
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
