package ORM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    public void connectToDatabase() {
        String url = "jdbc:postgresql://localhost:5432/Gestionale_118_db";
        String username = "webuser";
        String password = "webuser";

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password);
            /*System.out.println("Connessione al database stabilita con successo!");*/
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection() {
        return connection;
    }


}
