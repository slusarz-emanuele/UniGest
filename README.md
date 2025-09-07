# UniGest

UniGest è un'applicazione JavaFX per la gestione dei dati universitari.

## Requisiti

- Java 21
- Maven 3.9+

## Struttura dei package

Il codice sorgente è organizzato nel package base `it.univaq.unigest` con le seguenti principali sottosezioni:

- `gui` – interfaccia grafica e componenti visuali
- `service` – logica di business e query al database
- `repository` – accesso e persistenza dei dati
- `model` – classi di dominio
- `util` – utilità comuni

## Esecuzione

Per avviare l'applicazione in modalità sviluppo eseguire:

```bash
mvn clean javafx:run
```

## Documentazione

Per generare la documentazione Javadoc:

```bash
mvn javadoc:javadoc
```

## Compilazione

Per compilare il progetto senza eseguirlo:

```bash
mvn clean package
```

## UML

Per generare uml in formato .puml:
```bash
mvn -U clean process-classes
```

Per generare uml in formato .puml + conversione in SVG:
```bash
mvn -U clean package -DskipTests
```