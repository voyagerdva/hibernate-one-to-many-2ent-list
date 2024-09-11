package nn.ru;

import nn.ru.entity.Doc;
import nn.ru.entity.Group;
import nn.ru.entity.Item;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;

public class HibernateTest {
    @Test
    public void test_add_2docs_and_save() {
        // СОЗДАЕМ КОНФИГУРАЦИЮ И ДОБАВЛЯЕМ СУЩНОСТИ:
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(nn.ru.entity.Doc.class);
        configuration.addAnnotatedClass(nn.ru.entity.Item.class);

        // СОЗДАЕМ ФАКБРИКУ СЕССИЙ, ОТКРЫВАЕМ СЕССИЮ И ОТКРЫВАЕМ ТРАНЗАКЦИЮ:

        try (SessionFactory factory = configuration.buildSessionFactory();
             Session session = factory.openSession();) {
            Transaction transaction = session.beginTransaction();

            // РАБОТА С ДАННЫМИ:
            Doc doc1 = new Doc();
            Doc doc2 = new Doc();

            Item item = new Item();

            item.addDocToItem(doc1);
            item.addDocToItem(doc2);

//            session.save(doc1);
//            session.save(doc2);
            session.save(item);


            session.getTransaction().commit();
        }

    }
}
