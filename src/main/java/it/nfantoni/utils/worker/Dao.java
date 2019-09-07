package it.nfantoni.utils.worker;

import it.nfantoni.utils.entities.Entity;
import it.nfantoni.utils.settings.Settings;

import java.io.File;
import java.util.List;

public class Dao implements Worker {

    @Override
    public void work(Settings settings, List<Entity> entityList) {
        //TODO implements for Dao Pattern

        File directory = new File(settings.getOutputPath());
        directory.mkdirs();
    }

    private void writeDaoEntity(Entity entity){


    }

}
