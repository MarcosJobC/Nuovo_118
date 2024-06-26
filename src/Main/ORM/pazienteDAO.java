package Main.ORM;

import Main.BusinessLogic.menuController;
import Main.DomainModel.Paziente;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class pazienteDAO {
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

    public static void aggiungiPazienteDAO(Paziente paziente) {
        openConnection();
        try {
            String insertQuery = "INSERT INTO Pazienti (Nome, Cognome, DataNascita, LuogoNascita, IndirizzoResidenza) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, paziente.getNomePaziente());
            insertStatement.setString(2, paziente.getCognomePaziente());
            insertStatement.setDate(3, Date.valueOf(paziente.getDataNascita()));
            insertStatement.setString(4, paziente.getLuogoNascita());
            insertStatement.setString(5, paziente.getIndirizzoResidenza());

            insertStatement.executeUpdate();

            System.out.println("Paziente aggiunto con successo!");

            insertStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public static void aggiungiPazientedaServizioDAO(Paziente paziente) {
        openConnection();
        try {
            String insertQuery = "INSERT INTO Pazienti (Nome, Cognome, DataNascita, LuogoNascita, IndirizzoResidenza) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, paziente.getNomePaziente());
            insertStatement.setString(2, paziente.getCognomePaziente());
            insertStatement.setDate(3, Date.valueOf(paziente.getDataNascita()));
            insertStatement.setString(4, paziente.getLuogoNascita());
            insertStatement.setString(5, paziente.getIndirizzoResidenza());

            insertStatement.executeUpdate();

            System.out.println(" ");
            System.out.println(" ");
            System.out.println("Paziente aggiunto con successo, sei tornato in aggiungi servizio, ricerca il paziente!");

            insertStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }


    public static void modificaPazienteDAO(Scanner scanner) {
        openConnection();
        System.out.println("MODIFICA PAZIENTE:");

        try {
            // Visualizza la lista dei pazienti
            String listaPazientiQuery = "SELECT * FROM Pazienti";
            PreparedStatement listaPazientiStatement = connection.prepareStatement(listaPazientiQuery);
            ResultSet pazientiResultSet = listaPazientiStatement.executeQuery();

            System.out.println("Lista dei pazienti:");
            while (pazientiResultSet.next()) {
                int idPaziente = pazientiResultSet.getInt("ID");
                String nomePaziente = pazientiResultSet.getString("Nome");
                String cognomePaziente = pazientiResultSet.getString("Cognome");
                LocalDate dataNascita = pazientiResultSet.getDate("DataNascita").toLocalDate();
                String luogoNascita = pazientiResultSet.getString("LuogoNascita");
                String indirizzoResidenza = pazientiResultSet.getString("IndirizzoResidenza");

                System.out.println("ID: " + idPaziente + " | Nome: " + nomePaziente + " | Cognome: " + cognomePaziente +
                        " | Data di nascita: " + dataNascita + " | Luogo di nascita: " + luogoNascita +
                        " | Indirizzo di residenza: " + indirizzoResidenza);
            }
            System.out.println(" ");

            // Chiedi all'utente di inserire l'ID del paziente da modificare
            System.out.print("Inserisci l'ID del paziente da modificare: ");
            int idPazienteDaModificare = scanner.nextInt();
            scanner.nextLine(); // Consuma la newline rimanente

            String verificaQuery = "SELECT * FROM Pazienti WHERE ID = ?";
            PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
            verificaStatement.setInt(1, idPazienteDaModificare);
            ResultSet resultSet = verificaStatement.executeQuery();

            if (resultSet.next()) {
                // Il paziente esiste, procedi con la modifica

                String nomePaziente = resultSet.getString("Nome");
                String cognomePaziente = resultSet.getString("Cognome");
                LocalDate dataNascita = resultSet.getDate("DataNascita").toLocalDate();
                String luogoNascita = resultSet.getString("LuogoNascita");
                String indirizzoResidenza = resultSet.getString("IndirizzoResidenza");

                System.out.println("Il seguente paziente è stato trovato:");
                System.out.println("Nome: " + nomePaziente + " | Cognome: " + cognomePaziente +
                        " | Data di nascita: " + dataNascita + " | Luogo di nascita: " + luogoNascita +
                        " | Indirizzo di residenza: " + indirizzoResidenza);
                System.out.println(" ");

                // Richiedi i nuovi valori o lascia vuoto per non modificare
                System.out.print("Inserisci il nuovo nome (oppure lascia vuoto): ");
                String nuovoNome = scanner.nextLine();
                if (nuovoNome.isEmpty()) {
                    nuovoNome = nomePaziente;
                }

                System.out.print("Inserisci il nuovo cognome (oppure lascia vuoto): ");
                String nuovoCognome = scanner.nextLine();
                if (nuovoCognome.isEmpty()) {
                    nuovoCognome = cognomePaziente;
                }

                System.out.print("Inserisci la nuova data di nascita (formato YYYY-MM-DD) (oppure lascia vuoto): ");
                String nuovaDataNascitaString = scanner.nextLine();
                LocalDate nuovaDataNascita = nuovaDataNascitaString.isEmpty() ? dataNascita : LocalDate.parse(nuovaDataNascitaString);

                System.out.print("Inserisci il nuovo luogo di nascita (oppure lascia vuoto): ");
                String nuovoLuogoNascita = scanner.nextLine();
                if (nuovoLuogoNascita.isEmpty()) {
                    nuovoLuogoNascita = luogoNascita;
                }

                System.out.print("Inserisci il nuovo indirizzo di residenza (oppure lascia vuoto): ");
                String nuovoIndirizzoResidenza = scanner.nextLine();
                if (nuovoIndirizzoResidenza.isEmpty()) {
                    nuovoIndirizzoResidenza = indirizzoResidenza;
                }

                if (!nuovoNome.equals(nomePaziente) || !nuovoCognome.equals(cognomePaziente) ||
                        !nuovaDataNascita.equals(dataNascita) || !nuovoLuogoNascita.equals(luogoNascita) ||
                        !nuovoIndirizzoResidenza.equals(indirizzoResidenza)) {
                    String updateQuery = "UPDATE Pazienti SET Nome = ?, Cognome = ?, DataNascita = ?, LuogoNascita = ?, IndirizzoResidenza = ? WHERE ID = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

                    updateStatement.setString(1, nuovoNome);
                    updateStatement.setString(2, nuovoCognome);
                    updateStatement.setDate(3, Date.valueOf(nuovaDataNascita));
                    updateStatement.setString(4, nuovoLuogoNascita);
                    updateStatement.setString(5, nuovoIndirizzoResidenza);
                    updateStatement.setInt(6, idPazienteDaModificare);

                    // Esegui l'aggiornamento
                    updateStatement.executeUpdate();
                    System.out.println("Paziente modificato con successo!");
                    updateStatement.close();
                } else {
                    System.out.println("Nessuna modifica effettuata.");
                }

            } else {
                System.out.println("Nessun paziente trovato con l'ID fornito.");
            }
            resultSet.close();
            verificaStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        menuController.mostraMenuPazienti(scanner);
        closeConnection();
    }
    public static void eliminaPazienteDAO(Scanner scanner) {
        openConnection();
        try {
            // Visualizza la lista dei pazienti
            String listaPazientiQuery = "SELECT * FROM Pazienti";
            PreparedStatement listaPazientiStatement = connection.prepareStatement(listaPazientiQuery);
            ResultSet pazientiResultSet = listaPazientiStatement.executeQuery();

            System.out.println("Lista dei pazienti:");
            while (pazientiResultSet.next()) {
                int idPaziente = pazientiResultSet.getInt("ID");
                String nomePaziente = pazientiResultSet.getString("Nome");
                String cognomePaziente = pazientiResultSet.getString("Cognome");
                System.out.println("ID: " + idPaziente + " | Nome: " + nomePaziente + " | Cognome: " + cognomePaziente);
            }

            System.out.println(" ");
            System.out.print("Inserisci l'ID del paziente da eliminare (o premi 'q' per annullare): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("q")) {
                System.out.println("Operazione annullata.");
                menuController.mostraMenuPazienti(scanner);
                return; // Termina il metodo
            }

            try {
                int idPazienteDaEliminare = Integer.parseInt(input);

                String verificaQuery = "SELECT * FROM Pazienti WHERE ID = ?";
                PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
                verificaStatement.setInt(1, idPazienteDaEliminare);
                ResultSet resultSet = verificaStatement.executeQuery();

                if (resultSet.next()) {
                    // Il paziente esiste, procedi con l'eliminazione

                    String nomePaziente = resultSet.getString("Nome");
                    String cognomePaziente = resultSet.getString("Cognome");

                    System.out.println("Il seguente paziente sarà eliminato:");
                    System.out.println("Nome: " + nomePaziente + " | Cognome: " + cognomePaziente);
                    System.out.println(" ");

                    // Conferma l'eliminazione
                    System.out.print("Confermi l'eliminazione di questo paziente? [Verranno eliminati anche i servizi a lui connessi] (s/n): ");
                    String conferma = scanner.nextLine();

                    if (conferma.equalsIgnoreCase("s")) {
                        // Esegui l'eliminazione del servizio associato, se presente
                        String deleteServizioQuery = "DELETE FROM Servizi WHERE Paziente = ?";
                        PreparedStatement deleteServizioStatement = connection.prepareStatement(deleteServizioQuery);
                        deleteServizioStatement.setInt(1, idPazienteDaEliminare);
                        deleteServizioStatement.executeUpdate();
                        deleteServizioStatement.close();

                        // Esegui l'eliminazione del paziente
                        String deleteQuery = "DELETE FROM Pazienti WHERE ID = ?";
                        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                        deleteStatement.setInt(1, idPazienteDaEliminare);

                        // Esegui l'eliminazione
                        int rowCount = deleteStatement.executeUpdate();
                        deleteStatement.close();

                        if (rowCount > 0) {
                            System.out.println("Il paziente e il servizio associato sono stati eliminati con successo.");
                        } else {
                            System.out.println("Errore nell'eliminazione del paziente.");
                        }
                    } else {
                        System.out.println("Eliminazione annullata.");
                    }
                } else {
                    System.out.println("Nessun paziente trovato con l'ID fornito.");

                    // Chiedi nuovamente l'ID del paziente
                    eliminaPazienteDAO(scanner);
                }

                resultSet.close();
                verificaStatement.close();
            } catch (NumberFormatException e) {
                System.out.println("Input non valido. Inserisci un ID numerico valido o premi 'q' per annullare.");
                eliminaPazienteDAO(scanner);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(" ");
        System.out.println(" ");
        menuController.mostraMenuPazienti(scanner);
        closeConnection();
    }
    public static void visualizzaPazientiDAO(Scanner scanner) {
        openConnection();
        try {
            // Ottieni una lista di tutti i pazienti
            String listaPazientiQuery = "SELECT * FROM Pazienti";
            PreparedStatement listaPazientiStatement = connection.prepareStatement(listaPazientiQuery);
            ResultSet pazientiResultSet = listaPazientiStatement.executeQuery();

            System.out.println("Lista dei pazienti:");
            while (pazientiResultSet.next()) {
                int idPaziente = pazientiResultSet.getInt("ID");
                String nomePaziente = pazientiResultSet.getString("Nome");
                String cognomePaziente = pazientiResultSet.getString("Cognome");
                LocalDate dataNascita = pazientiResultSet.getDate("DataNascita").toLocalDate();
                String luogoNascita = pazientiResultSet.getString("LuogoNascita");
                String indirizzoResidenza = pazientiResultSet.getString("IndirizzoResidenza");

                System.out.println("ID: " + idPaziente + " | Nome: " + nomePaziente + " | Cognome: " + cognomePaziente);
                System.out.println("Data di Nascita: " + dataNascita + " | Luogo di Nascita: " + luogoNascita);
                System.out.println("Indirizzo di Residenza: " + indirizzoResidenza);
                System.out.println();
            }

            System.out.println("Premi un tasto qualsiasi per tornare al menu pazienti.");
            scanner.nextLine();
            menuController.mostraMenuPazienti(scanner);


            listaPazientiStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

}
