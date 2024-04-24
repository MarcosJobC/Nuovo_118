# Gestionale 118

Relazione progetto: https://www.overleaf.com/read/gnqxmxkhbxsr#6d9bc7

L'obiettivo primario del programma è fornire una piattaforma avanzata e intuitiva che metta a disposizione degli operatori gli strumenti essenziali per una gestione efficiente e accurata delle risorse all'interno di un'organizzazione di soccorso.

### Funzionamento
Ho implementato il programma utilizzando il linguaggio di programmazione Java, affiancato da un database relazionale PostgreSQL dedicato alla gestione e memorizzazione delle informazioni. Attraverso questo sistema, gli operatori possono accedere utilizzando credenziali personali, ciascuno con un accesso differenziato in base al proprio ruolo (amministratore o volontario).



### Principali Caratteristiche del Programma:

• Accesso Differenziato: Il sistema consente l'accesso attraverso ruoli distinti (amministratore o volontario -riconosciuto automaticamente durante il login-), ciascuno con autorizzazioni ed azioni specifiche.

• Gestione dei Volontari: I volontari possono essere registrati da amministratori oppure procedere autonomamente alla registrazione, fornendo dati anagrafici e dettagliate qualifiche.
Sono state implementate azioni come aggiunta-modifica-rimozione volontari per amministratori ed azioni come comunicare-rimuovere- modificare disponibilità per volontari.

• Assegnazione Manuale dei Servizi: Gli amministratori hanno la possibilità di assegnare manualmente servizi di emergenza ai volontari, specificando data, orario, paziente e mezzi coinvolti.

• Assegnazione Automatica dei Servizi: Il programma analizza i servizi non ancora assegnati che richiedono un autista e/o un soccorritore, identifica i volontari disponibili per quella data e procede con l'assegnazione. Al completamento dell'assegnazione, il volontario viene informato dell'assegnazione del servizio.

• Gestione dei Mezzi: Il sistema consente una gestione precisa e dettagliata dell'inventario dei mezzi a disposizione. Sono state implementate azioni come aggiunta-modifica-rimozione mezzi per amministratori.

• Monitoraggio dell'Attività: Gli amministratori e i volontari possono monitorare l'attività giornaliera e settimanale con un alto livello di dettaglio, visualizzando tutti i servizi da evadere, i turni da coprire con mezzi e personale a seguito.


## Miglioramenti effettuate nel corso del tempo...

### Per gli Amministratori:
#### Gestione Pazienti:
Aggiunta una sezione per gestire i pazienti.
Possibilità di aggiungere, modificare ed eliminare pazienti.
Modifica del servizio per includere l'ID del paziente al posto del nome paziente (privacy).


#### Menu iniziale
Non consentire l'accesso se i campi CF (Codice Fiscale) e password sono vuoti.
Reindirizzamento al menu iniziale dopo la registrazione.
Qualifica Main.BusinessLogic.AmministratoreController:

#### Eliminazione Volontari:
Risolvere il problema delle notifiche, disponibilità e servizi associati quando si elimina un volontario.
Modifica Mezzi:

Migliorare la formattazione dei dati dei mezzi (Sigla mezzo, Targa, Tipologia).
Assicurarsi che venga selezionata solo la tipologia corretta (1, 2 o 3).
Reindirizzamento al menu mezzi dopo la modifica.

#### Aggiunta Mezzi:
Non può esser lasciato vuoto alcun campo nell'aggiunta del mezzo nuovo
Ritorno al menu mezzi dopo l'inserimento
#### Eliminazione Mezzi:
Gestire la situazione in cui viene inserita una sigla mezzo errata.
Reindirizzamento al menu mezzi dopo l'eliminazione.

#### Modifica Anagrafe Volontari:
Migliorare la formattazione dei dati dei volontari (Codice fiscale, Nome, Cognome, ecc.).
Semplificare le richieste di modifica.
Reindirizzamento al menu volontari dopo la modifica.

#### Eliminazione Volontari:
Migliorare la formattazione dei dati dei volontari (Codice fiscale, Nome, Cognome, ecc.).
Reindirizzamento al menu volontari dopo l'eliminazione.

#### Gestione Servizi:
Non permettere di inserire date o orari precedenti o vuoti.
Reindirizzamento al menu servizi dopo l'aggiunta o la modifica dei servizi.

#### Visualizzazione Disponibilità e Notifiche:
Nascondere la voce se non ci sono disponibilità o notifiche non lette.

#### Assegnazione Automatica:
Tornare al menu servizi dopo aver assegnato automaticamente i servizi.



### Per gli Utenti:
#### Generali
I volontari possono essere registrati solo se hanno almeno 16 anni e una data di nascita valida.
Reindirizzamento al menu principale dopo la registrazione.
#### Rimozione Disponibilità:
Rimuovere l'opzione di rimozione se non ci sono disponibilità date.

#### Inserimento Disponibilità:
Impedire l'inserimento di date precedenti a oggi.

#### Visualizzazione Servizi Assegnati:
Nascondere la voce se non ci sono servizi assegnati.

