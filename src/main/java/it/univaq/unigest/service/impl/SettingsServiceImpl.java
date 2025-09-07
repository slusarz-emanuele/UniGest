package it.univaq.unigest.service.impl;

import it.univaq.unigest.model.Settings;
import it.univaq.unigest.repository.Repository;
import it.univaq.unigest.service.SettingsService;

public class SettingsServiceImpl implements SettingsService {

    private final Repository<Settings, String> repo;
    private static final String GLOBAL_ID = "global";

    /**
     * Crea il servizio delle Impostazioni iniettando il repository sottostante.
     *
     * @param repo repository per persistere/recuperare le impostazioni
     */
    public SettingsServiceImpl(Repository<Settings, String> repo) {
        this.repo = repo;
    }

    /** {@inheritDoc} */
    @Override
    public Settings get() {
        return repo.findById(GLOBAL_ID).orElseGet(() -> repo.save(Settings.defaults()));
    }

    /** {@inheritDoc} */
    @Override
    public Settings updateCartellaBackup(String path) {
        Settings s = get();
        s.setCartellaBackup(path == null ? "" : path.trim());
        return repo.save(s);
    }
}
