import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection connection; // Aggiungi questa variabile di istanza

    public void connectToDatabase() {
        String url = "jdbc:postgresql://localhost:5432/Gestionale_118_db";
        String username = "webuser";
        String password = "webuser";

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password); // Assegna la connessione alla variabile
            System.out.println("Connessione al database stabilita con successo!");
            // Ora puoi eseguire query e altre operazioni sul database utilizzando questa connessione.
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Aggiungi questo metodo per ottenere la connessione
    public Connection getConnection() {
        return connection;
    }
}
