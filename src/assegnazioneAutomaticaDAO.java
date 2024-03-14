import BusinessLogic.menuController;

import java.sql.*;
import java.time.LocalTime;
import java.util.Scanner;

import static ORM.utenteDAO.cambiaStatoVolontario;
import static ORM.utenteDAO.trovaVolontarioDisponibileNonConfermatoSOCIALI;


public class assegnazioneAutomaticaDAO {

    private static Connection connection;
    public assegnazioneAutomaticaDAO(Connection connection) {
        this.connection = connection;
    }

    //ASSEGNAZIONE AUTOMATICA VOLONTARI A SERVIZI






}
