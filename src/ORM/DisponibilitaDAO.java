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

public class DisponibilitaDAO {

    private static Connection connection;

    public DisponibilitaDAO(Connection connection) {
        this.connection = connection;
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


}
