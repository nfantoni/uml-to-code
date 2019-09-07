package it.nfantoni.utils.worker;

import it.nfantoni.utils.entities.Attributes;
import it.nfantoni.utils.entities.Entity;
import it.nfantoni.utils.settings.Settings;
import it.nfantoni.utils.sql.SqlUtilities;
import it.nfantoni.utils.string.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dao implements Worker {

    private static Logger logger = Logger.getLogger("DAO");

    private static final String ABSTRACT_DAO_PLACEHOLDER = "{$abstract-dao}";
    private static final String SRC_DIRECTORY = "src/dao";
    private static final String CLOSE_METHODS = ");\n\n";

    private List<Entity> entityList;
    private Settings settings;
    private File daoSrcDir = null;

    @Override
    public void work(Settings settings, List<Entity> entityList) throws IOException {
        //TODO implements for Dao Pattern

        this.entityList = entityList;
        this.settings = settings;

        File directory = new File(settings.getOutputPath());
        if(!directory.mkdirs())
            throw new IOException("Directory non creata");

        writeDaoFactory();
        writeEntityDao();
    }

    private void writeDaoFactory() throws IOException {

        daoSrcDir = new File(String.format("%s/%s", settings.getOutputPath(), SRC_DIRECTORY));
        if(!daoSrcDir.mkdirs())
            throw new IOException("Directory non creata");

        StringBuilder stringBuilder = new StringBuilder();

        entityList.forEach(it -> stringBuilder.append("    public abstract ").append(it.getName()).append("DAO get").append(it.getName()).append("DAO();\n"));

        ClassLoader classLoader = getClass().getClassLoader();
        File daoFactoryTemplateFile = new File(Objects.requireNonNull(classLoader.getResource("dao/dao-factory-template.java")).getFile());

        String daoFactoryTemplate = new String ( Files.readAllBytes( Paths.get(daoFactoryTemplateFile.getAbsolutePath()) ) );
        daoFactoryTemplate = daoFactoryTemplate.replace(ABSTRACT_DAO_PLACEHOLDER, stringBuilder.toString());

        try (PrintWriter out = new PrintWriter(daoSrcDir.getPath() + "/DAOFactory.java")) {
            out.println(daoFactoryTemplate);
        }

    }

    private void writeEntityDao(){

        entityList.forEach(it -> {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("package dao;\n\n");
            stringBuilder.append("public interface ").append(it.getName()).append("DAO {\n\n");
            stringBuilder.append("    // --- CRUD -------------\n\n");
            stringBuilder.append("    public void create(").append(it.getName()).append("DTO ").append(StringUtils.decapitalize(it.getName())).append(CLOSE_METHODS);

            final Optional<Attributes> attributes = it.getAttributes().stream().filter(Attributes::getPrimaryKey).findFirst();

            attributes.ifPresent(attributes1 -> stringBuilder.append("    public ").append(it.getName())
                    .append("DTO read(")
                    .append(SqlUtilities.javaType(attributes1.getSqlType()))
                    .append(" ").append(StringUtils.decapitalize(attributes1.getName())).append(CLOSE_METHODS));

            stringBuilder.append("    public boolean update(")
                    .append(it.getName()).append("DTO ")
                    .append(StringUtils.decapitalize(it.getName())).append(CLOSE_METHODS);

            attributes.ifPresent(attributes1 -> stringBuilder.append("    public boolean delete(")
                    .append(SqlUtilities.javaType(attributes1.getSqlType()))
                    .append(" ").append(StringUtils.decapitalize(attributes1.getName())).append(CLOSE_METHODS));

            stringBuilder.append("    // ----------------------------------\n\n");

            stringBuilder.append("    public boolean createTable();\n\n");

            stringBuilder.append("    public boolean dropTable();\n}");

            try (PrintWriter out = new PrintWriter(daoSrcDir.getPath() + "/"+ it.getName() +"DAO.java")) {
                out.println(stringBuilder.toString());
            } catch (FileNotFoundException e) {
                logger.log(Level.WARNING, e.getMessage());
            }

        });

    }

}
