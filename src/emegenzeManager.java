import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

public class emegenzeManager {

    private static Connection connection;
    public emegenzeManager(Connection connection) {
        this.connection = connection;
    }

    public static void aggiungiEmergenzeMancanti() {
        try {
            // Ottieni la data attuale
            LocalDate dataAttuale = LocalDate.now();

            // Ciclo per i prossimi 14 giorni
            for (int i = 0; i < 14; i++) {
                LocalDate dataEmergenza = dataAttuale.plusDays(i);

                // Per ciascun turno (mattina, pomeriggio, notte)
                String[] turni = { "mattina", "pomeriggio", "notte" };
                for (String turno : turni) {
                    // Verifica se esiste già una riga per questa data e questo turno
                    if (!esisteEmergenza(dataEmergenza, turno)) {
                        // Inserisci una nuova riga nella tabella "Emergenze"
                        inserisciEmergenza(dataEmergenza, turno);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static boolean esisteEmergenza(LocalDate data, String turno) throws SQLException {
        String query = "SELECT COUNT(*) FROM Emergenze WHERE Data = ? AND Turno = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDate(1, java.sql.Date.valueOf(data));
        statement.setString(2, turno);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        statement.close();
        return count > 0;
    }
    private static void inserisciEmergenza(LocalDate data, String turno) throws SQLException {
        String query = "INSERT INTO Emergenze (Data, Turno) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDate(1, java.sql.Date.valueOf(data));
        statement.setString(2, turno);
        statement.executeUpdate();
        statement.close();
        //System.out.println("Nuova emergenza aggiunta: Data=" + data + ", Turno=" + turno);
    }
    public static void rimuoviEmergenzePassate() {
        try {
            // Ottieni la data attuale
            LocalDate dataAttuale = LocalDate.now();

            // Esegui la query per rimuovere le emergenze con data antecedente all'odierna
            String query = "DELETE FROM Emergenze WHERE Data < ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, java.sql.Date.valueOf(dataAttuale));
            statement.executeUpdate();
            statement.close();

            //System.out.println("Emergenze passate rimosse con successo.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
