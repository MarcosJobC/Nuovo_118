package Main.Test;

import Main.DomainModel.Paziente;
import Main.ORM.DatabaseConnection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.*;
import java.time.LocalDate;

import static org.junit.Assert.*;


public class TestPazienteDC {

    private static Connection connection;

    @BeforeClass
    public static void setupClass() {
        // Assicurati di configurare una connessione al database di test
        DatabaseConnection dbConnection = new DatabaseConnection();
        dbConnection.connectToDatabase();
        connection = dbConnection.getConnection();
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        // Chiudi la connessione dopo tutti i test
        if (connection != null) connection.close();
    }
    @Test
    public void testAggiungiPazienteDAO() throws SQLException {
        LocalDate dataNascita = LocalDate.of(2000, 1, 1);
        Paziente paziente = new Paziente(999, "Mario", "Rossi", dataNascita, "Roma", "Via ugo foscolo");

        String insertQuery = "INSERT INTO Pazienti (Nome, Cognome, DataNascita, LuogoNascita, IndirizzoResidenza) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
        insertStatement.setString(1, paziente.getNomePaziente());
        insertStatement.setString(2, paziente.getCognomePaziente());
        insertStatement.setDate(3, Date.valueOf(paziente.getDataNascita()));
        insertStatement.setString(4, paziente.getLuogoNascita());
        insertStatement.setString(5, paziente.getIndirizzoResidenza());

        insertStatement.executeUpdate();

        // Verifica che il paziente sia stato aggiunto correttamente
        PreparedStatement psAfterInsert = connection.prepareStatement("SELECT * FROM Pazienti WHERE Nome = ? AND Cognome = ?");
        psAfterInsert.setString(1, paziente.getNomePaziente());
        psAfterInsert.setString(2, paziente.getCognomePaziente());
        ResultSet rsAfterInsert = psAfterInsert.executeQuery();

        assertTrue(rsAfterInsert.next()); // Assicurati che ci sia almeno una riga nel risultato della query

        // Verifica che i dati del paziente aggiunto corrispondano ai dati del paziente di test
        assertEquals(paziente.getNomePaziente(), rsAfterInsert.getString("Nome"));
        assertEquals(paziente.getCognomePaziente(), rsAfterInsert.getString("Cognome"));
        assertEquals(Date.valueOf(paziente.getDataNascita()), rsAfterInsert.getDate("DataNascita"));
        assertEquals(paziente.getLuogoNascita(), rsAfterInsert.getString("LuogoNascita"));
        assertEquals(paziente.getIndirizzoResidenza(), rsAfterInsert.getString("IndirizzoResidenza"));

        rsAfterInsert.close();
        psAfterInsert.close();
    }
    @Test
    public void testmodificaPazienteDAO() throws SQLException {
        try {
            // Visualizza la lista dei pazienti
            String listaPazientiQuery = "SELECT * FROM Pazienti";
            PreparedStatement listaPazientiStatement = connection.prepareStatement(listaPazientiQuery);
            ResultSet pazientiResultSet = listaPazientiStatement.executeQuery();

            // Chiedi all'utente di inserire l'ID del paziente da modificare
            System.out.print("Inserisci l'ID del paziente da modificare: ");
            int idPazienteDaModificare = 19;

            String verificaQuery = "SELECT * FROM Pazienti WHERE ID = ?";
            PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
            verificaStatement.setInt(1, idPazienteDaModificare);
            ResultSet resultSet = verificaStatement.executeQuery();

            // Assicurati che ci sia almeno un risultato
            assertTrue(resultSet.next());

            String nomePaziente = resultSet.getString("Nome");
            String cognomePaziente = resultSet.getString("Cognome");
            LocalDate dataNascita = resultSet.getDate("DataNascita").toLocalDate();
            String luogoNascita = resultSet.getString("LuogoNascita");
            String indirizzoResidenza = resultSet.getString("IndirizzoResidenza");

            // Richiedi i nuovi valori o lascia vuoto per non modificare
            String nuovoNome = "Gisfredo";
            String updateQuery = "UPDATE Pazienti SET Nome = ?, Cognome = ?, DataNascita = ?, LuogoNascita = ?, IndirizzoResidenza = ? WHERE ID = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

            updateStatement.setString(1, nuovoNome);
            updateStatement.setString(2, cognomePaziente);
            updateStatement.setDate(3, Date.valueOf(dataNascita));
            updateStatement.setString(4, luogoNascita);
            updateStatement.setString(5, indirizzoResidenza);
            updateStatement.setInt(6, idPazienteDaModificare);

            // Esegui l'aggiornamento dei dati del paziente nel database
            int rowsAffected = updateStatement.executeUpdate();

            // Verifica che una riga sia stata effettivamente aggiornata nel database
            assertEquals(1, rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Test fallito a causa di un'eccezione: " + e.getMessage());
        }
    }
    @Test
    public void testEliminaPazienteDAO() throws SQLException {
        try {
            // Preparare i dati per l'eliminazione
            int idPazienteDaEliminare = 18;

            // Eseguire l'eliminazione del paziente
            String deleteServizioQuery = "DELETE FROM Pazienti WHERE id = ?";
            PreparedStatement deleteServizioStatement = connection.prepareStatement(deleteServizioQuery);
            deleteServizioStatement.setInt(1, idPazienteDaEliminare); // Utilizzare setInt() per un valore int
            deleteServizioStatement.executeUpdate();
            deleteServizioStatement.close();

            // Verificare che il paziente sia stato eliminato correttamente
            PreparedStatement psAfterDelete = connection.prepareStatement("SELECT * FROM Pazienti WHERE id = ?");
            psAfterDelete.setInt(1, idPazienteDaEliminare); // Utilizzare setInt() anche qui
            ResultSet rsAfterDelete = psAfterDelete.executeQuery();

            assertFalse(rsAfterDelete.next()); // Assicurarsi che non ci siano più righe corrispondenti al paziente eliminato

            rsAfterDelete.close();
            psAfterDelete.close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Test fallito a causa di un'eccezione: " + e.getMessage());
        }
    }




}
