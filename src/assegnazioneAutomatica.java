import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;


public class assegnazioneAutomatica {

    private static Connection connection;
    public assegnazioneAutomatica(Connection connection) {
        this.connection = connection;
    }

    //ASSEGNAZIONE AUTOMATICA VOLONTARI A SERVIZI
    public static void assegnaAutomaticamenteSOCIALI(Scanner scanner) {
        //Vengono assegnati prima tutti gli autisti e successivamente tutti i soccoritori
        assegnaAutistiAutomaticamenteSOCIALI();
        assegnaSoccorritoriAutomaticamenteSOCIALI();
        System.out.println(" ");
        System.out.println(" ");
        menuManager.mostraMenuServizi(scanner);
    }
    public static void assegnaAutistiAutomaticamenteSOCIALI() {
        try {

            String serviziSenzaAutistaQuery = "SELECT * FROM Servizi WHERE Autista = 0";
            PreparedStatement serviziSenzaAutistaStatement = connection.prepareStatement(serviziSenzaAutistaQuery);
            ResultSet serviziSenzaAutistaResultSet = serviziSenzaAutistaStatement.executeQuery();


            while (serviziSenzaAutistaResultSet.next()) {
                int idServizio = serviziSenzaAutistaResultSet.getInt("Id");
                String dataServizio = serviziSenzaAutistaResultSet.getString("Data");
                LocalTime orarioServizio = serviziSenzaAutistaResultSet.getTime("orario").toLocalTime();

                // Trova un autista volontario disponibile e non confermato per questa data
                int autistaDisponibile = trovaVolontarioDisponibileNonConfermatoSOCIALI(dataServizio, orarioServizio);

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
    public static void assegnaSoccorritoriAutomaticamenteSOCIALI() {
        try {
            String serviziSenzaSoccorritoreQuery = "SELECT * FROM Servizi WHERE Soccorritore = 0";
            PreparedStatement serviziSenzaSoccorritoreStatement = connection.prepareStatement(serviziSenzaSoccorritoreQuery);
            ResultSet serviziSenzaSoccorritoreResultSet = serviziSenzaSoccorritoreStatement.executeQuery();

            while (serviziSenzaSoccorritoreResultSet.next()) {
                int idServizio = serviziSenzaSoccorritoreResultSet.getInt("Id");
                String dataServizio = serviziSenzaSoccorritoreResultSet.getString("Data");
                LocalTime orarioServizio = serviziSenzaSoccorritoreResultSet.getTime("orario").toLocalTime();

                // Trova un soccorritore volontario disponibile e non confermato per questa data
                int soccorritoreDisponibile = trovaVolontarioDisponibileNonConfermatoSOCIALI(dataServizio, orarioServizio);

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
    public static int trovaVolontarioDisponibileNonConfermatoSOCIALI(String dataServizio, LocalTime orarioServizio) {
        try {
            String disponibilitaQuery = "SELECT matricola_volontario, ora_inizio, ora_fine " +
                    "FROM Disponibilita WHERE data_disponibilita = ? " +
                    "AND confermata = 'Non confermata' " +
                    "AND tipologia = 'Servizi sociali' " +
                    "AND ? BETWEEN ora_inizio AND ora_fine"; // Aggiungi il vincolo sull'orario
            PreparedStatement disponibilitaStatement = connection.prepareStatement(disponibilitaQuery);
            disponibilitaStatement.setString(1, dataServizio);
            disponibilitaStatement.setTime(2, Time.valueOf(orarioServizio)); // Usa Time.valueOf per convertire LocalTime in Time
            ResultSet disponibilitaResultSet = disponibilitaStatement.executeQuery();

            while (disponibilitaResultSet.next()) {
                LocalTime oraInizioDisponibilita = disponibilitaResultSet.getTime("ora_inizio").toLocalTime();
                LocalTime oraFineDisponibilita = disponibilitaResultSet.getTime("ora_fine").toLocalTime();

                int volontarioDisponibile = disponibilitaResultSet.getInt("matricola_volontario");
                // Verifica se il volontario è disponibile (non è già assegnato) e se l'orario è corretto
                if (!verificaVolontarioAssegnato(dataServizio, volontarioDisponibile) &&
                        orarioServizio.isAfter(oraInizioDisponibilita) &&
                        orarioServizio.isBefore(oraFineDisponibilita)) {
                    return volontarioDisponibile;
                }
            }

            disponibilitaStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0; // Nessun volontario disponibile con tipologia "Servizi sociali" trovato
    }

    //ASSEGNAZIONE AUTOMATICA VOLONTARI A EMERGENZA
    public static void assegnaAutomaticamenteEMERGENZA(Scanner scanner) {
        try {
            String emergenzeSenzaSoccorritoreQuery = "SELECT * FROM Emergenze WHERE ID NOT IN (SELECT EmergenzaID FROM SoccorritoriEmergenza)";
            PreparedStatement emergenzeSenzaSoccorritoreStatement = connection.prepareStatement(emergenzeSenzaSoccorritoreQuery);
            ResultSet emergenzeSenzaSoccorritoreResultSet = emergenzeSenzaSoccorritoreStatement.executeQuery();

            while (emergenzeSenzaSoccorritoreResultSet.next()) {
                int idEmergenza = emergenzeSenzaSoccorritoreResultSet.getInt("ID");
                String dataEmergenza = emergenzeSenzaSoccorritoreResultSet.getString("Data");
                String turnoEmergenza = emergenzeSenzaSoccorritoreResultSet.getString("Turno");

                // Trova un volontario disponibile e non confermato per questa data ed emergenza
                int volontarioDisponibile = trovaVolontarioDisponibileNonConfermatoEMERGENZA(dataEmergenza, turnoEmergenza);

                if (volontarioDisponibile != 0) {
                    // Cambia lo stato del volontario da "Non confermata" a "Reclutato"
                    cambiaStatoVolontario(volontarioDisponibile, dataEmergenza);

                    // Assegna il volontario all'emergenza
                    String insertSoccorritoreEmergenzaQuery = "INSERT INTO SoccorritoriEmergenza (EmergenzaID, SoccorritoreID) VALUES (?, ?)";
                    PreparedStatement insertSoccorritoreEmergenzaStatement = connection.prepareStatement(insertSoccorritoreEmergenzaQuery);
                    insertSoccorritoreEmergenzaStatement.setInt(1, idEmergenza);
                    insertSoccorritoreEmergenzaStatement.setInt(2, volontarioDisponibile);
                    insertSoccorritoreEmergenzaStatement.executeUpdate();
                    System.out.println("Volontario assegnato automaticamente all'emergenza con ID " + idEmergenza);
                    insertSoccorritoreEmergenzaStatement.close();
                }
            }

            emergenzeSenzaSoccorritoreStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(" ");
        System.out.println(" ");
        menuManager.mostraMenuAdmin(scanner);
    }
    public static int trovaVolontarioDisponibileNonConfermatoEMERGENZA(String dataEmergenza, String turnoEmergenza) {
        try {
            String disponibilitaQuery = "SELECT matricola_volontario, turno_emergenza " +
                    "FROM Disponibilita WHERE data_disponibilita = ? " +
                    "AND confermata = 'Non confermata' " +
                    "AND tipologia = 'Emergenza' " +
                    "AND turno_emergenza = ?"; // Aggiungi il vincolo sul turno dell'emergenza
            PreparedStatement disponibilitaStatement = connection.prepareStatement(disponibilitaQuery);
            disponibilitaStatement.setString(1, dataEmergenza);
            disponibilitaStatement.setString(2, turnoEmergenza);
            ResultSet disponibilitaResultSet = disponibilitaStatement.executeQuery();

            while (disponibilitaResultSet.next()) {
                int volontarioDisponibile = disponibilitaResultSet.getInt("matricola_volontario");
                String turnoDisponibilita = disponibilitaResultSet.getString("turno_emergenza");

                // Verifica se il volontario è disponibile (non è già assegnato) e se il turno è corretto
                if (!verificaVolontarioAssegnato(dataEmergenza, volontarioDisponibile) &&
                        turnoEmergenza.equals(turnoDisponibilita)) {
                    return volontarioDisponibile;
                }
            }

            disponibilitaStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0; // Nessun volontario disponibile con tipologia "Emergenza" e turno corretto trovato per l'emergenza specifica
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
            notificheManager.inviaNotificaVolontario(matricolaVolontario, dataServizio);

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
