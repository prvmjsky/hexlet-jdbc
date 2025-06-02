package io.hexlet;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) throws SQLException {
        try (var connection = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {

            var dao = new UserDAO(connection);
            dao.createUserBase();

            var tommy = new User("tommy", "123456789");
            var alex = new User("alex", "333333333");
            dao.save(tommy);
            dao.save(alex);

            System.out.println(dao.findByID(1L).get().getName());
            tommy.setName("mommy");
            dao.save(tommy);
            dao.showAll();

            dao.deleteByName("mommy");
            dao.deleteByID(2L);

            dao.showAll();
        }
    }
}
