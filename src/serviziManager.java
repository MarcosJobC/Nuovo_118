import java.sql.*;
import java.time.LocalTime;
import java.util.Scanner;

public class serviziManager {

    private static Connection connection;
    public serviziManager(Connection connection) {
        this.connection = connection;
    }



    //GESTIONE SERVIZI
    public static void aggiungiServizio(Scanner scanner) {
        System.out.println("Inserisci la data del servizio (dd-mm-yyyy):");
        String dataServizio = scanner.nextLine();

        System.out.println("Inserisci l'orario del servizio (HH:MM):");
        String orarioString = scanner.nextLine();
        LocalTime localTime = LocalTime.parse(orarioString);
        Time orarioTime = Time.valueOf(localTime);

        String paziente = "";
        while (paziente.isEmpty()) {
            System.out.print("Inserisci il nome del paziente: ");
            paziente = scanner.nextLine();

            if (paziente.isEmpty()) {
                System.out.println("Il nome del paziente non può essere lasciato vuoto.");
            }
        }

        boolean siglaValida = false;
        String siglaMezzo = "";

        while (!siglaValida) {
            System.out.print("Inserisci la sigla del mezzo: ");
            siglaMezzo = scanner.nextLine();

            try {
                String verificaQuery = "SELECT * FROM Mezzi WHERE Sigla_mezzo = ?";
                PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
                verificaStatement.setString(1, siglaMezzo);
                ResultSet resultSet = verificaStatement.executeQuery();

                if (resultSet.next()) {
                    // La sigla del mezzo esiste, interrompi il ciclo
                    siglaValida = true;
                } else {
                    System.out.println("Il mezzo non risulta presente in deposito.");
                }

                verificaStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        System.out.print("Inserisci la matricola dell'autista (oppure lascia vuoto per non modificare): ");
        String inputAutista = scanner.nextLine();
        int Autista = 0; // Inizializza l'ID dell'autista a 0
        if (!inputAutista.isEmpty()) {
            Autista = Integer.parseInt(inputAutista);
        }

        System.out.print("Inserisci la matricola del soccorritore (oppure lascia vuoto per non modificare): ");
        String inputSoccorritore = scanner.nextLine();
        int Soccorritore = 0; // Inizializza l'ID del soccorritore a 0
        if (!inputSoccorritore.isEmpty()) {
            Soccorritore = Integer.parseInt(inputSoccorritore);
        }

        try {
            String query = "INSERT INTO Servizi (Data, Orario, Paziente, Sigla_Mezzo, Autista, Soccorritore) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, dataServizio);
            preparedStatement.setTime(2, orarioTime);
            preparedStatement.setString(3, paziente);
            preparedStatement.setString(4, siglaMezzo);
            preparedStatement.setInt(5, Autista);
            preparedStatement.setInt(6, Soccorritore);

            preparedStatement.executeUpdate();

            System.out.println("Servizio aggiunto con successo!");

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void modificaServizio(Scanner scanner) {
        System.out.println("Inserisci la data del servizio da modificare (dd-mm-yyyy):");
        String dataServizio = scanner.nextLine();

        System.out.println("Inserisci l'orario del servizio da modificare (HH:MM):");
        String orarioString = scanner.nextLine();
        LocalTime localTime = LocalTime.parse(orarioString);
        Time orarioTime = Time.valueOf(localTime);

        System.out.print("Inserisci il nome del paziente del servizio da modificare: ");
        String paziente = scanner.nextLine();

        try {
            String verificaQuery = "SELECT * FROM Servizi WHERE Data = ? AND Orario = ? AND Paziente = ?";
            PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
            verificaStatement.setString(1, dataServizio);
            verificaStatement.setTime(2, orarioTime);
            verificaStatement.setString(3, paziente);
            ResultSet resultSet = verificaStatement.executeQuery();



            if (resultSet.next()) {
                // Il servizio esiste, procedi con la modifica
                String siglaMezzo = resultSet.getString("Sigla_Mezzo");
                int Autista = resultSet.getInt("Autista");
                int Soccorritore = resultSet.getInt("Soccorritore");

                System.out.print("Il seguente servizio è stato trovato:");
                System.out.print("  Data: " + dataServizio);
                System.out.print("  Orario: " + orarioString);
                System.out.print("  Paziente: " + paziente);
                System.out.print("  Sigla Mezzo: " + siglaMezzo);
                System.out.print("  Autista: " + Autista);
                System.out.println("  Soccorritore: " + Soccorritore);
                System.out.println(" ");

                // Richiedi i nuovi valori o lascia vuoto per non modificare
                System.out.print("Inserisci la nuova data (oppure lascia vuoto): ");
                String nuovaData = scanner.nextLine();

                System.out.print("Inserisci il nuovo orario (oppure lascia vuoto): ");
                String nuovoOrarioString = scanner.nextLine();
                LocalTime nuovoLocalTime = nuovoOrarioString.isEmpty() ? localTime : LocalTime.parse(nuovoOrarioString);
                Time nuovoOrarioTime = Time.valueOf(nuovoLocalTime);

                System.out.print("Inserisci il nuovo nome del paziente (oppure lascia vuoto): ");
                String nuovoPaziente = scanner.nextLine();
                System.out.print("Inserisci il nuovo mezzo (oppure lascia vuoto): ");
                String nuovoMezzo = scanner.nextLine();
                // Verifica se il mezzo esiste nella tabella Mezzi
                while (!nuovoMezzo.isEmpty()) {
                    try {
                        String verificaMezzoQuery = "SELECT * FROM Mezzi WHERE Sigla_mezzo = ?";
                        PreparedStatement verificaMezzoStatement = connection.prepareStatement(verificaMezzoQuery);
                        verificaMezzoStatement.setString(1, nuovoMezzo);
                        ResultSet mezzoResultSet = verificaMezzoStatement.executeQuery();

                        if (mezzoResultSet.next()) {
                            // La sigla del mezzo esiste, interrompi il ciclo
                            break;
                        } else {
                            System.out.println("La sigla del mezzo non esiste. Reinserisci la sigla corretta o lascia vuoto per non modificare.");
                            nuovoMezzo = scanner.nextLine();
                        }

                        verificaMezzoStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                int nuovoAutista = Autista; // Inizializza con il valore attuale
                System.out.print("Inserisci la matricola dell'autista (oppure lascia vuoto per non modificare): ");
                String inputAutista = scanner.nextLine();
                if (!inputAutista.isEmpty()) {
                    int idAutista = Integer.parseInt(inputAutista);
                    if (verificaEsistenzaVolontario(idAutista)) {
                        nuovoAutista = idAutista;
                    } else {
                        System.out.println("L'ID dell'autista inserito non esiste.");
                        modificaServizio(scanner);
                        return; // Interrompi il metodo senza eseguire l'aggiornamento
                    }
                }

                int nuovoSoccorritore = Soccorritore; // Inizializza con il valore attuale
                System.out.print("Inserisci la matricola del soccorritore (oppure lascia vuoto per non modificare): ");
                String inputSoccorritore = scanner.nextLine();
                if (!inputSoccorritore.isEmpty()) {
                    int idSoccorritore = Integer.parseInt(inputSoccorritore);
                    if (verificaEsistenzaVolontario(idSoccorritore)) {
                        nuovoSoccorritore = idSoccorritore;
                    } else {
                        System.out.println("L'ID del soccorritore inserito non esiste.");
                        modificaServizio(scanner);
                        return; // Interrompi il metodo senza eseguire l'aggiornamento
                    }
                }


                if (!nuovaData.isEmpty() || !nuovoOrarioString.isEmpty() || !nuovoPaziente.isEmpty() ||
                        !nuovoMezzo.isEmpty() || !inputAutista.isEmpty() && Integer.parseInt(inputAutista) != 0 || !inputSoccorritore.isEmpty() && Integer.parseInt(inputSoccorritore) != 0) {
                    String updateQuery = "UPDATE Servizi SET Data = ?, Orario = ?, Paziente = ?, Sigla_Mezzo = ?, Autista = ?, Soccorritore = ? WHERE Data = ? AND Orario = ? AND Paziente = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

                    updateStatement.setString(1, nuovaData.isEmpty() ? dataServizio : nuovaData);
                    updateStatement.setTime(2, nuovoOrarioString.isEmpty() ? orarioTime : nuovoOrarioTime);
                    updateStatement.setString(3, nuovoPaziente.isEmpty() ? paziente : nuovoPaziente);
                    updateStatement.setString(4, nuovoMezzo.isEmpty() ? siglaMezzo : nuovoMezzo);
                    updateStatement.setInt(5, nuovoAutista);
                    updateStatement.setInt(6, nuovoSoccorritore);
                    updateStatement.setString(7, dataServizio);
                    updateStatement.setTime(8, orarioTime);
                    updateStatement.setString(9, paziente);
                    // Esegui l'aggiornamento
                    updateStatement.executeUpdate();
                    System.out.println("Servizio modificato con successo!");
                    updateStatement.close();
                } else {
                    System.out.println("Nessuna modifica effettuata.");
                }

            } else {
                System.out.println("Nessun servizio trovato con i dati forniti.");
                modificaServizio(scanner);
            }

            verificaStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void eliminaServizio(Scanner scanner) {
        System.out.println("Inserisci la data del servizio da eliminare (dd-mm-yyyy):");
        String dataServizio = scanner.nextLine();

        System.out.println("Inserisci l'orario del servizio da eliminare (HH:MM):");
        String orarioString = scanner.nextLine();
        LocalTime localTime = LocalTime.parse(orarioString);
        Time orarioTime = Time.valueOf(localTime);

        System.out.print("Inserisci il nome del paziente del servizio da eliminare: ");
        String paziente = scanner.nextLine();

        try {
            String verificaQuery = "SELECT * FROM Servizi WHERE Data = ? AND Orario = ? AND Paziente = ?";
            PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
            verificaStatement.setString(1, dataServizio);
            verificaStatement.setTime(2, orarioTime);
            verificaStatement.setString(3, paziente);
            ResultSet resultSet = verificaStatement.executeQuery();

            if (resultSet.next()) {
                // Il servizio esiste, chiedi conferma di eliminazione
                System.out.print("Il seguente servizio è stato trovato:");
                System.out.print("  Data: " + dataServizio);
                System.out.print("  Orario: " + orarioString);
                System.out.println("  Paziente: " + paziente);
                System.out.println(" ");

                System.out.print("Vuoi davvero eliminare questo servizio? (sì/no): ");
                String conferma = scanner.nextLine();

                if (conferma.equalsIgnoreCase("si") || conferma.equalsIgnoreCase("sì")) {
                    String deleteQuery = "DELETE FROM Servizi WHERE Data = ? AND Orario = ? AND Paziente = ?";
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                    deleteStatement.setString(1, dataServizio);
                    deleteStatement.setTime(2, orarioTime);
                    deleteStatement.setString(3, paziente);
                    deleteStatement.executeUpdate();
                    System.out.println("Servizio eliminato con successo!");
                    deleteStatement.close();
                } else {
                    System.out.println("Eliminazione annullata.");
                }
            } else {
                System.out.println("Nessun servizio trovato con i dati forniti.");
                eliminaServizio(scanner);
            }

            verificaStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private static boolean verificaEsistenzaVolontario(int idSoccorritore) {
        try {
            String verificaQuery = "SELECT id FROM Volontari WHERE id = ?";
            PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
            verificaStatement.setInt(1, idSoccorritore);
            ResultSet resultSet = verificaStatement.executeQuery();
            return resultSet.next(); // Restituisce true se l'ID esiste, altrimenti false
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // In caso di errore, restituisci false
        }
    }
}


