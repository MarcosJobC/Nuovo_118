import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class mezziManager {

    private static Connection connection;
    public mezziManager(Connection connection) {
        this.connection = connection;
    }




    //GESTIONE MEZZI
    public static void aggiungiMezzo(Scanner scanner) {
        System.out.println("Inserisci la sigla del mezzo:");
        String siglaMezzo = scanner.nextLine();

        System.out.println("Inserisci la targa del mezzo:");
        String targa = scanner.nextLine();

        System.out.println("Seleziona la tipologia del mezzo:");
        String tipologia;
        System.out.println("1. Auto");
        System.out.println("2. Ambulanza");
        System.out.println("3. Mezzo attrezzato");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                tipologia = "auto";
                break;
            case 2:
                tipologia = "ambulanza";
                break;
            case 3:
                tipologia = "mezzo attrezzato";
                break;
            default:
                System.out.println("Scelta non valida.");
                return;
        }

        try {
            String query = "INSERT INTO Mezzi (Sigla_mezzo, targa, tipologia) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, siglaMezzo);
            preparedStatement.setString(2, targa);
            preparedStatement.setString(3, tipologia);
            preparedStatement.executeUpdate();

            System.out.println("Mezzo aggiunto con successo!");

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void modificaMezzo(Scanner scanner) {
        try {
            String query = "SELECT * FROM Mezzi";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Lista dei mezzi:");
            while (resultSet.next()) {
                String siglaMezzo = resultSet.getString("Sigla_mezzo");
                String targa = resultSet.getString("targa");
                String tipologia = resultSet.getString("tipologia");
                System.out.println(siglaMezzo + " - " + targa + " - " + tipologia);
            }

            System.out.println("Inserisci la sigla del mezzo da modificare:");
            String siglaMezzo = scanner.nextLine();

            String verificaQuery = "SELECT * FROM Mezzi WHERE Sigla_mezzo = ?";
            PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
            verificaStatement.setString(1, siglaMezzo);
            ResultSet resultSetVerifica = verificaStatement.executeQuery();

            if (resultSetVerifica.next()) {
                System.out.println("Mezzo trovato. Inserisci i nuovi dati:");

                System.out.println("Nuova targa del mezzo (lascia vuoto per mantenere quella attuale):");
                String nuovaTarga = scanner.nextLine();
                if (nuovaTarga.isEmpty()) {
                    nuovaTarga = resultSetVerifica.getString("targa");
                }

                System.out.println("Seleziona la tipologia del mezzo:");
                String nuovaTipologia = "";
                System.out.println("1. Auto");
                System.out.println("2. Ambulanza");
                System.out.println("3. Mezzo attrezzato");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        nuovaTipologia = "auto";
                        break;
                    case 2:
                        nuovaTipologia = "ambulanza";
                        break;
                    case 3:
                        nuovaTipologia = "mezzo attrezzato";
                        break;
                }

                String updateQuery = "UPDATE Mezzi SET targa = ?, tipologia = ? WHERE Sigla_mezzo = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setString(1, nuovaTarga);
                updateStatement.setString(2, nuovaTipologia);
                updateStatement.setString(3, siglaMezzo);
                updateStatement.executeUpdate();

                System.out.println("Mezzo modificato con successo!");

                updateStatement.close();
            } else {
                System.out.println("Mezzo non trovato.");
                modificaMezzo(scanner);
            }

            verificaStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void eliminaMezzo(Scanner scanner) {
        try {
            String query = "SELECT * FROM Mezzi";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Lista dei mezzi:");
            while (resultSet.next()) {
                String siglaMezzo = resultSet.getString("Sigla_mezzo");
                String targa = resultSet.getString("targa");
                String tipologia = resultSet.getString("tipologia");
                System.out.println(siglaMezzo + " - " + targa + " - " + tipologia);
            }

            System.out.println("Inserisci la sigla del mezzo da eliminare:");
            String siglaMezzo = scanner.nextLine();

            String verificaQuery = "SELECT * FROM Mezzi WHERE Sigla_mezzo = ?";
            PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
            verificaStatement.setString(1, siglaMezzo);
            ResultSet resultSetVerifica = verificaStatement.executeQuery();

            if (resultSetVerifica.next()) {
                System.out.println("Mezzo trovato. Sei sicuro di volerlo eliminare? (s/n)");
                String conferma = scanner.nextLine();

                if (conferma.equalsIgnoreCase("s")) {
                    String deleteQuery = "DELETE FROM Mezzi WHERE Sigla_mezzo = ?";
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                    deleteStatement.setString(1, siglaMezzo);
                    deleteStatement.executeUpdate();

                    System.out.println("Mezzo eliminato con successo!");
                } else {
                    System.out.println("Operazione di eliminazione annullata.");
                }
            } else {
                System.out.println("Mezzo non trovato.");
                eliminaMezzo(scanner);
            }

            verificaStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
