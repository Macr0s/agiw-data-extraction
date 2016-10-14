# data-extraction
Il progetto è diviso principalmente in 2 parti:
- la prima parte che dato un file .tsv (Tab-separated values) in un formato specificato sotto effettua le richieste ai siti web e crea un file (nel nostro esempio chiamato urlToCode.csv) che contiene i siti e i codici ricavati con gli xpath.
Per la creazione di questo tsv noi usavamo un foglio excel su drive esportandolo poi come tsv.
- la seconda parte effettua la creazione dei file json **data.cognome.json** e **xpath.cognome.json** apartire dal file **urlToCode.csv**
E' stato diviso in questo modo per poter permettere di ricreare i json senza dover rieffettuare le richieste delle pagine. 

Il formato del file excel per creare il tsv è il seguente:  
[google doc sheet](https://docs.google.com/spreadsheets/d/17Auedavi5VWnPfuGmY6VP_6a2-MfN7Buj_rwKoqylGQ/pubhtml)  
![example image](http://imgur.com/a/HIBaF)

Dovete creare un file config.properties come da esempio (config.properties.example) dove vanno specificati i nomi dei file seguenti:
- data.cognome e xpath.cognome, i nomi dei file di output
- agiwTsv, il nome del file tsv iniziale
- urlToCode, il nome del file intermedio creato dalle richieste alle pagine
- file, il nome del file contente le risorse a voi assegnate
- (le ultime 3 possono essere ignorate, servivano per prove al volo)

Ricordatevi di installare le dipendeze con Maven dopo aver pullato (mvn install -f pom.xml) oppure da un qualsiasi IDE tra le opzioni.

## Funzionamento:
- create l'excel con siti e regole xpath
- configurate il file **config.properties**
- fate partire il main **UrlToCodeWriter.java** per far girare gli xpath sulle pagine
- fate partire il main in **JSONWriter.java** per fare creare i due file .json

N.B. Il codice non rappresentava la parte principale del progetto e potrebbe presentare dei bug, per qualsiasi dubbio contattateci.