package it.nfantoni.utils.worker;

import it.nfantoni.utils.entities.Entity;
import it.nfantoni.utils.settings.Settings;

import java.io.IOException;
import java.util.List;

public interface Worker {

    void work(Settings settings, List<Entity> entityList) throws IOException;
}
