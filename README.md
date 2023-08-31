L'obiettivo primario del programma è fornire una piattaforma avanzata e intuitiva che metta a disposizione degli operatori gli strumenti essenziali per una gestione efficiente e accurata delle risorse all'interno di un'organizzazione di soccorso.

Funzionamento: Ho implementato il programma utilizzando il linguaggio di programmazione Java, affiancato da un database relazionale PostgreSQL dedicato alla gestione e memorizzazione delle informazioni. Attraverso questo sistema, gli operatori possono accedere utilizzando credenziali personali, ciascuno con un accesso differenziato in base al proprio ruolo (amministratore o volontario).



Principali Caratteristiche del Programma:

• Accesso Differenziato: Il sistema consente l'accesso attraverso ruoli distinti (amministratore o volontario -riconosciuto automaticamente durante il login-), ciascuno con autorizzazioni ed azioni specifiche.

• Gestione dei Volontari: I volontari possono essere registrati da amministratori oppure procedere autonomamente alla registrazione, fornendo dati anagrafici e dettagliate qualifiche.
Sono state implementate azioni come aggiunta-modifica-rimozione volontari per amministratori ed azioni come comunicare-rimuovere- modificare disponibilità per volontari.

• Assegnazione Manuale dei Servizi: Gli amministratori hanno la possibilità di assegnare manualmente servizi di emergenza ai volontari, specificando data, orario, paziente e mezzi coinvolti.

• Assegnazione Automatica dei Servizi: Il programma analizza i servizi non ancora assegnati che richiedono un autista e/o un soccorritore, identifica i volontari disponibili per quella data e procede con l'assegnazione. Al completamento dell'assegnazione, il volontario viene informato dell'assegnazione del servizio.

• Gestione dei Mezzi: Il sistema consente una gestione precisa e dettagliata dell'inventario dei mezzi a disposizione. Sono state implementate azioni come aggiunta-modifica-rimozione mezzi per amministratori.

• Monitoraggio dell'Attività: Gli amministratori e i volontari possono monitorare l'attività giornaliera e settimanale con un alto livello di dettaglio, visualizzando tutti i servizi da evadere, i turni da coprire con mezzi e personale a seguito.
