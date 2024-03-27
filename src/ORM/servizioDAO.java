package ORM;

import BusinessLogic.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.ResultSet;

import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;




public class servizioDAO {
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


    public static void aggiungiServizioDAO(Scanner scanner) {
        openConnection();
        boolean newfromservizio = false;
        scanner.nextLine();

        LocalDate dataOggi = LocalDate.now();
        LocalTime orarioOggi = LocalTime.now();
        LocalTime orarioServizio = null;

        System.out.print("Inserisci la data del servizio (dd-MM-yyyy): ");
        String dataServizio = scanner.nextLine();

        try {
            // Converti la data del servizio in un oggetto LocalDate
            LocalDate dataInserita = LocalDate.parse(dataServizio, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            // Verifica se la data inserita è antecedente a oggi
            if (dataInserita.isBefore(dataOggi)) {
                System.out.println("La data del servizio non può essere antecedente a oggi.");
                aggiungiServizioDAO(scanner);
                return; // Termina il metodo in modo da evitare richieste aggiuntive
            }

            boolean orarioValido = false;

            do {
                System.out.print("Inserisci l'orario del servizio (HH:mm): ");
                String orarioString = scanner.nextLine();

                LocalTime localTime = LocalTime.parse(orarioString, DateTimeFormatter.ofPattern("HH:mm"));

                if (dataInserita.isEqual(dataOggi) && localTime.isBefore(orarioOggi)) {
                    System.out.println("L'orario del servizio non può essere antecedente all'orario attuale.");
                } else {
                    orarioServizio = localTime;
                    orarioValido = true;
                }
            } while (!orarioValido);

            String nomePaziente = "";
            String cognomePaziente = "";
            boolean pazienteValido = false;
            int pazienteId = 0; // Inizializza l'ID del paziente a 0
            while (!pazienteValido) {

                System.out.print("Inserisci il nome del paziente: ");
                nomePaziente = scanner.nextLine();

                System.out.print("Inserisci il cognome del paziente: ");
                cognomePaziente = scanner.nextLine();

                try {
                    // Verifica se esistono pazienti con nomi e cognomi simili (case insensitive)
                    String verificaQuery = "SELECT * FROM Pazienti WHERE LOWER(Nome) LIKE LOWER(?) AND LOWER(Cognome) LIKE LOWER(?)";
                    PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
                    verificaStatement.setString(1, "%" + nomePaziente + "%");
                    verificaStatement.setString(2, "%" + cognomePaziente + "%");
                    ResultSet resultSet = verificaStatement.executeQuery();

                    if (!resultSet.isBeforeFirst()) {
                        System.out.println("Nessun paziente trovato con nomi e cognomi simili.");
                        System.out.print("Vuoi aggiungere un nuovo paziente? (s/n): ");
                        String sceltaAggiunta = scanner.nextLine();
                        if (sceltaAggiunta.equalsIgnoreCase("s")) {
                            // Inserimento di un nuovo paziente
                            newfromservizio = true;
                            AmministratoreController.aggiungiPazientedaServizio(scanner,newfromservizio, dataServizio, orarioServizio);
                            return; // Termina il metodo dopo l'inserimento del nuovo paziente
                        } else {
                            // L'utente ha scelto di non aggiungere un nuovo paziente, ripeti la richiesta
                            continue;
                        }
                    } else {
                        System.out.println("Pazienti trovati:");
                        while (resultSet.next()) {
                            int id = resultSet.getInt("ID");
                            String nome = resultSet.getString("Nome");
                            String cognome = resultSet.getString("Cognome");
                            Date datanascita = resultSet.getDate("datanascita");
                            System.out.println("ID: " + id + " | Nome: " + nome + " | Cognome: " + cognome + " | Data di nascita: " + datanascita);
                        }

                        // Chiedi all'utente di selezionare l'ID del paziente
                        boolean idValido = false;
                        while (!idValido) {
                            System.out.print("Inserisci l'ID del paziente o 'q' per inserire un nuovo paziente: ");
                            String scelta = scanner.nextLine();

                            if (scelta.equals("q")) {
                                // Inserimento di un nuovo paziente
                                AmministratoreController.aggiungiPazientedaServizio(scanner,newfromservizio, dataServizio, orarioServizio);
                                return; // Termina il metodo dopo l'inserimento del nuovo paziente
                            } else {
                                try {
                                    pazienteId = Integer.parseInt(scelta);

                                    if (pazienteId > 0) {
                                        idValido = true;
                                    } else {
                                        System.out.println("ID non valido.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("ID non valido.");
                                }
                            }
                        }

                        pazienteValido = true;
                    }

                    verificaStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
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


            System.out.print("Inserisci la matricola dell'autista (oppure lascia vuoto per non inserire): ");
            String inputAutista = scanner.nextLine();
            int Autista = 0; // Inizializza l'ID dell'autista a 0
            if (!inputAutista.isEmpty()) {
                Autista = Integer.parseInt(inputAutista);
            }

            System.out.print("Inserisci la matricola del soccorritore (oppure lascia vuoto per non inserire): ");
            String inputSoccorritore = scanner.nextLine();
            int Soccorritore = 0; // Inizializza l'ID del soccorritore a 0
            if (!inputSoccorritore.isEmpty()) {
                Soccorritore = Integer.parseInt(inputSoccorritore);
            }

            try {
                String query = "INSERT INTO Servizi (Data, Orario, Paziente, Sigla_Mezzo, Autista, Soccorritore) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, dataServizio);
                preparedStatement.setTime(2, java.sql.Time.valueOf(orarioServizio));
                preparedStatement.setInt(3, pazienteId);
                preparedStatement.setString(4, siglaMezzo);
                preparedStatement.setInt(5, Autista);
                preparedStatement.setInt(6, Soccorritore);

                preparedStatement.executeUpdate();

                System.out.println("BusinessLogic.Servizio aggiunto con successo!");

                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (Autista > 0) {
                notificaDAO.inviaNotificaVolontarioDAO(Autista, dataServizio);
            }
            if (Soccorritore > 0) {
                notificaDAO.inviaNotificaVolontarioDAO(Soccorritore, dataServizio);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Formato data non valido. Utilizza il formato dd-mm-yyyy.");
            aggiungiServizioDAO(scanner);
        }
        System.out.println(" ");
        System.out.println(" ");
        menuController.mostraMenuServizi(scanner);
        closeConnection();
    }
    public static void aggiungiServizioInternoDAO(Scanner scanner, boolean newfromservizio,String dataServizio,LocalTime orarioServizio){
        openConnection();
        try {

            String nomePaziente = "";
            String cognomePaziente = "";
            boolean pazienteValido = false;
            int pazienteId = 0; // Inizializza l'ID del paziente a 0
            while (!pazienteValido) {

                System.out.print("Inserisci il nome del paziente: ");
                nomePaziente = scanner.nextLine();

                System.out.print("Inserisci il cognome del paziente: ");
                cognomePaziente = scanner.nextLine();

                try {
                    // Verifica se esistono pazienti con nomi e cognomi simili (case insensitive)
                    String verificaQuery = "SELECT * FROM Pazienti WHERE LOWER(Nome) LIKE LOWER(?) AND LOWER(Cognome) LIKE LOWER(?)";
                    PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
                    verificaStatement.setString(1, "%" + nomePaziente + "%");
                    verificaStatement.setString(2, "%" + cognomePaziente + "%");
                    ResultSet resultSet = verificaStatement.executeQuery();

                    if (!resultSet.isBeforeFirst()) {
                        System.out.println("Nessun paziente trovato con nomi e cognomi simili.");
                        System.out.print("Vuoi aggiungere un nuovo paziente? (s/n): ");
                        String sceltaAggiunta = scanner.nextLine();
                        if (sceltaAggiunta.equalsIgnoreCase("s")) {
                            // Inserimento di un nuovo paziente
                            newfromservizio = true;
                            AmministratoreController.aggiungiPazientedaServizio(scanner,newfromservizio, dataServizio, orarioServizio);
                            return; // Termina il metodo dopo l'inserimento del nuovo paziente
                        } else {
                            // L'utente ha scelto di non aggiungere un nuovo paziente, ripeti la richiesta
                            continue;
                        }
                    } else {
                        System.out.println("Pazienti trovati:");
                        while (resultSet.next()) {
                            int id = resultSet.getInt("ID");
                            String nome = resultSet.getString("Nome");
                            String cognome = resultSet.getString("Cognome");
                            Date datanascita = resultSet.getDate("datanascita");
                            System.out.println("ID: " + id + " | Nome: " + nome + " | Cognome: " + cognome + " | Data di nascita: " + datanascita);
                        }

                        // Chiedi all'utente di selezionare l'ID del paziente
                        boolean idValido = false;
                        while (!idValido) {
                            System.out.print("Inserisci l'ID del paziente o 'q' per inserire un nuovo paziente: ");
                            String scelta = scanner.nextLine();

                            if (scelta.equals("q")) {
                                // Inserimento di un nuovo paziente
                                AmministratoreController.aggiungiPazientedaServizio(scanner,newfromservizio, dataServizio, orarioServizio);
                                return; // Termina il metodo dopo l'inserimento del nuovo paziente
                            } else {
                                try {
                                    pazienteId = Integer.parseInt(scelta);

                                    if (pazienteId > 0) {
                                        idValido = true;
                                    } else {
                                        System.out.println("ID non valido.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("ID non valido.");
                                }
                            }
                        }

                        pazienteValido = true;
                    }

                    verificaStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
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


            System.out.print("Inserisci la matricola dell'autista (oppure lascia vuoto per non inserire): ");
            String inputAutista = scanner.nextLine();
            int Autista = 0; // Inizializza l'ID dell'autista a 0
            if (!inputAutista.isEmpty()) {
                Autista = Integer.parseInt(inputAutista);
            }

            System.out.print("Inserisci la matricola del soccorritore (oppure lascia vuoto per non inserire): ");
            String inputSoccorritore = scanner.nextLine();
            int Soccorritore = 0; // Inizializza l'ID del soccorritore a 0
            if (!inputSoccorritore.isEmpty()) {
                Soccorritore = Integer.parseInt(inputSoccorritore);
            }

            try {
                String query = "INSERT INTO Servizi (Data, Orario, Paziente, Sigla_Mezzo, Autista, Soccorritore) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, dataServizio);
                preparedStatement.setTime(2, java.sql.Time.valueOf(orarioServizio));
                preparedStatement.setInt(3, pazienteId);
                preparedStatement.setString(4, siglaMezzo);
                preparedStatement.setInt(5, Autista);
                preparedStatement.setInt(6, Soccorritore);

                preparedStatement.executeUpdate();

                System.out.println("BusinessLogic.Servizio aggiunto con successo!");

                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (Autista > 0) {
                notificaDAO.inviaNotificaVolontarioDAO(Autista, dataServizio);
            }
            if (Soccorritore > 0) {
                notificaDAO.inviaNotificaVolontarioDAO(Soccorritore, dataServizio);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Formato data non valido. Utilizza il formato dd-mm-yyyy.");
            aggiungiServizioDAO(scanner);
        }
        System.out.println(" ");
        System.out.println(" ");
        menuController.mostraMenuServizi(scanner);
        closeConnection();
    }

    public static void modificaServizioDAO(Scanner scanner) {
        openConnection();
        scanner.nextLine();
        Time nuovoOrarioTime = null;
        try {
            // Ottieni una lista di tutti i servizi
            String listaServiziQuery = "SELECT * FROM Servizi";
            PreparedStatement listaServiziStatement = connection.prepareStatement(listaServiziQuery);
            ResultSet serviziResultSet = listaServiziStatement.executeQuery();

            // Stampa la lista dei servizi con i dettagli
            System.out.println("Lista dei servizi:");
            while (serviziResultSet.next()) {
                int idServizio = serviziResultSet.getInt("ID");
                String dataServizio = serviziResultSet.getString("Data");
                Time orarioServizio = serviziResultSet.getTime("Orario");
                String orarioServizioString = orarioServizio.toLocalTime().toString();
                String siglaMezzo = serviziResultSet.getString("sigla_mezzo");
                int Autista = serviziResultSet.getInt("Autista");
                int Soccorritore = serviziResultSet.getInt("Soccorritore");

                System.out.println("ID: " + idServizio + " | Data: " + dataServizio + " | Orario: " + orarioServizioString + " | BusinessLogic.Mezzo: " + siglaMezzo +" | Autista: " + Autista + " | Soccorritore: " + Soccorritore  );
            }
            System.out.println(" ");

            // Chiedi all'utente di inserire l'ID del servizio da modificare
            int idServizioDaModificare = 0;
            boolean idValido = false;

            while (!idValido) {
                System.out.print("Inserisci l'ID del servizio da modificare o premi 'q' per tornare indietro: ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("q")) {
                    System.out.println("Annullamento dell'operazione.");
                    System.out.println(" ");
                    System.out.println(" ");
                    menuController.mostraMenuServizi(scanner);
                } else {
                    try {
                        idServizioDaModificare = Integer.parseInt(input);

                        String verificaQuery = "SELECT * FROM Servizi WHERE ID = ?";
                        PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
                        verificaStatement.setInt(1, idServizioDaModificare);
                        ResultSet resultSet = verificaStatement.executeQuery();

                        if (resultSet.next()) {
                            idValido = true; // L'ID è valido, esci dal ciclo
                        } else {
                            System.out.println("Nessun servizio trovato con l'ID specificato. Riprova.");
                        }
                        resultSet.close();
                        verificaStatement.close();
                    } catch (NumberFormatException e) {
                        System.out.println("Input non valido. Inserisci un numero di ID valido o 'q' per annullare.");
                    }
                }
            }


            // Ora che abbiamo l'ID valido, otteniamo i dati del servizio da modificare
            String dataServizio = "";
            Time orarioServizio = null;
            String orarioServizioString = "";
            int pazienteServizio = 0;
            String siglaMezzo = "";
            int Autista = 0;
            int Soccorritore = 0;

            String servizioQuery = "SELECT * FROM Servizi WHERE ID = ?";
            PreparedStatement servizioStatement = connection.prepareStatement(servizioQuery);
            servizioStatement.setInt(1, idServizioDaModificare);
            ResultSet servizioResultSet = servizioStatement.executeQuery();

            if (servizioResultSet.next()) {
                dataServizio = servizioResultSet.getString("Data");
                orarioServizio = servizioResultSet.getTime("Orario");
                pazienteServizio = servizioResultSet.getInt("Paziente");
                orarioServizioString = orarioServizio.toLocalTime().toString();
                siglaMezzo = servizioResultSet.getString("sigla_mezzo");
                Autista = servizioResultSet.getInt("Autista");
                Soccorritore = servizioResultSet.getInt("Soccorritore");
            } else {
                System.out.println("Nessun servizio trovato con l'ID specificato.");
                System.out.println(" ");
                System.out.println(" ");
                menuController.mostraMenuServizi(scanner);
                return; // Esci dal metodo in caso di ID non valido
            }
            String nuovaData = dataServizio; // Inizializza con la data attuale
            String nuovaOraString = orarioServizioString; // Inizializza con l'orario attuale

            while (true) {
                System.out.print("Inserisci la nuova data nel formato 'dd-MM-yyyy' (lascia vuoto per non modificare): ");
                String inputNuovaData = scanner.nextLine();

                if (!inputNuovaData.isEmpty()) {
                    try {
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        LocalDate oggi = LocalDate.now();
                        LocalDate dataInserita = LocalDate.parse(inputNuovaData, dateFormatter);

                        if (dataInserita.isBefore(oggi)) {
                            System.out.println("La data inserita è antecedente ad oggi. Inserisci una data valida.");
                        } else {
                            nuovaData = inputNuovaData;
                            break; // Esci dal ciclo se la data è valida
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("Formato data non valido. Utilizza il formato 'dd-MM-yyyy'.");
                    }
                } else {
                    break; // Esci dal ciclo se la data è vuota
                }
            }

            while (true) {
                System.out.print("Inserisci l'ora nel formato 'HH:mm' (lascia vuoto per non modificare): ");
                String inputNuovaOra = scanner.nextLine();

                if (!inputNuovaOra.isEmpty()) {
                    try {
                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                        LocalTime oraAttuale = LocalTime.now();
                        LocalTime nuovaOra = LocalTime.parse(inputNuovaOra, timeFormatter);

                        if (nuovaOra.isBefore(oraAttuale)) {
                            System.out.println("L'ora inserita è antecedente all'ora attuale. Inserisci un'ora valida.");
                        } else {
                            nuovaOraString = inputNuovaOra;
                            // Effettua la conversione esplicita da LocalTime a java.sql.Time
                            java.sql.Time nuovaOraSql = java.sql.Time.valueOf(nuovaOra);
                            nuovoOrarioTime = nuovaOraSql;
                            break; // Esci dal ciclo se l'ora è valida
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("Formato ora non valido. Utilizza il formato 'HH:mm'.");
                    }
                } else {
                    // L'utente ha lasciato vuoto, quindi mantieni l'orario precedente
                    nuovoOrarioTime = orarioServizio;
                    break; // Esci dal ciclo se l'ora è vuota
                }
            }

            System.out.print("Inserisci il nuovo mezzo (lascia vuoto per non modificare): ");
            String nuovoMezzo = scanner.nextLine();
            if (nuovoMezzo.isEmpty()) {
                nuovoMezzo = siglaMezzo;
            }

            System.out.print("Inserisci il nuovo ID dell'autista (lascia vuoto per non modificare): ");
            String inputNuovoAutista = scanner.nextLine();
            int nuovoAutista = Autista; // Inizializza con l'ID attuale
            if (!inputNuovoAutista.isEmpty()) {
                try {
                    nuovoAutista = Integer.parseInt(inputNuovoAutista);
                } catch (NumberFormatException e) {
                    System.out.println("Input non valido per l'ID dell'autista. Il valore attuale sarà mantenuto.");
                }
            }

            System.out.print("Inserisci il nuovo ID del soccorritore (lascia vuoto per non modificare): ");
            String inputNuovoSoccorritore = scanner.nextLine();
            int nuovoSoccorritore = Soccorritore; // Inizializza con l'ID attuale
            if (!inputNuovoSoccorritore.isEmpty()) {
                try {
                    nuovoSoccorritore = Integer.parseInt(inputNuovoSoccorritore);
                } catch (NumberFormatException e) {
                    System.out.println("Input non valido per l'ID del soccorritore. Il valore attuale sarà mantenuto.");
                }
            }

            // Aggiorna i dati del servizio nel database
            String updateServizioQuery = "UPDATE Servizi SET Data = ?, Orario = ?, Paziente = ?, sigla_mezzo = ?, Autista = ?, Soccorritore = ? WHERE ID = ?";
            PreparedStatement updateServizioStatement = connection.prepareStatement(updateServizioQuery);
            updateServizioStatement.setString(1, nuovaData);
            updateServizioStatement.setTime(2, nuovoOrarioTime);
            updateServizioStatement.setInt(3,pazienteServizio);
            updateServizioStatement.setString(4, nuovoMezzo);
            updateServizioStatement.setInt(5, nuovoAutista);
            updateServizioStatement.setInt(6, nuovoSoccorritore);
            updateServizioStatement.setInt(7, idServizioDaModificare);

            int righeModificate = updateServizioStatement.executeUpdate();
            if (righeModificate > 0) {
                System.out.println("BusinessLogic.Servizio modificato con successo!");
            } else {
                System.out.println("Si è verificato un errore durante la modifica del servizio.");
            }

            updateServizioStatement.close();
            servizioResultSet.close();
            servizioStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(" ");
        System.out.println(" ");
        menuController.mostraMenuServizi(scanner);
        closeConnection();
    }
    public static void eliminaServizioDAO(Scanner scanner) {
        openConnection();
        scanner.nextLine();
        boolean operazioneAnnullata = false;

        try {
            String query = "SELECT * FROM Servizi";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Lista dei servizi:");
            while (resultSet.next()) {
                int idServizio = resultSet.getInt("Id");
                String dataServizio = resultSet.getString("Data");
                String pazienteServizio = resultSet.getString("Paziente");
                String mezzoServizio = resultSet.getString("Sigla_mezzo");

                System.out.println("ID: " + idServizio + " | Data: " + dataServizio + " | Paziente: " + pazienteServizio + " | BusinessLogic.Mezzo: " + mezzoServizio);
            }

            System.out.println(" ");
            System.out.print("Inserisci l'ID del servizio da eliminare o premi 'q' per tornare indietro: ");
            while (true) {
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("q")) {
                    System.out.println("Annullamento dell'operazione.");
                    operazioneAnnullata = true;

                    System.out.println(" ");
                    System.out.println(" ");
                    menuController.mostraMenuServizi(scanner);
                    break;
                } else {
                    try {
                        int idServizioDaEliminare = Integer.parseInt(input);

                        String verificaQuery = "SELECT Id FROM Servizi WHERE Id = ?";
                        PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
                        verificaStatement.setInt(1, idServizioDaEliminare);
                        ResultSet verificaResultSet = verificaStatement.executeQuery();

                        if (verificaResultSet.next()) {
                            System.out.print("Sei sicuro di voler eliminare il servizio? (s/n): ");
                            String conferma = scanner.nextLine().trim();

                            if (conferma.equalsIgnoreCase("s")) {
                                String deleteQuery = "DELETE FROM Servizi WHERE Id = ?";
                                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                                deleteStatement.setInt(1, idServizioDaEliminare);
                                int rowsAffected = deleteStatement.executeUpdate();

                                if (rowsAffected > 0) {
                                    System.out.println("BusinessLogic.Servizio con ID " + idServizioDaEliminare + " eliminato con successo!");
                                    System.out.println(" ");
                                    System.out.println(" ");
                                    menuController.mostraMenuServizi(scanner);
                                } else {
                                    System.out.println("Input non valido.");
                                    System.out.print("Inserisci l'ID del servizio da eliminare o premi 'q' per tornare indietro: ");
                                }
                            } else {
                                System.out.println("Eliminazione annullata.");
                                System.out.print("Inserisci l'ID del servizio da eliminare o premi 'q' per tornare indietro: ");
                            }
                        } else {
                            System.out.println("Nessun servizio trovato con l'ID specificato.");
                            System.out.print("Inserisci l'ID del servizio da eliminare o premi 'q' per tornare indietro: ");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Input non valido.");
                        System.out.print("Inserisci l'ID del servizio da eliminare o premi 'q' per tornare indietro: ");
                    }
                }
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();

    }

    public static void rimuoviAssegnazioneServizioDAO(String dataDisponibilita, int matricolaVolontario) {
        openConnection();
        try {
            String updateQuery = "UPDATE Servizi SET Autista = CASE WHEN Autista = ? THEN 0 ELSE Autista END, Soccorritore = CASE WHEN Soccorritore = ? THEN 0 ELSE Soccorritore END WHERE Data = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setInt(1, matricolaVolontario);
            updateStatement.setInt(2, matricolaVolontario);
            updateStatement.setString(3, dataDisponibilita);
            updateStatement.executeUpdate();
            updateStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }
    public static boolean ciSonoRichiesteRimozioneDAO() {
        openConnection();
        try {
            String query = "SELECT COUNT(*) FROM Disponibilita WHERE Richiesta_Rimozione = true";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            int count = resultSet.getInt(1);
            closeConnection();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
    }

    public static void rimuoviServiziScadutiDAO() {
        openConnection();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String oggi = dateFormat.format(cal.getTime()); // Ottieni la data odierna nel formato dd-MM-yyyy

        String deleteServiziQuery = "DELETE FROM Servizi WHERE TO_DATE(Data, 'dd-MM-yyyy') < TO_DATE(?, 'dd-MM-yyyy')";
        try {
            PreparedStatement deleteServiziStatement = connection.prepareStatement(deleteServiziQuery);
            deleteServiziStatement.setString(1, oggi); // Passa la data odierna come stringa nel formato dd-MM-yyyy
            int rowCount = deleteServiziStatement.executeUpdate();
            deleteServiziStatement.close();
            /*System.out.println(rowCount + " servizi scaduti sono stati rimosse.");*/
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }




    public static void assegnaAutistiAutomaticamenteDAO() {
        openConnection();
        try {

            String serviziSenzaAutistaQuery = "SELECT * FROM Servizi WHERE Autista = 0";
            PreparedStatement serviziSenzaAutistaStatement = connection.prepareStatement(serviziSenzaAutistaQuery);
            ResultSet serviziSenzaAutistaResultSet = serviziSenzaAutistaStatement.executeQuery();


            while (serviziSenzaAutistaResultSet.next()) {
                int idServizio = serviziSenzaAutistaResultSet.getInt("Id");
                String dataServizio = serviziSenzaAutistaResultSet.getString("Data");
                LocalTime orarioServizio = serviziSenzaAutistaResultSet.getTime("orario").toLocalTime();

                // Trova un autista volontario disponibile e non confermato per questa data
                int autistaDisponibile = utenteDAO.trovaVolontarioDisponibileNonConfermato(dataServizio, orarioServizio);

                if (autistaDisponibile != 0) {
                    // Cambia lo stato del volontario da "Non confermata" a "Reclutato"
                    utenteDAO.cambiaStatoVolontario(autistaDisponibile, dataServizio);

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
        closeConnection();
    }
    public static void assegnaSoccorritoriAutomaticamenteDAO() {
        openConnection();
        try {
            String serviziSenzaSoccorritoreQuery = "SELECT * FROM Servizi WHERE Soccorritore = 0";
            PreparedStatement serviziSenzaSoccorritoreStatement = connection.prepareStatement(serviziSenzaSoccorritoreQuery);
            ResultSet serviziSenzaSoccorritoreResultSet = serviziSenzaSoccorritoreStatement.executeQuery();

            while (serviziSenzaSoccorritoreResultSet.next()) {
                int idServizio = serviziSenzaSoccorritoreResultSet.getInt("Id");
                String dataServizio = serviziSenzaSoccorritoreResultSet.getString("Data");
                LocalTime orarioServizio = serviziSenzaSoccorritoreResultSet.getTime("orario").toLocalTime();

                // Trova un soccorritore volontario disponibile e non confermato per questa data
                int soccorritoreDisponibile = utenteDAO.trovaVolontarioDisponibileNonConfermato(dataServizio, orarioServizio);

                if (soccorritoreDisponibile != 0) {
                    // Cambia lo stato del volontario da "Non confermata" a "Reclutato"
                    utenteDAO.cambiaStatoVolontario(soccorritoreDisponibile, dataServizio);

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
        closeConnection();
    }

    public static void visualizzaServiziAssegnatiDAO(Scanner scanner, int matricolaVolontario) {
        openConnection();
        try {
            // Query per i servizi assegnati
            String serviziQuery = "SELECT 'BusinessLogic.Servizio' AS Tipo, Data, Orario, Sigla_Mezzo FROM Servizi WHERE Autista = ? OR Soccorritore = ?";
            PreparedStatement serviziStatement = connection.prepareStatement(serviziQuery);
            serviziStatement.setInt(1, matricolaVolontario);
            serviziStatement.setInt(2, matricolaVolontario);

            // Query per le emergenze assegnate
            String emergenzeQuery = "SELECT 'Emergenza' AS Tipo, Data, Turno FROM Emergenze " +
                    "WHERE ID IN (SELECT EmergenzaID FROM SoccorritoriEmergenza WHERE SoccorritoreID = ?)";
            PreparedStatement emergenzeStatement = connection.prepareStatement(emergenzeQuery);
            emergenzeStatement.setInt(1, matricolaVolontario);

            // Esecuzione delle query e combinazione dei risultati
            ResultSet serviziResultSet = serviziStatement.executeQuery();
            ResultSet emergenzeResultSet = emergenzeStatement.executeQuery();

            List<String> serviziEdEmergenze = new ArrayList<>();

            while (serviziResultSet.next()) {
                String tipo = serviziResultSet.getString("Tipo");
                String data = serviziResultSet.getString("Data");
                Time orario = serviziResultSet.getTime("Orario");
                String siglaMezzo = serviziResultSet.getString("Sigla_Mezzo");

                StringBuilder risultato = new StringBuilder();
                risultato.append("Tipo: ").append(tipo).append(" - Data: ").append(data);

                if (tipo.equals("BusinessLogic.Servizio")) {
                    risultato.append(" - Orario: ").append(orario).append(" - BusinessLogic.Mezzo: ").append(siglaMezzo);
                } else if (tipo.equals("Emergenza")) {
                    String turno = serviziResultSet.getString("Turno");
                    risultato.append(" - Turno: ").append(turno);
                }

                serviziEdEmergenze.add(risultato.toString());
            }

            while (emergenzeResultSet.next()) {
                String tipo = emergenzeResultSet.getString("Tipo");
                String data = emergenzeResultSet.getString("Data");
                String turno = emergenzeResultSet.getString("Turno");

                StringBuilder risultato = new StringBuilder();
                risultato.append("Tipo: ").append(tipo).append(" - Data: ").append(data).append(" - Turno: ").append(turno);

                serviziEdEmergenze.add(risultato.toString());
            }

            serviziStatement.close();
            emergenzeStatement.close();

            System.out.println("Servizi ed Emergenze assegnati:");

            for (String risultato : serviziEdEmergenze) {
                System.out.println(risultato);
            }

            System.out.println(" ");
            System.out.println("Premi un qualsiasi tasto per tornare indietro...");

            // Attendiamo che l'utente prema un tasto
            scanner.nextLine();

            menuController.mostraMenuUtenteNormale(scanner, matricolaVolontario);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

}


