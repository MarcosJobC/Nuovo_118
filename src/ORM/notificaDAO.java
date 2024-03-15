package ORM;

import BusinessLogic.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class notificaDAO {

    private static Connection connection;
    public notificaDAO(Connection connection) {
        this.connection = connection;
    }



    public static boolean ciSonoNotificheNonLette(int matricolaVolontario) {
        //TODO Sposta metà in MenuController
        try {
            String notificheQuery = "SELECT Id FROM Notifiche WHERE Matricola_Volontario = ? AND Letta = false";
            PreparedStatement notificheStatement = connection.prepareStatement(notificheQuery);
            notificheStatement.setInt(1, matricolaVolontario);
            ResultSet notificheResultSet = notificheStatement.executeQuery();

            boolean ciSonoNotifiche = notificheResultSet.next();
            notificheResultSet.close();

            return ciSonoNotifiche;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void segnaNotificheComeLette(int matricolaVolontario) {
        try {
            String segnaLetteQuery = "UPDATE Notifiche SET Letta = true WHERE Matricola_Volontario = ?";
            PreparedStatement segnaLetteStatement = connection.prepareStatement(segnaLetteQuery);
            segnaLetteStatement.setInt(1, matricolaVolontario);
            segnaLetteStatement.executeUpdate();
            segnaLetteStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void visualizzaNotifiche(Scanner scanner, int matricolaVolontario) {
        //TODO Sposta metà in VolontarioController
        try {
            String notificheQuery = "SELECT * FROM Notifiche WHERE Matricola_Volontario = ? ORDER BY Data_Invio DESC";
            PreparedStatement notificheStatement = connection.prepareStatement(notificheQuery);
            notificheStatement.setInt(1, matricolaVolontario);
            ResultSet notificheResultSet = notificheStatement.executeQuery();

            System.out.println("NOTIFICHE ASSEGNAZIONE SERVIZI:");
            while (notificheResultSet.next()) {
                int idNotifica = notificheResultSet.getInt("Id");
                String giornoAssegnato = notificheResultSet.getString("Giorno");
                boolean letta = notificheResultSet.getBoolean("Letta");

                String statoLetta = letta ? "Letta" : "Non letta";
                System.out.println("["+statoLetta+"] " + "Servizi assegnati per il giorno " + giornoAssegnato + ", vai sulla lista servizi per visionarli.");
            }

            // Segna le notifiche come lette
            segnaNotificheComeLette(matricolaVolontario);
            notificheStatement.close();

            System.out.println("\nPremi qualsiasi tasto per tornare al menu precedente...");
            scanner.nextLine();
            menuController.mostraMenuUtenteNormale(scanner, matricolaVolontario);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static void inviaNotificaVolontario(int matricolaVolontario, String giorno) {
        try {
            String inserisciNotificaQuery = "INSERT INTO Notifiche (Matricola_Volontario, Giorno, Data_Invio, Letta) VALUES (?, ?, ?, ?)";
            PreparedStatement inserisciNotificaStatement = connection.prepareStatement(inserisciNotificaQuery);
            inserisciNotificaStatement.setInt(1, matricolaVolontario);
            inserisciNotificaStatement.setString(2, giorno);
            inserisciNotificaStatement.setDate(3, Date.valueOf(LocalDate.now())); // Imposta la data attuale
            inserisciNotificaStatement.setBoolean(4, false); // Inizialmente la notifica non è letta
            inserisciNotificaStatement.executeUpdate();
            inserisciNotificaStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void eliminaNotificheLette() {
        try {
            String query = "DELETE FROM Notifiche WHERE letta = 'true'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
