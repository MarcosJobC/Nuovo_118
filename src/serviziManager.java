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

        System.out.print("Inserisci la data del servizio (dd-mm-yyyy): ");
        String dataServizio = scanner.nextLine();

        try {
            // Converti la data del servizio in un oggetto LocalDate
            LocalDate dataInserita = LocalDate.parse(dataServizio, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            // Verifica se la data inserita è antecedente a oggi
            if (dataInserita.isBefore(dataOggi)) {
                System.out.println("La data del servizio non può essere antecedente a oggi.");
                aggiungiServizio(scanner);
            }

            // Se la data è oggi, verifica anche l'orario
            if (dataInserita.isEqual(dataOggi)) {
                boolean orarioValido = false;

                do {
                    System.out.println("Inserisci l'orario del servizio (HH:MM):");
                    String orarioString = scanner.nextLine();

                    LocalTime localTime = LocalTime.parse(orarioString, DateTimeFormatter.ofPattern("HH:mm"));

                    if (localTime.isBefore(orarioOggi)) {
                        System.out.println("L'orario del servizio non può essere antecedente all'orario attuale.");
                    } else {
                        orarioServizio = localTime;
                        orarioValido = true;
                    }
                } while (!orarioValido);
            }

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
        LocalTime localTime = LocalTime.now();

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

                System.out.println("ID: " + idServizio + " | Data: " + dataServizio + " | Orario: " + orarioServizio + " | Paziente: " + pazienteServizio);
            }
            System.out.println(" ");

            // Chiedi all'utente di inserire l'ID del servizio da modificare
            System.out.print("Inserisci l'ID del servizio da modificare: ");
            int idServizioDaModificare = scanner.nextInt();
            scanner.nextLine(); // Consuma la newline rimanente

            String verificaQuery = "SELECT * FROM Servizi WHERE ID = ?";
            PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
            verificaStatement.setInt(1, idServizioDaModificare);
            ResultSet resultSet = verificaStatement.executeQuery();


            if (resultSet.next()) {
                // Il servizio esiste, procedi con la modifica

                String dataServizio = resultSet.getString("Data");
                String orarioServizio = resultSet.getString("Orario"); // Utilizza la colonna "Orario" dal result set
                String pazienteServizio = resultSet.getString("Paziente");
                String siglaMezzo = resultSet.getString("Sigla_Mezzo");
                int Autista = resultSet.getInt("Autista");
                int Soccorritore = resultSet.getInt("Soccorritore");

                System.out.println("Il seguente servizio è stato trovato:");
                System.out.println("  Data: " + dataServizio + " | Orario: " + orarioServizio + " | Paziente: " + pazienteServizio + " | Sigla Mezzo: " + siglaMezzo + " | Autista: " + Autista + " | Soccorritore: " + Soccorritore);
                System.out.println(" ");

                // Richiedi i nuovi valori o lascia vuoto per non modificare
                System.out.print("Inserisci la nuova data (oppure lascia vuoto): ");
                String nuovaData = scanner.nextLine();

                if (nuovaData.isEmpty()) {
                    nuovaData = dataServizio;
                }

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
                        //Invio notifica
                        notificheManager.inviaNotificaVolontario(nuovoAutista, nuovaData);
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
                        //Invio notifica
                        notificheManager.inviaNotificaVolontario(nuovoSoccorritore, nuovaData);
                    } else {
                        System.out.println("L'ID del soccorritore inserito non esiste.");
                        modificaServizio(scanner);
                        return; // Interrompi il metodo senza eseguire l'aggiornamento
                    }
                }


                if (!nuovaData.isEmpty() || !nuovoOrarioString.isEmpty() || !nuovoPaziente.isEmpty() ||
                        !nuovoMezzo.isEmpty() || !inputAutista.isEmpty() && Integer.parseInt(inputAutista) != 0 || !inputSoccorritore.isEmpty() && Integer.parseInt(inputSoccorritore) != 0) {
                    String updateQuery = "UPDATE Servizi SET Data = ?, Orario = ?, Paziente = ?, Sigla_Mezzo = ?, Autista = ?, Soccorritore = ? WHERE ID = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

                    updateStatement.setString(1, nuovaData.isEmpty() ? dataServizio : nuovaData);
                    updateStatement.setTime(2, nuovoOrarioString.isEmpty() ? Time.valueOf(orarioServizio) : nuovoOrarioTime);
                    updateStatement.setString(3, nuovoPaziente.isEmpty() ? pazienteServizio : nuovoPaziente);
                    updateStatement.setString(4, nuovoMezzo.isEmpty() ? siglaMezzo : nuovoMezzo);
                    updateStatement.setInt(5, nuovoAutista);
                    updateStatement.setInt(6, nuovoSoccorritore);
                    updateStatement.setInt(7, idServizioDaModificare); // Imposta l'ID del servizio da modificare

                    // Esegui l'aggiornamento
                    updateStatement.executeUpdate();
                    System.out.println("Servizio modificato con successo!");
                    updateStatement.close();
                } else {
                    System.out.println("Nessuna modifica effettuata.");
                }

            } else {
                System.out.println("Nessun servizio trovato con l'ID fornito.");
            }
            resultSet.close();
            verificaStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            System.out.println(" ");
            System.out.println(" ");
            menuManager.mostraMenuServizi(scanner);
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


