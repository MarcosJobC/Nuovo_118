package BusinessLogic;

import ORM.mezzoDAO;

import java.util.Scanner;

public class MezzoController {

    public static void aggiungiMezzo(Scanner scanner) {
        String siglaMezzo;
        String targa;
        String tipologia;

        do {
            System.out.println("Inserisci la sigla del mezzo:");
            siglaMezzo = scanner.nextLine().trim(); // Rimuovi spazi iniziali e finali

            if (siglaMezzo.isEmpty()) {
                System.out.println("La sigla del mezzo non può essere vuota. Riprova.");
            }
        } while (siglaMezzo.isEmpty());

        do {
            System.out.println("Inserisci la targa del mezzo:");
            targa = scanner.nextLine().trim(); // Rimuovi spazi iniziali e finali

            if (targa.isEmpty()) {
                System.out.println("La targa del mezzo non può essere vuota. Riprova.");
            }
        } while (targa.isEmpty());

        do {
            System.out.println("Seleziona la tipologia del mezzo:");
            System.out.println("1. Auto");
            System.out.println("2. Ambulanza");
            System.out.println("3. Mezzo attrezzato");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choice = 0; // Impostiamo una scelta non valida in caso di input non numerico
            }

            switch (choice) {
                case 1:
                    tipologia = "Auto";
                    break;
                case 2:
                    tipologia = "Ambulanza";
                    break;
                case 3:
                    tipologia = "BusinessLogic.Mezzo attrezzato";
                    break;
                default:
                    System.out.println("Scelta non valida. Devi selezionare 1, 2 o 3.");
                    tipologia = ""; // Imposta tipologia a vuoto per ripetere il ciclo
            }
        } while (tipologia.isEmpty());
        mezzoDAO.aggiungiMezzoDAO(siglaMezzo,  targa, tipologia );
        menuController.mostraMenuMezzi(scanner);
    }
}