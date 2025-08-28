package it.univaq.unigest.util;

/**
 * Tipi di messaggi di log utilizzati nel sistema.
 * <p>
 * Ogni valore rappresenta una tipologia di messaggio che può essere registrato
 * o visualizzato nel contesto dell'applicazione.
 * </p>
 */
public enum LogType {
    /** Messaggi informativi generali. */
    INFO,

    /** Messaggi relativi a errori che richiedono attenzione. */
    ERROR,

    /** Messaggi di avviso che segnalano potenziali problemi. */
    WARNING,

    /** Messaggi di debug, usati per analisi e sviluppo. */
    DEBUG
}