import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.sql.ResultSet;


public class serviziManager {

    private static Connection connection;
    public serviziManager(Connection connection) {
        this.connection = connection;
    }



    //GESTIONE SERVIZI
    public static void aggiungiServizio(Scanner scanner) {
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
                aggiungiServizio(scanner);
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
            if (Autista > 0) {
                notificheManager.inviaNotificaVolontario(Autista, dataServizio);
            }
            if (Soccorritore > 0) {
                notificheManager.inviaNotificaVolontario(Soccorritore, dataServizio);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Formato data non valido. Utilizza il formato dd-mm-yyyy.");
            aggiungiServizio(scanner);
        }
        menuManager.mostraMenuServizi(scanner);
    }
    public static void modificaServizio(Scanner scanner) {
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
                String pazienteServizio = serviziResultSet.getString("Paziente");
                String siglaMezzo = serviziResultSet.getString("sigla_mezzo");
                int Autista = serviziResultSet.getInt("Autista");
                int Soccorritore = serviziResultSet.getInt("Soccorritore");

                System.out.println("ID: " + idServizio + " | Data: " + dataServizio + " | Orario: " + orarioServizioString + " | Paziente: " + pazienteServizio);
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
                    menuManager.mostraMenuServizi(scanner);
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
            String pazienteServizio = "";
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
                orarioServizioString = orarioServizio.toLocalTime().toString();
                pazienteServizio = servizioResultSet.getString("Paziente");
                siglaMezzo = servizioResultSet.getString("sigla_mezzo");
                Autista = servizioResultSet.getInt("Autista");
                Soccorritore = servizioResultSet.getInt("Soccorritore");
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
                    break; // Esci dal ciclo se l'ora è vuota
                }
            }

            System.out.print("Inserisci il nuovo nome del paziente (lascia vuoto per non modificare): ");
            String nuovoPaziente = scanner.nextLine();
            if (nuovoPaziente.isEmpty()) {
                nuovoPaziente = pazienteServizio;
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
            updateServizioStatement.setString(3, nuovoPaziente);
            updateServizioStatement.setString(4, nuovoMezzo);
            updateServizioStatement.setInt(5, nuovoAutista);
            updateServizioStatement.setInt(6, nuovoSoccorritore);
            updateServizioStatement.setInt(7, idServizioDaModificare);

            int righeModificate = updateServizioStatement.executeUpdate();
            if (righeModificate > 0) {
                System.out.println("Servizio modificato con successo!");
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
        menuManager.mostraMenuServizi(scanner);
    }

    public static void eliminaServizio(Scanner scanner) {
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

                System.out.println("ID: " + idServizio + " | Data: " + dataServizio + " | Paziente: " + pazienteServizio + " | Mezzo: " + mezzoServizio);
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
                    menuManager.mostraMenuServizi(scanner);
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
                                    System.out.println("Servizio con ID " + idServizioDaEliminare + " eliminato con successo!");
                                    System.out.println(" ");
                                    System.out.println(" ");
                                    menuManager.mostraMenuServizi(scanner);
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

        if (!operazioneAnnullata) {
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
    public static void visualizzaRichiesteRimozione(Scanner scanner) {
        try {
            String query = "SELECT * FROM Disponibilita WHERE Richiesta_Rimozione = true";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("RICHIESTE URGENTI DI RIMOZIONE:");
            while (resultSet.next()) {
                int idDisponibilita = resultSet.getInt("ID_disponibilita");
                int matricolaVolontario = resultSet.getInt("Matricola_volontario");
                String dataDisponibilita = resultSet.getString("Data_disponibilita");
                String tipologia = resultSet.getString("Tipologia");
                String motivoRimozione = resultSet.getString("Motivo_Rimozione");

                System.out.println("ID: " + idDisponibilita + " - Matricola Volontario: " + matricolaVolontario + " - Data Disponibilità: " + dataDisponibilita + " - Tipologia: " + tipologia + " - Motivo Rimozione: " + motivoRimozione);
            }

            // Aggiungi il messaggio per accettare o tornare indietro
            System.out.print("Inserisci l'ID della richiesta di rimozione da accettare oppure lascia vuoto per tornare indietro: ");

            //Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine().trim(); // Leggi l'input e rimuovi spazi iniziali e finali

            // Loop finché l'input non è vuoto o un ID valido
            while (!input.isEmpty() && !input.matches("\\d+")) {
                System.out.println("Input non valido. Inserisci l'ID della richiesta o lascia vuoto per tornare indietro.");
                System.out.print("Inserisci l'ID della richiesta di rimozione da accettare oppure lascia vuoto per tornare indietro: ");
                input = scanner.nextLine().trim();
            }

            if (!input.isEmpty()) {
                int idRichiestaDaAccettare = Integer.parseInt(input);
                accettaRichiestaRimozione(idRichiestaDaAccettare);
            } else {
                // L'utente ha lasciato vuoto, torna indietro
                menuManager.mostraMenuAdmin(scanner);
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void accettaRichiestaRimozione(int idRichiesta) {
        try {
            String query = "SELECT * FROM Disponibilita WHERE ID_disponibilita = ? AND Richiesta_Rimozione = true";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idRichiesta);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String dataDisponibilita = resultSet.getString("Data_disponibilita");
                int matricolaVolontario = resultSet.getInt("Matricola_volontario");

                // Rimuovi l'assegnazione del volontario al servizio
                rimuoviAssegnazioneServizio(dataDisponibilita, matricolaVolontario);

                // Cancella la riga della richiesta dalla tabella Disponibilita
                cancellaRichiestaRimozione(idRichiesta);

                System.out.println("Richiesta di rimozione accettata. L'assegnazione del volontario al servizio è stata rimossa e la richiesta è stata cancellata.");
            } else {
                System.out.println("Richiesta di rimozione non valida.");
                //TODO rimanda la lista delle richieste di rimozione - visualizzaRichiesteRimozione();
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void cancellaRichiestaRimozione(int idRichiesta) {
        try {
            String deleteQuery = "DELETE FROM Disponibilita WHERE ID_disponibilita = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setInt(1, idRichiesta);
            deleteStatement.executeUpdate();
            deleteStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void rimuoviAssegnazioneServizio(String dataDisponibilita, int matricolaVolontario) {
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
    }
    public static boolean ciSonoRichiesteRimozione() {
        try {
            String query = "SELECT COUNT(*) FROM Disponibilita WHERE Richiesta_Rimozione = true";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }









}


