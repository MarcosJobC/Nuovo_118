import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;


public class assegnazioneAutomatica {

    private static Connection connection;
    public assegnazioneAutomatica(Connection connection) {
        this.connection = connection;
    }




    public static void assegnaAutomaticamente() {
        //Vengono assegnati prima tutti gli autisti e successivamente tutti i soccoritori
        assegnaAutistiAutomaticamente();
        assegnaSoccorritoriAutomaticamente();
    }

    public static void assegnaAutistiAutomaticamente() {
        try {

            String serviziSenzaAutistaQuery = "SELECT * FROM Servizi WHERE Autista = 0";
            PreparedStatement serviziSenzaAutistaStatement = connection.prepareStatement(serviziSenzaAutistaQuery);
            ResultSet serviziSenzaAutistaResultSet = serviziSenzaAutistaStatement.executeQuery();


            while (serviziSenzaAutistaResultSet.next()) {
                int idServizio = serviziSenzaAutistaResultSet.getInt("Id");
                String dataServizio = serviziSenzaAutistaResultSet.getString("Data");

                // Trova un autista volontario disponibile e non confermato per questa data
                int autistaDisponibile = trovaVolontarioDisponibileNonConfermato(dataServizio);

                if (autistaDisponibile != 0) {
                    // Cambia lo stato del volontario da "Non confermata" a "Reclutato"
                    cambiaStatoVolontario(autistaDisponibile, dataServizio);

                    String updateQuery = "UPDATE Servizi SET Autista = ? WHERE Id = ? AND Autista = 0";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setInt(1, autistaDisponibile);
                    updateStatement.setInt(2, idServizio);
                    updateStatement.executeUpdate();
                    System.out.println("Autista assegnato automaticamente al servizio con ID " + idServizio);
                    updateStatement.close();
                }
            }

            serviziSenzaAutistaStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void assegnaSoccorritoriAutomaticamente() {
        try {
            String serviziSenzaSoccorritoreQuery = "SELECT * FROM Servizi WHERE Soccorritore = 0";
            PreparedStatement serviziSenzaSoccorritoreStatement = connection.prepareStatement(serviziSenzaSoccorritoreQuery);
            ResultSet serviziSenzaSoccorritoreResultSet = serviziSenzaSoccorritoreStatement.executeQuery();

            while (serviziSenzaSoccorritoreResultSet.next()) {
                int idServizio = serviziSenzaSoccorritoreResultSet.getInt("Id");
                String dataServizio = serviziSenzaSoccorritoreResultSet.getString("Data");

                // Trova un soccorritore volontario disponibile e non confermato per questa data
                int soccorritoreDisponibile = trovaVolontarioDisponibileNonConfermato(dataServizio);

                if (soccorritoreDisponibile != 0) {
                    // Cambia lo stato del volontario da "Non confermata" a "Reclutato"
                    cambiaStatoVolontario(soccorritoreDisponibile, dataServizio);

                    String updateQuery = "UPDATE Servizi SET Soccorritore = ? WHERE Id = ? AND Soccorritore = 0";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setInt(1, soccorritoreDisponibile);
                    updateStatement.setInt(2, idServizio);
                    updateStatement.executeUpdate();
                    System.out.println("Soccorritore assegnato automaticamente al servizio con ID " + idServizio);
                    updateStatement.close();
                }
            }

            serviziSenzaSoccorritoreStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int trovaVolontarioDisponibileNonConfermato(String dataServizio) {
        try {
            String disponibilitaQuery = "SELECT matricola_volontario FROM Disponibilita WHERE data_disponibilita = ? AND confermata = 'Non confermata'";
            PreparedStatement disponibilitaStatement = connection.prepareStatement(disponibilitaQuery);
            disponibilitaStatement.setString(1, dataServizio);
            ResultSet disponibilitaResultSet = disponibilitaStatement.executeQuery();

            while (disponibilitaResultSet.next()) {
                int volontarioDisponibile = disponibilitaResultSet.getInt("matricola_volontario");
                // Verifica se il volontario è disponibile (non è già assegnato)
                if (!verificaVolontarioAssegnato(dataServizio, volontarioDisponibile)) {
                    return volontarioDisponibile;
                }
            }

            disponibilitaStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0; // Nessun volontario disponibile trovato
    }

    public static void cambiaStatoVolontario(int matricolaVolontario, String dataServizio) {
        try {
            String updateStatoQuery = "UPDATE Disponibilita SET confermata = 'Reclutato' WHERE matricola_volontario = ? AND data_disponibilita = ? AND confermata = 'Non confermata'";
            PreparedStatement updateStatoStatement = connection.prepareStatement(updateStatoQuery);
            updateStatoStatement.setInt(1, matricolaVolontario);
            updateStatoStatement.setString(2, dataServizio);
            updateStatoStatement.executeUpdate();
            updateStatoStatement.close();


            // Creazione della notifica
            String inserisciNotificaQuery = "INSERT INTO Notifiche (Matricola_Volontario, Giorno, Data_Invio, Letta) VALUES (?, ?, ?, ?)";
            PreparedStatement inserisciNotificaStatement = connection.prepareStatement(inserisciNotificaQuery);
            inserisciNotificaStatement.setInt(1, matricolaVolontario);
            inserisciNotificaStatement.setString(2, dataServizio);
            inserisciNotificaStatement.setDate(3, Date.valueOf(LocalDate.now())); // Imposta solo la data attuale
            inserisciNotificaStatement.setBoolean(4, false); // Inizialmente la notifica non è letta
            inserisciNotificaStatement.executeUpdate();
            inserisciNotificaStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }










    public static boolean verificaVolontarioAssegnato(String dataServizio, int matricolaVolontario) {
        try {
            String verificaAssegnazioneQuery = "SELECT Id FROM Servizi WHERE (Autista = ? OR Soccorritore = ?) AND Data = ?";
            PreparedStatement verificaAssegnazioneStatement = connection.prepareStatement(verificaAssegnazioneQuery);
            verificaAssegnazioneStatement.setInt(1, matricolaVolontario);
            verificaAssegnazioneStatement.setInt(2, matricolaVolontario);
            verificaAssegnazioneStatement.setString(3, dataServizio);
            ResultSet assegnazioneResultSet = verificaAssegnazioneStatement.executeQuery();

            boolean volontarioAssegnato = assegnazioneResultSet.next();
            assegnazioneResultSet.close();

            return volontarioAssegnato;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // In caso di errore, considera il volontario non assegnato
        }
    }


}
