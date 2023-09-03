import java.sql.*;
import java.util.Scanner;

public class volontariManager {

    private static Connection connection;
    public volontariManager(Connection connection) {
        this.connection = connection;
    }


    //GESTIONE VOLONTARI
    public static void registrazione(Scanner scanner,boolean sceltaValida) {
        sceltaValida = true;
        System.out.println("Inserisci il nome:");
        String nome = scanner.nextLine();

        System.out.println("Inserisci il cognome:");
        String cognome = scanner.nextLine();

        System.out.println("Inserisci la data di nascita (dd-mm-yyyy):");
        String dataDiNascita = scanner.nextLine();

        System.out.println("Inserisci la qualifica:");
        String qualifica = scanner.nextLine();

        System.out.println("Inserisci il codice fiscale:");
        String codicefiscale = scanner.nextLine();

        System.out.println("Inserisci la password:");
        String password = scanner.nextLine();

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
    public static void accesso(Scanner scanner,boolean sceltaValida) {
        sceltaValida = true;
        System.out.println("Inserisci il codice fiscale:");
        String codicefiscale = scanner.nextLine();

        System.out.println("Inserisci la password:");
        String password = scanner.nextLine();

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

                if (isAdmin) {
                    // Mostra il menu per gli utenti amministratori
                    menuManager.mostraMenuAdmin(scanner);
                } else {
                    // Mostra il menu per gli utenti non amministratori
                    menuManager.mostraMenuUtenteNormale(scanner,matricolaVolontario);
                }
            } else {
                System.out.println("Accesso negato. Codice fiscale o password errati.");
                System.out.println(" ");
                System.out.println(" ");
                menuManager.menuIniziale(scanner);
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void modificaAnagrafeVolontari(Scanner scanner) {
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
                System.out.println("Nome: "+nome + " | Cognome: " + cognome + " | CF: (" + codiceFiscale + ")");
            }

            boolean volontarioTrovato = false;

            while (!volontarioTrovato) {
                System.out.print("Inserisci il codice fiscale del volontario da modificare o premi 'q' per tornare al menu precedente: ");
                String codiceFiscaleVolontario = scanner.nextLine();

                if (codiceFiscaleVolontario.equalsIgnoreCase("q")) {
                    System.out.println("Annullamento dell'operazione.");
                    System.out.println("  ");
                    System.out.println("  ");
                    menuManager.mostraMenuVolontari(scanner);
                }

                String verificaQuery = "SELECT * FROM Volontari WHERE Codice_fiscale = ?";
                PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                verificaStatement.setString(1, codiceFiscaleVolontario);
                ResultSet volontarioResultSet = verificaStatement.executeQuery();

                if (volontarioResultSet.next()) {
                    System.out.print("Volontario trovato. Inserisci i nuovi dati: ");

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
                    System.out.println("Volontario non trovato.");
                }

                verificaStatement.close();
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        menuManager.mostraMenuVolontari(scanner);
    }
    public static void eliminaVolontario(Scanner scanner) {
        scanner.nextLine();
        try {
            String query = "SELECT * FROM Volontari";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Lista dei volontari:");
            while (resultSet.next()) {
                String nome = resultSet.getString("Nome");
                String cognome = resultSet.getString("Cognome");
                String codiceFiscale = resultSet.getString("codice_fiscale");
                System.out.println("Nome: "+nome + " | Cognome: " + cognome + " | CF: (" + codiceFiscale + ")");
            }

            boolean volontarioTrovato = false;

            while (!volontarioTrovato) {
                System.out.print("Inserisci il codice fiscale del volontario da modificare o premi 'q' per tornare al menu precedente: ");
                String codiceFiscaleVolontario = scanner.nextLine();

                if (codiceFiscaleVolontario.equalsIgnoreCase("q")) {
                    System.out.println("Annullamento dell'operazione.");
                    System.out.println("  ");
                    System.out.println("  ");
                    menuManager.mostraMenuVolontari(scanner);
                }

                String verificaQuery = "SELECT * FROM Volontari WHERE Codice_fiscale = ?";
                PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
                verificaStatement.setString(1, codiceFiscaleVolontario);
                ResultSet risultatoVerifica = verificaStatement.executeQuery();

                if (risultatoVerifica.next()) {
                    System.out.println("Volontario trovato. Sei sicuro di volerlo eliminare? (s/n)");
                    String conferma = scanner.nextLine();

                    if (conferma.equalsIgnoreCase("s")) {
                        String deleteQuery = "DELETE FROM Volontari WHERE Codice_fiscale = ?";
                        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                        deleteStatement.setString(1, codiceFiscaleVolontario);
                        deleteStatement.executeUpdate();

                        System.out.println("Volontario eliminato con successo!");
                        System.out.println("  ");
                        System.out.println("  ");
                        volontarioTrovato = true;
                    } else {
                        System.out.println("Operazione di eliminazione annullata.");
                    }
                } else {
                    System.out.println("Volontario non trovato.");
                }
                verificaStatement.close();
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        menuManager.mostraMenuVolontari(scanner);
    }



    public static void inserisciDisponibilita(Scanner scanner, int matricolaVolontario) {

        String dataDisponibilita = "";
        while (dataDisponibilita.isEmpty()) {
            System.out.print("Inserisci la data della disponibilità (dd/mm/yyyy): ");
            dataDisponibilita = scanner.nextLine();

            if (dataDisponibilita.isEmpty()) {
                System.out.println("La data non può essere vuota. Riprova.");
            }
        }

        System.out.print("Seleziona la tipologia del servizio: Emergenza(E) | Servizi sociali (S) | Centralino(C) | lascia vuoto per qualsiasi ruolo: ");
        String sceltaTipologia = scanner.nextLine().toUpperCase();

        String tipologia;
        switch (sceltaTipologia) {
            case "E":
                tipologia = "Emergenza";
                break;
            case "S":
                tipologia = "Servizi sociali";
                break;
            case "C":
                tipologia = "Centralino";
                break;
            default:
                System.out.println("Tipologia impostata a: qualsiasi ruolo.");
                tipologia = "Qualsiasi";
        }

        try {
            String insertQuery = "INSERT INTO Disponibilita (Matricola_volontario, Data_disponibilita, Tipologia, Confermata) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setInt(1, matricolaVolontario);
            insertStatement.setString(2, dataDisponibilita);
            insertStatement.setString(3, tipologia);
            insertStatement.setString(4, "Non confermata");

            insertStatement.executeUpdate();

            System.out.println(" ");
            System.out.println("Disponibilità inserita con successo! Grazie mille!");
            insertStatement.close();
            System.out.println(" ");
            menuManager.mostraMenuUtenteNormale(scanner, matricolaVolontario);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                    menuManager.mostraMenuUtenteNormale(scanner, matricolaVolontario);
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
            menuManager.mostraMenuUtenteNormale(scanner, matricolaVolontario);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void visualizzaServiziAssegnati(Scanner scanner, int matricolaVolontario) {
        try {
            String query = "SELECT * FROM Servizi WHERE Autista = ? OR Soccorritore = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, matricolaVolontario);
            preparedStatement.setInt(2, matricolaVolontario);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Servizi assegnati:");

            while (resultSet.next()) {
                String giorno = resultSet.getString("Data");
                Time orario = resultSet.getTime("Orario");
                String siglaMezzo = resultSet.getString("Sigla_Mezzo");

                System.out.println("Data: " + giorno + " - Orario: " + orario + " - Mezzo: " + siglaMezzo);
            }

            preparedStatement.close();


            System.out.println(" ");
            menuManager.mostraMenuUtenteNormale(scanner, matricolaVolontario);

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
                System.out.println("Matricola: " + matricolaVolontario + " - Data Disponibilità: " + dataDisponibilita+ " - Tipologia: " + tipologia);

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
        menuManager.mostraMenuAdmin(scanner);
    }



    public static boolean haDisponibilita(int matricolaVolontario) {
        try {
            String query = "SELECT * FROM Disponibilita WHERE Matricola_volontario = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, matricolaVolontario);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next(); // Restituisci true se ci sono disponibilità, altrimenti false

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Gestione delle eccezioni: se si verifica un errore, restituisci false di default
        }
    }




}
