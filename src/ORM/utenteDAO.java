package ORM;
import BusinessLogic.*;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class utenteDAO {

    private static Connection connection;
    public utenteDAO(Connection connection) {
        this.connection = connection;
    }


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
    public static void modificaUtenteDAO(Scanner scanner) {
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



    public static void visualizzaDisponibilitaENotificheNonLette(Scanner scanner) {
        //TODO metto nel Amministratorecontroller
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
        //TODO metto nel Utentecontroller
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
    public static boolean haServiziAssegnati(int matricolaVolontario) {
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



    public static int trovaVolontarioDisponibileNonConfermato(String dataServizio, LocalTime orarioServizio) {
        try {
            String disponibilitaQuery = "SELECT matricola_volontario, ora_inizio, ora_fine " +
                    "FROM Disponibilita WHERE data_disponibilita = ? " +
                    "AND confermata = 'Non confermata' " +
                    "AND tipologia = 'Servizi sociali' " +
                    "AND ? BETWEEN ora_inizio AND ora_fine"; // Aggiungi il vincolo sull'orario
            PreparedStatement disponibilitaStatement = connection.prepareStatement(disponibilitaQuery);
            disponibilitaStatement.setString(1, dataServizio);
            disponibilitaStatement.setTime(2, Time.valueOf(orarioServizio)); // Usa Time.valueOf per convertire LocalTime in Time
            ResultSet disponibilitaResultSet = disponibilitaStatement.executeQuery();

            while (disponibilitaResultSet.next()) {
                LocalTime oraInizioDisponibilita = disponibilitaResultSet.getTime("ora_inizio").toLocalTime();
                LocalTime oraFineDisponibilita = disponibilitaResultSet.getTime("ora_fine").toLocalTime();

                int volontarioDisponibile = disponibilitaResultSet.getInt("matricola_volontario");
                // Verifica se il volontario è disponibile (non è già assegnato) e se l'orario è corretto
                if (!verificaVolontarioAssegnato(dataServizio, volontarioDisponibile) &&
                        orarioServizio.isAfter(oraInizioDisponibilita) &&
                        orarioServizio.isBefore(oraFineDisponibilita)) {
                    return volontarioDisponibile;
                }
            }

            disponibilitaStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0; // Nessun volontario disponibile con tipologia "Servizi sociali" trovato
    }
    public static void cambiaStatoVolontario(int matricolaVolontario, String dataServizio) {
        try {
            String updateStatoQuery = "UPDATE Disponibilita SET confermata = 'Reclutato' WHERE matricola_volontario = ? AND data_disponibilita = ? AND confermata = 'Non confermata'";
            PreparedStatement updateStatoStatement = connection.prepareStatement(updateStatoQuery);
            updateStatoStatement.setInt(1, matricolaVolontario);
            updateStatoStatement.setString(2, dataServizio);
            updateStatoStatement.executeUpdate();
            updateStatoStatement.close();


            // Creazione della notifica
            notificaDAO.inviaNotificaVolontario(matricolaVolontario, dataServizio);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static boolean verificaVolontarioAssegnato(String dataServizio, int matricolaVolontario) {
        try {
            String verificaAssegnazioneQuery = "SELECT Id FROM Servizi WHERE (Autista = ? OR Soccorritore = ?) AND Data = ?";
            PreparedStatement verificaAssegnazioneStatement = connection.prepareStatement(verificaAssegnazioneQuery);
            verificaAssegnazioneStatement.setInt(1, matricolaVolontario);
            verificaAssegnazioneStatement.setInt(2, matricolaVolontario);
            verificaAssegnazioneStatement.setString(3, dataServizio);
            ResultSet assegnazioneResultSet = verificaAssegnazioneStatement.executeQuery();

            boolean volontarioAssegnato = assegnazioneResultSet.next();
            assegnazioneResultSet.close();

            return volontarioAssegnato;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // In caso di errore, considera il volontario non assegnato
        }
    }



}
