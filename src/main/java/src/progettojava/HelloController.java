package src.progettojava;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.awt.*;
import javafx.scene.input.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HelloController extends Component {

    // INIZIALIZZAZIONE VARIABILI
    @FXML
    private Pane primo, seleziona_modalita, regole_single, regole_multi, single_player, inserisci_parola, vittoria, sconfitta;

    @FXML
    private TextField field;
    @FXML
    private ImageView base, asta, parte_alta, corda, testa, busto, braccia, gambe, lettera_giusta, lettera_sbagliata;
    private List<ImageView> impiccato = new ArrayList<>();

    @FXML
    private Label trattino1, trattino2, trattino3, trattino4, trattino5, trattino6, trattino7, trattino8, trattino9, trattino10, trattino11, trattino12, parola, parola1, lettera_presente;
    private List<Label> trattini = new ArrayList<>();

    private List<Character> lettereInserite = new ArrayList<>();

    private String parola_utente;
    private String parola_generata;
    private char lettera;
    private int numero_errori = 0;


    //INIZIO CODICE
    //CODICI COMUNI
    public void cambia(ActionEvent actionEvent) { // SELEZIONARE LA MODALITA
        primo.setVisible(false);
        seleziona_modalita.setVisible(true);
    }

    public void regole_giocatore_singolo(ActionEvent actionEvent) { // REGOLE GIOCATORE SINGOLO
        seleziona_modalita.setVisible(false);
        regole_multi.setVisible(false);
        regole_single.setVisible(true);
    }

    public void regole_multi_giocatore(ActionEvent actionEvent) { // REGOLE MULTI GIOCATORE
        seleziona_modalita.setVisible(false);
        regole_single.setVisible(false);
        regole_multi.setVisible(true);
    }

    //MULTIGIOCATORE
    public void inizializza_multigiocatore(ActionEvent actionEvent) { //GRAFICA DOVE SI INSERISCE LA PAROLA
        seleziona_modalita.setVisible(false);
        single_player.setVisible(false);
        regole_multi.setVisible(false);
        inserisci_parola.setVisible(true);
    }

    public void due_giocatori(ActionEvent actionEvent) { //GIOCO GIOCATORE SINGOLO
        seleziona_modalita.setVisible(false);
        regole_single.setVisible(false);
        inserisci_parola.setVisible(false);
        single_player.setVisible(true);

        inizializza();

        parola_utente = leggiParola();

        mostra_trattini(parola_utente);

        single_player.setOnKeyPressed(this::gestisciTastoPremuto_multigiocatore);
        single_player.requestFocus();

    }

    //------------------------------------------------------------------------------------------------------------------
    //GIOCATORE SINGOLO
    public void giocatore_singolo(ActionEvent actionEvent) {
        seleziona_modalita.setVisible(false);
        regole_single.setVisible(false);
        inserisci_parola.setVisible(false);
        single_player.setVisible(true);

        inizializza();

        List<String> parole = caricaParole();

        parola_generata = parole.get(new Random().nextInt(parole.size()));

        mostra_trattini(parola_generata);

        single_player.setOnKeyPressed(this::gestisciTastoPremuto_giocatore_singolo);
        single_player.requestFocus();
    }

    //------------------------------------------------------------------------------------------------------------------
    // PARITA FINITA
    public void torna_principale(ActionEvent actionEvent) { // FRECCIA CHE TORNA ALLA HOME
        seleziona_modalita.setVisible(false);
        primo.setVisible(true);
    }

    public void torna_selezionaModalita(ActionEvent actionEvent) { //FRECCIA CHE TORNA ALLA SELEZIONA MODALITA
        single_player.setVisible(false);
        inserisci_parola.setVisible(false);
        seleziona_modalita.setVisible(true);
    }

    public void nuova_partita(ActionEvent actionEvent) {
        reset();
        vittoria.setVisible(false);
        sconfitta.setVisible(false);
        primo.setVisible(false);
        seleziona_modalita.setVisible(true);
    }

    public void exit(ActionEvent actionEvent) { //CHIUSURA PROGRAMMA
        System.exit(0);
    }

    //------------------------------------------------------------------------------------------------------------------------------------
    //METODI

    public void inizializza() {
        impiccato.add(base);
        impiccato.add(asta);
        impiccato.add(parte_alta);
        impiccato.add(corda);
        impiccato.add(testa);
        impiccato.add(busto);
        impiccato.add(braccia);
        impiccato.add(gambe);

        trattini.add(trattino1);
        trattini.add(trattino2);
        trattini.add(trattino3);
        trattini.add(trattino4);
        trattini.add(trattino5);
        trattini.add(trattino6);
        trattini.add(trattino7);
        trattini.add(trattino8);
        trattini.add(trattino9);
        trattini.add(trattino10);
        trattini.add(trattino11);
        trattini.add(trattino12);


        for (ImageView i : impiccato) {
            i.setVisible(false);
        }

        for (Label l : trattini) {
            l.setVisible(false);
        }

        lettera_presente.setVisible(false);
        lettera_giusta.setVisible(false);
        lettera_sbagliata.setVisible(false);
    }

    //PAROLA UTENTE
    public String leggiParola() {
        parola_utente = field.getText();
        if (parola_utente.length() > 12) {
            parola_utente = parola_utente.substring(0, 12);
        }
        return parola_utente;
    }

    //PAROLA CASUALE
    private static List<String> caricaParole() {
        List<String> parole = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/src/progettojava/parole_impiccato.txt"))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                parole.add(linea.trim().toLowerCase());
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }
        return parole;
    }

    public void mostra_trattini(String parola) {
        for (int i = 0; i < parola.length(); i++) {
            trattini.get(i).setVisible(true);
        }
    }

    //CONTROLLI PAROLE
    public void controllo(char lettera, String parola_indovinare) {
        boolean flag = false;
        StringBuilder parola_completata = new StringBuilder();
        lettera_giusta.setVisible(false);
        for (int i = 0; i < parola_indovinare.length(); i++) {
            if (parola_indovinare.charAt(i) == lettera) {
                trattini.get(i).setText(String.valueOf(lettera));
                flag = true;
            }
            if (!trattini.get(i).getText().isEmpty()) {
                parola_completata.append(trattini.get(i).getText());
            } else {
                parola_completata.append("_");
            }
        }

        if (parola_completata.toString().equals(parola_indovinare)) {
            single_player.setVisible(false);
            vittoria.setVisible(true);
            sconfitta.setVisible(false);
            parola.setText(parola_indovinare);
        }

        if (!flag) {
            impiccato.get(numero_errori).setVisible(true);
            numero_errori++;
            lettera_sbagliata.setVisible(true);
        }
        else {
            lettera_giusta.setVisible(true);
            lettera_sbagliata.setVisible(false);
        }

        if (numero_errori == 8) {
            single_player.setVisible(false);
            vittoria.setVisible(false);
            sconfitta.setVisible(true);
            parola1.setText(parola_indovinare);
        }
    }


    //--------------------------------------------------------------------------------------------------------------------
        public void gestisciTastoPremuto_multigiocatore(KeyEvent event) {
            if (!event.getText().isEmpty()) {
                lettera = event.getText().charAt(0);

                // Controlliamo se la lettera è già stata inserita
                if (lettereInserite.contains(lettera)) {
                    lettera_giusta.setVisible(false);
                    lettera_presente.setVisible(true);
                    lettera_sbagliata.setVisible(true);
                    return;
                }
                else {
                    lettera_presente.setVisible(false);
                    lettera_sbagliata.setVisible(false);
                }

                // Aggiungiamo la lettera alla lista delle lettere già usate
                lettereInserite.add(lettera);
                controllo(lettera, parola_utente);
            }
        }

        public void gestisciTastoPremuto_giocatore_singolo (KeyEvent event){
            if (!event.getText().isEmpty()) {
                lettera = event.getText().charAt(0);

                // Controlliamo se la lettera è già stata inserita
                if (lettereInserite.contains(lettera)) {
                    lettera_giusta.setVisible(false);
                    lettera_presente.setVisible(true);
                    lettera_sbagliata.setVisible(true);
                    return;
                }
                else {
                    lettera_presente.setVisible(false);
                    lettera_sbagliata.setVisible(false);
                }

                // Aggiungiamo la lettera alla lista delle lettere già usate
                lettereInserite.add(lettera);
                controllo(lettera, parola_generata);
            }
        }


        //RESET
        public void reset () {

            parola_utente = "";
            parola_generata = "";
            lettera = '\0';
            numero_errori = 0;
            lettereInserite.clear();


            for (ImageView i : impiccato) {
                i.setVisible(false);
            }


            for (Label l : trattini) {
                l.setVisible(false);
                l.setText("-");
            }


            if (field != null) {
                field.clear();
            }

            single_player.setVisible(false);
            seleziona_modalita.setVisible(true);
        }
}



