package Main.Test;
import Main.DomainModel.Utente;
import Main.ORM.DatabaseConnection;
import Main.ORM.utenteDAO;
import org.junit.*;

import static org.junit.Assert.*;

import java.sql.*;

public class TestUtente {



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
    public void testRegistrazioneDAO() throws SQLException {
        // Crea un oggetto utente per il test, passando tutti i parametri necessari
        Utente utente = new Utente(999, "Mario", "Rossi", "1990-01-01", "Volontario", "RSSMRO90A01H501Z", "passwordSegreta", false);

        // Esegui il metodo di registrazione
        utenteDAO.registrazioneDAO(utente);

        // Verifica che l'utente sia stato inserito correttamente
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM Volontari WHERE codice_fiscale = ?");
        ps.setString(1, utente.getCodiceFiscale());
        ResultSet rs = ps.executeQuery();

        assertTrue(rs.next());  // Verifica che ci sia almeno un risultato
        assertEquals(utente.getNome(), rs.getString("Nome"));
        assertEquals(utente.getCognome(), rs.getString("Cognome"));
        assertEquals(utente.getDatadinascita(), rs.getString("data_di_nascita"));
        assertEquals(utente.getQualifica(), rs.getString("Qualifica"));
        assertEquals(utente.getCodiceFiscale(), rs.getString("codice_fiscale"));
        assertEquals(utente.getPassword(), rs.getString("Password"));
        assertFalse(rs.getBoolean("IsAdmin"));  // Assicurati che l'IsAdmin sia impostato correttamente a false

        rs.close();
        ps.close();
    }
    @Test
    public void testModificaUtenteDAO() throws SQLException {
        // Esegui il metodo di modifica dell'utente
        String nuovoNome = "Giuseppe";
        String nuovoCognome = "Verdi";
        String nuovaQualifica = "Autista";

        String codiceFiscaleVolontario = "RSSMRO90A01H501Z";
        String verificaQuery = "SELECT * FROM Volontari WHERE Codice_fiscale = ?";
        PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        verificaStatement.setString(1, codiceFiscaleVolontario);
        ResultSet volontarioResultSet = verificaStatement.executeQuery();

        if (volontarioResultSet.next()) {
            volontarioResultSet.updateString("Nome", nuovoNome);
            volontarioResultSet.updateString("Cognome", nuovoCognome);
            volontarioResultSet.updateString("Qualifica", nuovaQualifica);
            volontarioResultSet.updateRow();

            // Verifica che l'utente sia stato modificato correttamente nel database
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Volontari WHERE codice_fiscale = ?");
            ps.setString(1, codiceFiscaleVolontario);
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next());  // Verifica che ci sia almeno un risultato
            assertEquals(nuovoNome, rs.getString("Nome"));
            assertEquals(nuovoCognome, rs.getString("Cognome"));
            assertEquals(nuovaQualifica, rs.getString("Qualifica"));
            rs.close();
            ps.close();
        }

    }
    @Test
    public void testEliminaUtenteDAO() throws SQLException {

        String codiceFiscaleVolontario="RSSMRO90A01H501Z";
        String deleteQueryVolontario = "DELETE FROM Volontari WHERE codice_fiscale = ?";
        PreparedStatement deleteStatementVolontario = connection.prepareStatement(deleteQueryVolontario);
        deleteStatementVolontario.setString(1, codiceFiscaleVolontario);
        deleteStatementVolontario.executeUpdate();

        // Verifica che l'utente sia stato eliminato correttamente
        PreparedStatement psAfterDelete = connection.prepareStatement("SELECT * FROM Volontari WHERE codice_fiscale = ?");
        psAfterDelete.setString(1, codiceFiscaleVolontario);
        ResultSet rsAfterDelete = psAfterDelete.executeQuery();

        assertFalse(rsAfterDelete.next());  // Assicurati che non ci siano più righe corrispondenti all'utente eliminato

        rsAfterDelete.close();
        psAfterDelete.close();

    }




}
