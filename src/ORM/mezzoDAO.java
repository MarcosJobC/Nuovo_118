package ORM;

import BusinessLogic.menuController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class mezzoDAO {

    private static Connection connection;
    public mezzoDAO(Connection connection) {
        this.connection = connection;
    }




    //GESTIONE MEZZI
    // TODO MODIFICATI
    public static void aggiungiMezzoDAO(String siglaMezzo, String targa,String tipologia ) {

        try {
            String query = "INSERT INTO Mezzi (Sigla_mezzo, targa, tipologia) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, siglaMezzo);
            preparedStatement.setString(2, targa);
            preparedStatement.setString(3, tipologia);
            preparedStatement.executeUpdate();

            System.out.println("BusinessLogic.Mezzo aggiunto con successo!");
            System.out.println(" ");
            System.out.println(" ");

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // TODO NON MODIFICATI
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
                System.out.println("Sigla mezzo: "+siglaMezzo + " | Targa:  " + targa + " | Tipologia: " + tipologia);
            }

            System.out.println(" ");
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

                String nuovaTipologia = "";
                do {
                    System.out.println("Seleziona la tipologia del mezzo:");
                    System.out.println("1. Auto");
                    System.out.println("2. Ambulanza");
                    System.out.println("3. BusinessLogic.Mezzo attrezzato");
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    switch (choice) {
                        case 1:
                            nuovaTipologia = "Auto";
                            break;
                        case 2:
                            nuovaTipologia = "Ambulanza";
                            break;
                        case 3:
                            nuovaTipologia = "BusinessLogic.Mezzo attrezzato";
                            break;
                        default:
                            System.out.println("Scelta non valida. Devi selezionare 1, 2 o 3.");
                    }
                } while (nuovaTipologia.isEmpty());

                String updateQuery = "UPDATE Mezzi SET targa = ?, tipologia = ? WHERE Sigla_mezzo = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setString(1, nuovaTarga);
                updateStatement.setString(2, nuovaTipologia);
                updateStatement.setString(3, siglaMezzo);
                updateStatement.executeUpdate();

                System.out.println("BusinessLogic.Mezzo modificato con successo!");
                System.out.println(" ");
                System.out.println(" ");
                updateStatement.close();
            } else {
                System.out.println("BusinessLogic.Mezzo non trovato.");
                modificaMezzo(scanner);
            }

            verificaStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        menuController.mostraMenuMezzi(scanner);
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

            boolean mezzoTrovato = false;

            while (!mezzoTrovato) {
                System.out.println("Inserisci la sigla del mezzo da eliminare o premi 'q' per tornare indietro:");
                String siglaMezzo = scanner.nextLine();

                if (siglaMezzo.equalsIgnoreCase("q")) {
                    System.out.println("Annullamento dell'operazione.");
                    System.out.println(" ");
                    System.out.println(" ");
                    mezzoTrovato = true;
                } else {
                    String verificaQuery = "SELECT * FROM Mezzi WHERE Sigla_mezzo = ?";
                    PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
                    verificaStatement.setString(1, siglaMezzo);
                    ResultSet resultSetVerifica = verificaStatement.executeQuery();

                    if (resultSetVerifica.next()) {
                        System.out.println("BusinessLogic.Mezzo trovato. Sei sicuro di volerlo eliminare? (s/n)");
                        String conferma = scanner.nextLine();

                        if (conferma.equalsIgnoreCase("s")) {
                            String deleteQuery = "DELETE FROM Mezzi WHERE Sigla_mezzo = ?";
                            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                            deleteStatement.setString(1, siglaMezzo);
                            deleteStatement.executeUpdate();

                            System.out.println("BusinessLogic.Mezzo eliminato con successo!");
                            System.out.println(" ");
                            System.out.println(" ");
                        } else {
                            System.out.println("Operazione di eliminazione annullata.");
                        }

                        mezzoTrovato = true;
                    } else {
                        System.out.println("BusinessLogic.Mezzo non trovato.");
                    }

                    verificaStatement.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        menuController.mostraMenuMezzi(scanner);
    }

}
