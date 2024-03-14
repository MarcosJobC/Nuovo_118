package ORM;
import BusinessLogic.*;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class utentiManager {

    private static Connection connection;
    public utentiManager(Connection connection) {
        this.connection = connection;
    }


    //GESTIONE VOLONTARI

    //TODO NUOVO METODI
    public static void registrazioneDAO(String nome, String cognome, String dataDiNascita, String qualifica, String codicefiscale, String password) {
        try {
            String query = "INSERT INTO Volontari (Nome, Cognome, data_di_nascita, Qualifica, codice_fiscale, Password, IsAdmin) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, cognome);
            preparedStatement.setString(3, dataDiNascita);
            preparedStatement.setString(4, qualifica);
            preparedStatement.setString(5, codicefiscale);
            preparedStatement.setString(6, password);
            preparedStatement.setString(7, "False");
            preparedStatement.executeUpdate();

            System.out.println("Registrazione completata!");

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void accessoDAO(String codicefiscale,String password,Scanner scanner) {


        try {
            String query = "SELECT * FROM Volontari WHERE Codice_fiscale = ? AND Password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, codicefiscale);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int matricolaVolontario = resultSet.getInt("id"); // Ottenere l'ID dell'utente
                boolean isAdmin = resultSet.getString("isadmin").equalsIgnoreCase("True");

                System.out.println("Accesso consentito!");
                System.out.println(" ");
                System.out.println(" ");

                if (isAdmin) {
                    // Mostra il menu per gli utenti amministratori
                    menuController.mostraMenuAdmin(scanner);
                } else {
                    // Mostra il menu per gli utenti non amministratori
                    menuController.mostraMenuUtenteNormale(scanner, matricolaVolontario);
                }
            } else {
                System.out.println("Accesso negato. Codice fiscale o password errati.");
                System.out.println(" ");
                System.out.println(" ");
                menuController.menuIniziale(scanner);
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void inserisciDisponibilitaDAO(Scanner scanner, int matricolaVolontario, String dataDisponibilita, String tipologia, LocalTime oraInizio, LocalTime oraFine) {

        try {
            // Verifica se esiste già una disponibilità per questa data e tipologia
            String verificaQuery = "SELECT COUNT(*) FROM Disponibilita WHERE Matricola_volontario = ? AND Data_disponibilita = ? AND Tipologia = ?";
            PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
            verificaStatement.setInt(1, matricolaVolontario);
            verificaStatement.setString(2, dataDisponibilita);
            verificaStatement.setString(3, tipologia);
            ResultSet verificaResult = verificaStatement.executeQuery();
            verificaResult.next();
            int count = verificaResult.getInt(1);
            verificaStatement.close();

            if (count > 0) {
                System.out.println("Hai già inserito una disponibilità per questa data e tipologia, se vuoi inserirne una nuova prima elimina la precedente.");
                System.out.println(" ");
                System.out.println(" ");
                menuController.mostraMenuUtenteNormale(scanner, matricolaVolontario);
            }

            String insertQuery = "INSERT INTO Disponibilita (Matricola_volontario, Data_disponibilita, Tipologia, Confermata, Ora_inizio, Ora_fine, Turno_emergenza) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setInt(1, matricolaVolontario);
            insertStatement.setString(2, dataDisponibilita);
            insertStatement.setString(3, tipologia);
            insertStatement.setString(4, "Non confermata");
            insertStatement.setString(7, tipologia);

            if ("Servizi sociali".equals(tipologia)) {
                insertStatement.setTime(5, Time.valueOf(oraInizio));
                insertStatement.setTime(6, Time.valueOf(oraFine));
            } else {
                insertStatement.setNull(5, Types.TIME);
                insertStatement.setNull(6, Types.TIME);
            }


            insertStatement.executeUpdate();

            System.out.println(" ");
            System.out.println("Disponibilità inserita con successo! Grazie mille!");
            insertStatement.close();
            System.out.println(" ");
            System.out.println(" ");
            menuController.mostraMenuUtenteNormale(scanner, matricolaVolontario);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //TODO NON MESSI IN BUSINESS LOGIC
    public static void modificaAnagrafeVolontariDAO(Scanner scanner) {
        scanner.nextLine();
        try {
            String query = "SELECT * FROM Volontari";
            PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Lista dei volontari:");
            while (resultSet.next()) {
                String nome = resultSet.getString("Nome");
                String cognome = resultSet.getString("Cognome");
                String codiceFiscale = resultSet.getString("codice_fiscale");
                System.out.println("Nome: " + nome + " | Cognome: " + cognome + " | CF: (" + codiceFiscale + ")");
            }

            boolean volontarioTrovato = false;

            while (!volontarioTrovato) {
                System.out.print("Inserisci il codice fiscale del volontario da modificare o premi 'q' per tornare al menu precedente: ");
                String codiceFiscaleVolontario = scanner.nextLine();

                if (codiceFiscaleVolontario.equalsIgnoreCase("q")) {
                    System.out.println("Annullamento dell'operazione.");
                    System.out.println("  ");
                    System.out.println("  ");
                    menuController.mostraMenuVolontari(scanner);
                }

                String verificaQuery = "SELECT * FROM Volontari WHERE Codice_fiscale = ?";
                PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                verificaStatement.setString(1, codiceFiscaleVolontario);
                ResultSet volontarioResultSet = verificaStatement.executeQuery();

                if (volontarioResultSet.next()) {
                    System.out.println("Volontario trovato. Inserisci i nuovi dati: ");

                    System.out.print("Nuovo nome (lascia vuoto per mantenere invariato): ");
                    String nuovoNome = scanner.nextLine();

                    System.out.print("Nuovo cognome (lascia vuoto per mantenere invariato): ");
                    String nuovoCognome = scanner.nextLine();

                    System.out.print("Nuova data di nascita (dd-mm-yyyy, lascia vuoto per mantenere invariato): ");
                    String nuovaDataDiNascita = scanner.nextLine();

                    System.out.print("Nuova qualifica (lascia vuoto per mantenere invariato): ");
                    String nuovaQualifica = scanner.nextLine();

                    System.out.print("Nuovo codice fiscale (lascia vuoto per mantenere invariato): ");
                    String nuovocodicefiscale = scanner.nextLine();

                    if (!nuovoNome.isEmpty()) {
                        volontarioResultSet.updateString("Nome", nuovoNome);
                    }
                    if (!nuovoCognome.isEmpty()) {
                        volontarioResultSet.updateString("Cognome", nuovoCognome);
                    }
                    if (!nuovaDataDiNascita.isEmpty()) {
                        volontarioResultSet.updateString("data_di_nascita", nuovaDataDiNascita);
                    }
                    if (!nuovaQualifica.isEmpty()) {
                        volontarioResultSet.updateString("Qualifica", nuovaQualifica);
                    }
                    if (!nuovocodicefiscale.isEmpty()) {
                        volontarioResultSet.updateString("codice_fiscale", nuovocodicefiscale);
                    }

                    volontarioResultSet.updateRow();

                    System.out.println("Anagrafe volontario modificata con successo!");
                    System.out.println("  ");
                    System.out.println("  ");
                    volontarioTrovato = true;
                } else {
                    System.out.println("BusinessLogic.Volontario non trovato.");
                }

                verificaStatement.close();
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        menuController.mostraMenuVolontari(scanner);
    }
    public static void eliminaVolontario(Scanner scanner) {
        scanner.nextLine();
        try {
            String query = "SELECT * FROM Volontari WHERE id <> 0";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Lista dei volontari:");
            while (resultSet.next()) {
                String nome = resultSet.getString("Nome");
                String cognome = resultSet.getString("Cognome");
                String codiceFiscale = resultSet.getString("codice_fiscale");
                System.out.println("Nome: " + nome + " | Cognome: " + cognome + " | CF: (" + codiceFiscale + ")");
            }

            boolean volontarioTrovato = false;

            while (!volontarioTrovato) {
                System.out.print("Inserisci il codice fiscale del volontario da eliminare o premi 'q' per tornare al menu precedente: ");
                String codiceFiscaleVolontario = scanner.nextLine();

                if (codiceFiscaleVolontario.equalsIgnoreCase("q")) {
                    System.out.println("Annullamento dell'operazione.");
                    System.out.println("  ");
                    System.out.println("  ");
                    menuController.mostraMenuVolontari(scanner);
                }

                String verificaQuery = "SELECT * FROM Volontari WHERE codice_fiscale = ?";
                PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
                verificaStatement.setString(1, codiceFiscaleVolontario);
                ResultSet risultatoVerifica = verificaStatement.executeQuery();

                if (risultatoVerifica.next()) {
                    System.out.println("BusinessLogic.Volontario trovato. Sei sicuro di volerlo eliminare? (s/n)");
                    String conferma = scanner.nextLine();

                    if (conferma.equalsIgnoreCase("s")) {
                        // Esegui il passaggio 1: Ottieni le notifiche associate al volontario
                        int idVolontario = risultatoVerifica.getInt("id");

                        String queryNotifiche = "SELECT * FROM Notifiche WHERE Matricola_Volontario = ?";
                        PreparedStatement statementNotifiche = connection.prepareStatement(queryNotifiche);
                        statementNotifiche.setInt(1, idVolontario);
                        ResultSet risultatoNotifiche = statementNotifiche.executeQuery();

                        // Esegui il passaggio 2: Elimina le notifiche associate al volontario
                        while (risultatoNotifiche.next()) {
                            int idNotifica = risultatoNotifiche.getInt("id");
                            String deleteQueryNotifiche = "DELETE FROM Notifiche WHERE id = ?";
                            PreparedStatement deleteStatementNotifiche = connection.prepareStatement(deleteQueryNotifiche);
                            deleteStatementNotifiche.setInt(1, idNotifica);
                            deleteStatementNotifiche.executeUpdate();
                        }

                        // Esegui il passaggio 3: Ottieni le disponibilità associate al volontario
                        String queryDisponibilita = "SELECT * FROM Disponibilita WHERE Matricola_volontario = ?";
                        PreparedStatement statementDisponibilita = connection.prepareStatement(queryDisponibilita);
                        statementDisponibilita.setInt(1, idVolontario);
                        ResultSet risultatoDisponibilita = statementDisponibilita.executeQuery();

                        // Esegui il passaggio 4: Elimina le disponibilità associate al volontario
                        while (risultatoDisponibilita.next()) {
                            int idDisponibilita = risultatoDisponibilita.getInt("id_disponibilita");
                            String deleteQueryDisponibilita = "DELETE FROM Disponibilita WHERE id_disponibilita = ?";
                            PreparedStatement deleteStatementDisponibilita = connection.prepareStatement(deleteQueryDisponibilita);
                            deleteStatementDisponibilita.setInt(1, idDisponibilita);
                            deleteStatementDisponibilita.executeUpdate();
                        }

                        // Esegui il passaggio 5: Elimina il volontario
                        String deleteQueryVolontario = "DELETE FROM Volontari WHERE codice_fiscale = ?";
                        PreparedStatement deleteStatementVolontario = connection.prepareStatement(deleteQueryVolontario);
                        deleteStatementVolontario.setString(1, codiceFiscaleVolontario);
                        deleteStatementVolontario.executeUpdate();

                        // Passaggio aggiuntivo: Imposta a 0 i campi Autista e Soccorritore nella tabella Servizi
                        String updateQueryServizi = "UPDATE Servizi SET Autista = 0, Soccorritore = 0 WHERE Autista = ? OR Soccorritore = ?";
                        PreparedStatement updateStatementServizi = connection.prepareStatement(updateQueryServizi);
                        updateStatementServizi.setInt(1, idVolontario);
                        updateStatementServizi.setInt(2, idVolontario);
                        updateStatementServizi.executeUpdate();

                        System.out.println("BusinessLogic.Volontario eliminato con successo!");
                        System.out.println("  ");
                        System.out.println("  ");
                        volontarioTrovato = true;
                    } else {
                        System.out.println("Operazione di eliminazione annullata.");
                    }
                } else {
                    System.out.println("BusinessLogic.Volontario non trovato.");
                }
                verificaStatement.close();
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        menuController.mostraMenuVolontari(scanner);
    }
    public static void rimuoviDisponibilita(Scanner scanner, int matricolaVolontario) {
        scanner.nextLine();

        try {
            String query = "SELECT * FROM Disponibilita WHERE Matricola_volontario = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, matricolaVolontario);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("LISTA DELLE DISPONIBILITA' FORNITE:");
            while (resultSet.next()) {
                int idDisponibilita = resultSet.getInt("ID_disponibilita");
                String dataDisponibilita = resultSet.getString("Data_disponibilita");
                String tipologia = resultSet.getString("Tipologia");
                String confermata = resultSet.getString("Confermata");

                System.out.println("ID: " + idDisponibilita + " - Data disponibilità: " + dataDisponibilita + " - Tipologia: " + tipologia + " - Stato: " + confermata);
            }

            System.out.println(" ");
            System.out.print("Inserisci l'ID della disponibilità da rimuovere: ");
            int idDaRimuovere = scanner.nextInt();
            scanner.nextLine();

            String verificaQuery = "SELECT * FROM Disponibilita WHERE ID_disponibilita = ? AND Matricola_volontario = ?";
            PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
            verificaStatement.setInt(1, idDaRimuovere);
            verificaStatement.setInt(2, matricolaVolontario);
            ResultSet verificaResultSet = verificaStatement.executeQuery();

            if (verificaResultSet.next()) {
                String confermata = verificaResultSet.getString("Confermata");
                if (confermata.equalsIgnoreCase("Non confermata")) {
                    String deleteQuery = "DELETE FROM Disponibilita WHERE ID_disponibilita = ?";
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                    deleteStatement.setInt(1, idDaRimuovere);
                    deleteStatement.executeUpdate();

                    System.out.println("Disponibilità rimossa autonomamente con successo!");
                    System.out.println(" ");
                    menuController.mostraMenuUtenteNormale(scanner, matricolaVolontario);
                } else if (confermata.equalsIgnoreCase("Reclutato")) {

                    System.out.println("Questa disponibilità è già stata confermata e non può esser rimossa autonomamente. Vuoi richiedere la rimozione ad un amministratore? (s/n).");
                    String risposta = scanner.nextLine();
                    if (risposta.equalsIgnoreCase("s")) {
                        System.out.print("Inserisci il motivo della richiesta di rimozione: ");
                        String motivoRimozione = scanner.nextLine();

                        try {
                            String updateRichiestaQuery = "UPDATE Disponibilita SET Richiesta_Rimozione = true, Motivo_Rimozione = ? WHERE ID_disponibilita = ?";
                            PreparedStatement updateRichiestaStatement = connection.prepareStatement(updateRichiestaQuery);
                            updateRichiestaStatement.setString(1, motivoRimozione);
                            updateRichiestaStatement.setInt(2, idDaRimuovere);
                            updateRichiestaStatement.executeUpdate();
                            updateRichiestaStatement.close();

                            System.out.println("Richiesta di rimozione inviata agli amministratori.");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                System.out.println("Disponibilità non trovata.");
                rimuoviDisponibilita(scanner, matricolaVolontario);
            }

            verificaStatement.close();
            preparedStatement.close();

            System.out.println(" ");
            menuController.mostraMenuUtenteNormale(scanner, matricolaVolontario);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void visualizzaServiziEEmergenzeAssegnate(Scanner scanner, int matricolaVolontario) {
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
    }
    public static void visualizzaDisponibilitaENotificheNonLette(Scanner scanner) {
        try {
            // Recupera le disponibilità non confermate
            String disponibilitaQuery = "SELECT matricola_volontario, data_disponibilita, tipologia FROM Disponibilita WHERE confermata = 'Non confermata'";
            PreparedStatement disponibilitaStatement = connection.prepareStatement(disponibilitaQuery);
            ResultSet disponibilitaResultSet = disponibilitaStatement.executeQuery();

            System.out.println("ELENCO NUOVE DISPONIBILITA'");
            while (disponibilitaResultSet.next()) {
                int matricolaVolontario = disponibilitaResultSet.getInt("matricola_volontario");
                String dataDisponibilita = disponibilitaResultSet.getString("data_disponibilita");
                String tipologia = disponibilitaResultSet.getString("tipologia");
                System.out.println("Matricola: " + matricolaVolontario + " - Data Disponibilità: " + dataDisponibilita + " - Tipologia: " + tipologia);

            }
            System.out.println(" ");
            disponibilitaStatement.close();

            // Recupera le notifiche non lette
            String notificheQuery = "SELECT Matricola_Volontario, Giorno FROM Notifiche WHERE Letta = false";
            PreparedStatement notificheStatement = connection.prepareStatement(notificheQuery);
            ResultSet notificheResultSet = notificheStatement.executeQuery();

            System.out.println("ELENCO NOTIFICHE SERVIZI NON LETTE");
            while (notificheResultSet.next()) {
                int matricolaVolontario = notificheResultSet.getInt("Matricola_Volontario");
                String Giorno = notificheResultSet.getString("Giorno");
                System.out.println("Matricola: " + matricolaVolontario + " - Data servizio: " + Giorno);
            }

            notificheStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Premi un tasto qualsiasi per tornare al menu precedente.");
        scanner.nextLine();
        menuController.mostraMenuAdmin(scanner);
    }
    public static boolean ciSonoDisponibilitaENotificheNonLette() {
        try {
            // Verifica se ci sono disponibilità non confermate
            String disponibilitaQuery = "SELECT COUNT(*) FROM Disponibilita WHERE confermata = 'Non confermata'";
            PreparedStatement disponibilitaStatement = connection.prepareStatement(disponibilitaQuery);
            ResultSet disponibilitaResultSet = disponibilitaStatement.executeQuery();
            disponibilitaResultSet.next();
            int conteggioDisponibilitaNonConfermate = disponibilitaResultSet.getInt(1);

            // Verifica se ci sono notifiche non lette
            String notificheQuery = "SELECT COUNT(*) FROM Notifiche WHERE Letta = false";
            PreparedStatement notificheStatement = connection.prepareStatement(notificheQuery);
            ResultSet notificheResultSet = notificheStatement.executeQuery();
            notificheResultSet.next();
            int conteggioNotificheNonLette = notificheResultSet.getInt(1);

            // Restituisci true se ci sono disponibilità non confermate o notifiche non lette, altrimenti false
            return conteggioDisponibilitaNonConfermate > 0 || conteggioNotificheNonLette > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Gestione delle eccezioni: se si verifica un errore, restituisci false di default
        }
    }
    public static boolean haDisponibilita(int matricolaVolontario) {
        try {
            String query = "SELECT COUNT(*) FROM Disponibilita WHERE matricola_volontario = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, matricolaVolontario);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int numeroDisponibilita = resultSet.getInt(1);
                return numeroDisponibilita > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // In caso di errore o nessuna disponibilità
    }
    public static boolean haServiziOEmergenzeAssegnate(int matricolaVolontario) {
        try {
            // Query per verificare se il volontario ha servizi assegnati
            String serviziQuery = "SELECT * FROM Servizi WHERE Autista = ? OR Soccorritore = ?";
            PreparedStatement serviziStatement = connection.prepareStatement(serviziQuery);
            serviziStatement.setInt(1, matricolaVolontario);
            serviziStatement.setInt(2, matricolaVolontario);
            ResultSet serviziResultSet = serviziStatement.executeQuery();

            // Query per verificare se il volontario ha emergenze assegnate
            String emergenzeQuery = "SELECT * FROM SoccorritoriEmergenza WHERE SoccorritoreID = ?";
            PreparedStatement emergenzeStatement = connection.prepareStatement(emergenzeQuery);
            emergenzeStatement.setInt(1, matricolaVolontario);
            ResultSet emergenzeResultSet = emergenzeStatement.executeQuery();

            // Verifica se ci sono servizi assegnati o emergenze assegnate
            boolean haServiziAssegnati = serviziResultSet.next();
            boolean haEmergenzeAssegnate = emergenzeResultSet.next();

            serviziStatement.close();
            emergenzeStatement.close();

            return haServiziAssegnati || haEmergenzeAssegnate; // Restituisci true se ci sono servizi o emergenze assegnate, altrimenti false

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Gestione delle eccezioni: se si verifica un errore, restituisci false di default
        }
    }
    public static void mostraListaVolontari(Scanner scanner) {
        try {
            // Prepara la query per ottenere la lista di tutti i volontari
            String query = "SELECT * FROM Volontari WHERE id <> 0";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Esegui la query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Mostra i volontari nella lista
            System.out.println("Lista dei volontari:");
            while (resultSet.next()) {
                int matricola = resultSet.getInt("id");
                String nome = resultSet.getString("Nome");
                String cognome = resultSet.getString("Cognome");
                String codiceFiscale = resultSet.getString("codice_fiscale");
                String qualifica = resultSet.getString("Qualifica");

                System.out.println("Matricola: " + matricola + " | Nome: " + nome + " | Cognome: " + cognome + " | Codice Fiscale: " + codiceFiscale + " | Qualifica: " + qualifica);
            }

            // Chiudi le risorse
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Premi un tasto qualsiasi per tornare al menu precedente...");
        scanner.nextLine();
        scanner.nextLine();
        System.out.println(" ");
        menuController.mostraMenuVolontari(scanner);
    }
}
