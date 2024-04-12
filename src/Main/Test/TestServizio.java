package Main.Test;

import Main.ORM.DatabaseConnection;
import org.junit.*;
import java.sql.*;

import static org.junit.Assert.*;

public class TestServizio {

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
    public void testAggiungiServizioDAO() {
        String dataServizio = "01-01-2025";
        String orarioString = "15:22:22";
        String siglaMezzo = "2626";
        int Autista = 0; // Inizializza l'ID dell'autista a 0
        int pazienteId = 9;
        int Soccorritore = 0; // Inizializza l'ID del soccorritore a 0

        try {
            String query = "INSERT INTO Servizi (Data, Orario, Paziente, Sigla_Mezzo, Autista, Soccorritore) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, dataServizio);
            preparedStatement.setTime(2, java.sql.Time.valueOf(orarioString));
            preparedStatement.setInt(3, pazienteId);
            preparedStatement.setString(4, siglaMezzo);
            preparedStatement.setInt(5, Autista);
            preparedStatement.setInt(6, Soccorritore);
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            // Verifica che una riga sia stata effettivamente aggiunta al database
            assertEquals(1, rowsAffected);

            // Ora puoi eseguire una query per ottenere i valori inseriti e verificarli
            // ad esempio, potresti eseguire una query per verificare che i valori inseriti corrispondano a quelli attesi

            // Esempio:
            String selectQuery = "SELECT * FROM Servizi WHERE Data = ? AND Orario = ? AND Paziente = ? AND Sigla_Mezzo = ? AND Autista = ? AND Soccorritore = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setString(1, dataServizio);
            selectStatement.setTime(2, java.sql.Time.valueOf(orarioString));
            selectStatement.setInt(3, pazienteId);
            selectStatement.setString(4, siglaMezzo);
            selectStatement.setInt(5, Autista);
            selectStatement.setInt(6, Soccorritore);
            ResultSet resultSet = selectStatement.executeQuery();

            // Verifica che almeno una riga sia stata restituita dalla query
            assertTrue(resultSet.next());

            // Verifica che i valori restituiti corrispondano a quelli attesi
            assertEquals(dataServizio, resultSet.getString("Data"));
            assertEquals(java.sql.Time.valueOf(orarioString), resultSet.getTime("Orario"));
            assertEquals(pazienteId, resultSet.getInt("Paziente"));
            assertEquals(siglaMezzo, resultSet.getString("Sigla_Mezzo"));
            assertEquals(Autista, resultSet.getInt("Autista"));
            assertEquals(Soccorritore, resultSet.getInt("Soccorritore"));

            resultSet.close();
            selectStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testmodificaServizioDAO() {
        try {
            // Ottieni una lista di tutti i servizi
            String listaServiziQuery = "SELECT * FROM Servizi";
            PreparedStatement listaServiziStatement = connection.prepareStatement(listaServiziQuery);
            ResultSet serviziResultSet = listaServiziStatement.executeQuery();


            // Chiedi all'utente di inserire l'ID del servizio da modificare
            int idServizioDaModificare = 9;

            // Ora che abbiamo l'ID valido, otteniamo i dati del servizio da modificare
            String dataServizio = "";
            Time orarioServizio = null;
            String orarioServizioString = "";
            int pazienteServizio = 0;
            String siglaMezzo = "";
            int Autista = 0;
            int Soccorritore = 0;

            String servizioQuery = "SELECT * FROM Servizi WHERE ID = ?";
            PreparedStatement servizioStatement = connection.prepareStatement(servizioQuery);
            servizioStatement.setInt(1, idServizioDaModificare);
            ResultSet servizioResultSet = servizioStatement.executeQuery();

            if (servizioResultSet.next()) {
                dataServizio = servizioResultSet.getString("Data");
                orarioServizio = servizioResultSet.getTime("Orario");
                pazienteServizio = servizioResultSet.getInt("Paziente");
                orarioServizioString = orarioServizio.toLocalTime().toString();
                siglaMezzo = servizioResultSet.getString("sigla_mezzo");
                Autista = servizioResultSet.getInt("Autista");
                Soccorritore = servizioResultSet.getInt("Soccorritore");
            } else {
                return; // Esci dal metodo in caso di ID non valido
            }
            String nuovaData = dataServizio; // Inizializza con la data attuale
            String nuovaOraString = orarioServizioString; // Inizializza con l'orario attuale

            String nuovoMezzo = "3030";

            // Aggiorna i dati del servizio nel database
            String updateServizioQuery = "UPDATE Servizi SET Data = ?, Orario = ?, Paziente = ?, sigla_mezzo = ?, Autista = ?, Soccorritore = ? WHERE ID = ?";
            PreparedStatement updateServizioStatement = connection.prepareStatement(updateServizioQuery);
            updateServizioStatement.setString(1, nuovaData);
            updateServizioStatement.setTime(2, Time.valueOf(nuovaOraString));
            updateServizioStatement.setInt(3, pazienteServizio);
            updateServizioStatement.setString(4, nuovoMezzo);
            updateServizioStatement.setInt(5, Autista);
            updateServizioStatement.setInt(6, Soccorritore);
            updateServizioStatement.setInt(7, idServizioDaModificare);

            int rowsAffected = updateServizioStatement.executeUpdate();
            updateServizioStatement.close();

            // Verifica che una riga sia stata effettivamente aggiornata nel database
            assertEquals(1, rowsAffected);

            // Ora puoi eseguire una query per ottenere il valore aggiornato e verificarlo
            // Ad esempio, potresti eseguire una query per verificare che il valore del mezzo sia stato aggiornato correttamente

            // Esempio:
            String selectQuery = "SELECT sigla_mezzo FROM Servizi WHERE ID = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, idServizioDaModificare);
            ResultSet resultSet = selectStatement.executeQuery();

            // Verifica che almeno una riga sia stata restituita dalla query
            assertTrue(resultSet.next());

            // Verifica che il valore del mezzo restituito corrisponda a quello atteso
            assertEquals(nuovoMezzo, resultSet.getString("sigla_mezzo"));

            resultSet.close();
            selectStatement.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testEliminaServizioDAO() throws SQLException {
        int idServizioDaEliminare = 39; // ID del servizio da eliminare

        // Esegui l'eliminazione del servizio dal database
        String deleteQueryServizio = "DELETE FROM Servizi WHERE ID = ?";
        PreparedStatement deleteStatementServizio = connection.prepareStatement(deleteQueryServizio);
        deleteStatementServizio.setInt(1, idServizioDaEliminare);
        deleteStatementServizio.executeUpdate();

        // Verifica che il servizio sia stato eliminato correttamente
        PreparedStatement psAfterDelete = connection.prepareStatement("SELECT * FROM Servizi WHERE ID = ?");
        psAfterDelete.setInt(1, idServizioDaEliminare);
        ResultSet rsAfterDelete = psAfterDelete.executeQuery();

        assertFalse(rsAfterDelete.next()); // Assicurati che non ci siano più righe corrispondenti al servizio eliminato

        rsAfterDelete.close();
        psAfterDelete.close();
    }
}
