package Main.Test;
import static org.junit.Assert.*;
import Main.ORM.mezzoDAO;

import Main.DomainModel.Mezzo;
import Main.ORM.DatabaseConnection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;

public class TestMezzoDC {
    private static Connection connection;

    @BeforeClass
    public static void setupClass() {
        DatabaseConnection dbConnection = new DatabaseConnection();
        dbConnection.connectToDatabase();
        connection = dbConnection.getConnection();
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        if (connection != null) connection.close();
    }

    @Test
    public void testAggiungiMezzoDAO() throws SQLException {
        Mezzo mezzo = new Mezzo("ME1", "ABC123", "Auto");

        mezzoDAO.aggiungiMezzoDAO(mezzo);

        PreparedStatement psAfterInsert = connection.prepareStatement("SELECT * FROM Mezzi WHERE Sigla_mezzo = ?");
        psAfterInsert.setString(1, mezzo.getSiglaMezzo());
        ResultSet rsAfterInsert = psAfterInsert.executeQuery();

        assertTrue(rsAfterInsert.next()); // Verifica che il mezzo sia stato aggiunto correttamente

        rsAfterInsert.close();
        psAfterInsert.close();
    }
    @Test
    public void testModificaMezzoDAO() throws SQLException {
        // Simulazione dell'input dell'utente
        String nuovaTarga = "ABC123";
        String nuovaTipologia = "Ambulanza";
        String siglaMezzo = "ME1"; // Modifica il mezzo aggiunto nel test precedente

        // Verifica che il mezzo esista prima di tentare la modifica
        String verificaQuery = "SELECT * FROM Mezzi WHERE Sigla_mezzo = ?";
        PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
        verificaStatement.setString(1, siglaMezzo);
        ResultSet resultSetVerifica = verificaStatement.executeQuery();

        if (resultSetVerifica.next()) {
            // Il mezzo esiste, quindi esegui l'aggiornamento
            String updateQuery = "UPDATE Mezzi SET targa = ?, tipologia = ? WHERE Sigla_mezzo = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, nuovaTarga);
            updateStatement.setString(2, nuovaTipologia);
            updateStatement.setString(3, siglaMezzo);
            updateStatement.executeUpdate();

            // Verifica che la tipologia del mezzo sia stata modificata correttamente
            String selectTipologiaQuery = "SELECT tipologia FROM Mezzi WHERE Sigla_mezzo = ?";
            PreparedStatement selectTipologiaStatement = connection.prepareStatement(selectTipologiaQuery);
            selectTipologiaStatement.setString(1, siglaMezzo);
            ResultSet resultSetTipologia = selectTipologiaStatement.executeQuery();
            if (resultSetTipologia.next()) {
                String tipologiaAttuale = resultSetTipologia.getString("tipologia");
                assertEquals(nuovaTipologia, tipologiaAttuale);
            } else {
                fail("La tipologia del mezzo non è stata trovata nel database dopo l'aggiornamento.");
            }

            // Chiudi le risorse
            resultSetTipologia.close();
            selectTipologiaStatement.close();
        } else {
            // Il mezzo non esiste, quindi il test fallisce
            fail("Il mezzo da modificare non esiste nel database.");
        }

        // Chiudi le risorse
        resultSetVerifica.close();
        verificaStatement.close();
    }
    @Test
    public void testEliminaMezzoDAO() throws SQLException {
        // Simulazione dell'input dell'utente
        String siglaMezzoDaEliminare = "ME1"; // Modifica con la sigla del mezzo da eliminare

        // Verifica che il mezzo esista prima di tentare l'eliminazione
        String verificaQuery = "SELECT * FROM Mezzi WHERE Sigla_mezzo = ?";
        PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
        verificaStatement.setString(1, siglaMezzoDaEliminare);
        ResultSet resultSetVerifica = verificaStatement.executeQuery();

        if (resultSetVerifica.next()) {
            // Il mezzo esiste, quindi esegui l'eliminazione
            String deleteQuery = "DELETE FROM Mezzi WHERE Sigla_mezzo = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setString(1, siglaMezzoDaEliminare);
            deleteStatement.executeUpdate();

            // Verifica che il mezzo sia stato eliminato correttamente
            PreparedStatement psAfterDelete = connection.prepareStatement("SELECT * FROM Mezzi WHERE Sigla_mezzo = ?");
            psAfterDelete.setString(1, siglaMezzoDaEliminare);
            ResultSet rsAfterDelete = psAfterDelete.executeQuery();

            assertFalse(rsAfterDelete.next()); // Assicurati che non ci siano più righe corrispondenti al mezzo eliminato

            // Chiudi le risorse
            rsAfterDelete.close();
            psAfterDelete.close();
        } else {
            // Il mezzo non esiste, quindi il test fallisce
            fail("Il mezzo da eliminare non esiste nel database.");
        }

        // Chiudi le risorse
        resultSetVerifica.close();
        verificaStatement.close();
    }

}
