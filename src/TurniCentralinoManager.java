import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class TurniCentralinoManager {
    private static Connection connection;

    public TurniCentralinoManager(Connection connection) {
        this.connection = connection;
    }

    public static void aggiungiTurniMancanti() {
        try {
            // Ottieni la data attuale
            LocalDate dataAttuale = LocalDate.now();

            // Ciclo per i prossimi 14 giorni
            for (int i = 0; i < 14; i++) {
                LocalDate dataTurno = dataAttuale.plusDays(i);

                // Per ciascun turno (mattina, pomeriggio, notte)
                String[] turni = { "mattina", "pomeriggio", "notte" };
                for (String turno : turni) {
                    // Verifica se esiste già una riga per questa data e questo turno
                    if (!esisteTurno(dataTurno, turno)) {
                        // Inserisci una nuova riga nella tabella dei turni al centralino
                        inserisciTurno(dataTurno, turno);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean esisteTurno(LocalDate data, String turno) throws SQLException {
        String query = "SELECT COUNT(*) FROM TurniCentralino WHERE Data = ? AND Turno = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, data.toString()); // Converte la data in una stringa
        statement.setString(2, turno);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        statement.close();
        return count > 0;
    }

    private static void inserisciTurno(LocalDate data, String turno) throws SQLException {
        String query = "INSERT INTO TurniCentralino (Data, Turno) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);

        // Converti la data in una stringa nel formato desiderato (ad esempio, "dd-MM-yyyy")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dataString = dateFormat.format(java.sql.Date.valueOf(data));

        statement.setString(1, dataString);
        statement.setString(2, turno);
        statement.executeUpdate();
        statement.close();
    }

    public static void rimuoviTurniPassati() {
        try {
            // Ottieni la data attuale
            LocalDate dataAttuale = LocalDate.now();

            // Esegui la query per rimuovere i turni con data antecedente all'odierna
            String query = "DELETE FROM TurniCentralino WHERE Data < ?";
            PreparedStatement statement = connection.prepareStatement(query);

            // Converti la data attuale in una stringa nel formato desiderato (ad esempio, "dd-MM-yyyy")
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String dataString = dateFormat.format(java.sql.Date.valueOf(dataAttuale));

            statement.setString(1, dataString);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
