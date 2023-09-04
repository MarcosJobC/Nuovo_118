import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class pazientiManager {

    private static Connection connection;
    public pazientiManager(Connection connection) {
        this.connection = connection;
    }



    //METODI PAZIENTI
    public static void aggiungiPaziente(Scanner scanner) {
        System.out.println(" ");
        System.out.println("INSERIMENTO NUOVO PAZIENTE:");
        try {
            String nomePaziente, cognomePaziente, dataNascitaString, luogoNascita, indirizzoResidenza;
            LocalDate dataNascita = null;

            // Richiedi e valida il nome del paziente
            do {
                System.out.print("Inserisci il nome del paziente: ");
                nomePaziente = scanner.nextLine();
                if (nomePaziente.isEmpty()) {
                    System.out.println("Il campo nome non può essere lasciato vuoto.");
                }
            } while (nomePaziente.isEmpty());

            // Richiedi e valida il cognome del paziente
            do {
                System.out.print("Inserisci il cognome del paziente: ");
                cognomePaziente = scanner.nextLine();
                if (cognomePaziente.isEmpty()) {
                    System.out.println("Il campo cognome non può essere lasciato vuoto.");
                }
            } while (cognomePaziente.isEmpty());

            // Richiedi e valida la data di nascita del paziente
            do {
                System.out.print("Inserisci la data di nascita del paziente (formato dd-MM-yyyy): ");
                dataNascitaString = scanner.nextLine();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                try {
                    dataNascita = LocalDate.parse(dataNascitaString, formatter);
                    if (dataNascita.isAfter(LocalDate.now())) {
                        System.out.println("La data di nascita non può essere successiva a oggi.");
                        dataNascita = null;
                    }
                } catch (DateTimeParseException e) {
                    dataNascita = null;
                }
                if (dataNascita == null) {
                    System.out.println("Inserisci una data di nascita valida.");
                }
            } while (dataNascita == null);

            // Richiedi e valida il luogo di nascita del paziente
            do {
                System.out.print("Inserisci il luogo di nascita del paziente: ");
                luogoNascita = scanner.nextLine();
                if (luogoNascita.isEmpty()) {
                    System.out.println("Il campo luogo di nascita non può essere lasciato vuoto.");
                }
            } while (luogoNascita.isEmpty());

            // Richiedi e valida l'indirizzo di residenza del paziente
            do {
                System.out.print("Inserisci l'indirizzo di residenza del paziente: ");
                indirizzoResidenza = scanner.nextLine();
                if (indirizzoResidenza.isEmpty()) {
                    System.out.println("Il campo indirizzo di residenza non può essere lasciato vuoto.");
                }
            } while (indirizzoResidenza.isEmpty());

            String insertQuery = "INSERT INTO Pazienti (Nome, Cognome, DataNascita, LuogoNascita, IndirizzoResidenza) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, nomePaziente);
            insertStatement.setString(2, cognomePaziente);
            insertStatement.setDate(3, Date.valueOf(dataNascita));
            insertStatement.setString(4, luogoNascita);
            insertStatement.setString(5, indirizzoResidenza);

            insertStatement.executeUpdate();

            System.out.println("Paziente aggiunto con successo!");

            insertStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(" ");
        System.out.println(" ");
        menuManager.mostraMenuPazienti(scanner);
    }
    public static void modificaPaziente(Scanner scanner) {
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
        menuManager.mostraMenuPazienti(scanner);
    }
    public static void eliminaPaziente(Scanner scanner) {

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

            // Chiedi all'utente di inserire l'ID del paziente da eliminare
            System.out.print("Inserisci l'ID del paziente da eliminare: ");
            int idPazienteDaEliminare = scanner.nextInt();
            scanner.nextLine(); // Consuma la newline rimanente

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
                System.out.print("Confermi l'eliminazione di questo paziente? (s/n): ");
                String conferma = scanner.nextLine();

                if (conferma.equalsIgnoreCase("s")) {
                    String deleteQuery = "DELETE FROM Pazienti WHERE ID = ?";
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                    deleteStatement.setInt(1, idPazienteDaEliminare);

                    // Esegui l'eliminazione
                    deleteStatement.executeUpdate();
                    System.out.println("Paziente eliminato con successo!");
                    deleteStatement.close();
                } else {
                    System.out.println("Eliminazione annullata.");
                }
            } else {
                System.out.println("Nessun paziente trovato con l'ID fornito.");
                eliminaPaziente(scanner);
            }
            resultSet.close();
            verificaStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(" ");
        System.out.println(" ");
        menuManager.mostraMenuPazienti(scanner);
    }
    public static void visualizzaPazienti(Scanner scanner) {
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
            menuManager.mostraMenuPazienti(scanner);


            listaPazientiStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
