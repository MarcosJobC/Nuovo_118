import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.ResultSet;

public class notificheManager {

    private static Connection connection;
    public notificheManager(Connection connection) {
        this.connection = connection;
    }


    public static boolean ciSonoNotificheNonLette(int matricolaVolontario) {
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
        try {
            String notificheQuery = "SELECT * FROM Notifiche WHERE Matricola_Volontario = ? ORDER BY Data_Invio DESC";
            PreparedStatement notificheStatement = connection.prepareStatement(notificheQuery);
            notificheStatement.setInt(1, matricolaVolontario);
            ResultSet notificheResultSet = notificheStatement.executeQuery();

            System.out.println("Notifiche:");
            while (notificheResultSet.next()) {
                int idNotifica = notificheResultSet.getInt("Id");
                String giornoAssegnato = notificheResultSet.getString("Giorno");
                boolean letta = notificheResultSet.getBoolean("Letta");

                String statoLetta = letta ? "Letta" : "Non letta";
                System.out.println(idNotifica + ". [" + statoLetta + "] " + "Ti sono stati assegnati dei servizi per il giorno " + giornoAssegnato + ", vai sulla lista servizi per visionarli.");
            }

            // Segna le notifiche come lette
            segnaNotificheComeLette(matricolaVolontario);

            notificheStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





}
