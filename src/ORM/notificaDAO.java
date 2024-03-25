package ORM;

import BusinessLogic.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class notificaDAO {

    private static Connection connection;

    public static void openConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                DatabaseConnection dbConnection = new DatabaseConnection();
                dbConnection.connectToDatabase();
                connection = dbConnection.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean ciSonoNotificheNonLetteDAO(int matricolaVolontario) {
        openConnection();
        try {
            String notificheQuery = "SELECT Id FROM Notifiche WHERE Matricola_Volontario = ? AND Letta = false";
            PreparedStatement notificheStatement = connection.prepareStatement(notificheQuery);
            notificheStatement.setInt(1, matricolaVolontario);
            ResultSet notificheResultSet = notificheStatement.executeQuery();

            boolean ciSonoNotifiche = notificheResultSet.next();
            notificheResultSet.close();

            closeConnection();
            return ciSonoNotifiche;
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
    }


    public static void segnaNotificheComeLetteDAO(int matricolaVolontario) {
        openConnection();
        try {
            String segnaLetteQuery = "UPDATE Notifiche SET Letta = true WHERE Matricola_Volontario = ?";
            PreparedStatement segnaLetteStatement = connection.prepareStatement(segnaLetteQuery);
            segnaLetteStatement.setInt(1, matricolaVolontario);
            segnaLetteStatement.executeUpdate();
            segnaLetteStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }


    public static void visualizzaNotificheDAO(Scanner scanner, int matricolaVolontario) {
        openConnection();
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
            segnaNotificheComeLetteDAO(matricolaVolontario);
            notificheStatement.close();

            System.out.println("\nPremi qualsiasi tasto per tornare al menu precedente...");
            scanner.nextLine();
            menuController.mostraMenuUtenteNormale(scanner, matricolaVolontario);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }
    public static void inviaNotificaVolontarioDAO(int matricolaVolontario, String giorno) {
        openConnection();
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
        closeConnection();
    }
    public static void eliminaNotificheLetteDAO() {
        openConnection();
        try {
            String query = "DELETE FROM Notifiche WHERE letta = 'true'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }



}
