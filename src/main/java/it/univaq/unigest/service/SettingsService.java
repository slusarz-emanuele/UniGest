package it.univaq.unigest.service;

import it.univaq.unigest.model.Settings;

public interface SettingsService {
    Settings get();
    Settings updateCartellaBackup(String path);
}
