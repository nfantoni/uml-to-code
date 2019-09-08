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
    private static final String PUBLIC = "\tpublic ";
    private static final String CLOSE_FUNCTION = "; }\n\n";
    private static final String DTO_OVERRIDE_PLACEHOLDR ="{$dto-override}";
    private static final String IMPORT_DAO_PLACEHOLDER = "{$import-dao}";


    private List<Entity> entityList;
    private Settings settings;
    private File daoSrcDir = null;

    @Override
    public void work(Settings settings, List<Entity> entityList) throws IOException {
        //TODO implements for Dao Pattern

        this.entityList = entityList;
        this.settings = settings;

        File directory = new File(settings.getOutputPath());
        if (!directory.mkdirs())
            throw new IOException("Directory output non creata");

        writeDaoFactory();
        writeEntityDao();
        writeEntityDTO();
        writeDb2DaoFactory();
    }

    private void writeDaoFactory() throws IOException {

        daoSrcDir = new File(String.format("%s/%s", settings.getOutputPath(), SRC_DIRECTORY));
        if (!daoSrcDir.mkdirs())
            throw new IOException("Directory non creata");

        StringBuilder stringBuilder = new StringBuilder();

        entityList.forEach(it -> stringBuilder.append("    public abstract ").append(it.getName()).append("DAO get").append(it.getName()).append("DAO();\n"));

        ClassLoader classLoader = getClass().getClassLoader();
        File daoFactoryTemplateFile = new File(Objects.requireNonNull(classLoader.getResource("dao/dao-factory-template.java")).getFile());

        String daoFactoryTemplate = new String(Files.readAllBytes(Paths.get(daoFactoryTemplateFile.getAbsolutePath())));
        daoFactoryTemplate = daoFactoryTemplate.replace(ABSTRACT_DAO_PLACEHOLDER, stringBuilder.toString());

        try (PrintWriter out = new PrintWriter(daoSrcDir.getPath() + "/DAOFactory.java")) {
            out.println(daoFactoryTemplate);
        }

    }

    private void writeEntityDao() {

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

            try (PrintWriter out = new PrintWriter(daoSrcDir.getPath() + "/" + it.getName() + "DAO.java")) {
                out.println(stringBuilder.toString());
            } catch (FileNotFoundException e) {
                logger.log(Level.WARNING, e.getMessage());
            }

        });

    }

    private void writeEntityDTO() {

        entityList.forEach(it -> {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("package dao;\n\n");

            //check if present sqltype DATE
            if (it.getAttributes().stream().anyMatch(att -> att.getSqlType().contains("DATE")))
                stringBuilder.append("import java.util.Date;\n\n");

            stringBuilder.append("public class ").append(it.getName()).append("DTO {\n\n");

            it.getAttributes().forEach(attributes -> stringBuilder.append("\tprivate ")
                    .append(SqlUtilities.javaType(attributes.getSqlType())).append(" ")
                    .append(StringUtils.decapitalize(attributes.getName())).append(";\n"));

            it.getAssociations().stream().filter(item -> item.getMultiplicity().equals("*")).forEach(ass ->
                    entityList.stream().filter(ent -> ent.getName().equals(ass.getClassName()))
                            .forEach(entity -> entity.getAttributes().stream()
                                    .filter(Attributes::getPrimaryKey).forEach(attributes ->
                                            stringBuilder.append("\tprivate ")
                                                    .append(SqlUtilities.javaType(attributes.getSqlType())).append(" ")
                                                    .append(StringUtils.decapitalize(attributes.getName())).append(entity.getName()).append(";\n")
                                    )

                            )

            );

            stringBuilder.append("\n").append(PUBLIC).append(it.getName()).append("DTO() {\n" +
                    "\n" +
                    "\t}\n\n");

            it.getAttributes().forEach(attributes -> {
                stringBuilder.append(PUBLIC)
                        .append(SqlUtilities.javaType(attributes.getSqlType()))
                        .append(" get")
                        .append(attributes.getName())
                        .append("() { return ").append(StringUtils.decapitalize(attributes.getName()))
                        .append(CLOSE_FUNCTION);

                stringBuilder.append("\tpublic void ")
                        .append(" set")
                        .append(attributes.getName()).append("(")
                        .append(SqlUtilities.javaType(attributes.getSqlType()))
                        .append(" ").append(StringUtils.decapitalize(attributes.getName())).append(") { this.")
                        .append(StringUtils.decapitalize(attributes.getName())).append(" = ").append(StringUtils.decapitalize(attributes.getName()))
                        .append(CLOSE_FUNCTION);

            });

            it.getAssociations().stream().filter(item -> item.getMultiplicity().equals("*")).forEach(ass ->
                    entityList.stream().filter(ent -> ent.getName().equals(ass.getClassName()))
                            .forEach(entity -> entity.getAttributes().stream()
                                    .filter(Attributes::getPrimaryKey).forEach(attributes -> {
                                        stringBuilder.append(PUBLIC).append(SqlUtilities.javaType(attributes.getSqlType()))
                                                .append(" get").append(attributes.getName()).append(entity.getName())
                                                .append("() { return ").append(StringUtils.decapitalize(attributes.getName()))
                                                .append(entity.getName()).append(CLOSE_FUNCTION);

                                        stringBuilder.append("\tpublic void set").append(attributes.getName()).append(entity.getName())
                                                .append("() { this.").append(StringUtils.decapitalize(attributes.getName()))
                                                .append(entity.getName()).append(" = ")
                                                .append(StringUtils.decapitalize(attributes.getName()))
                                                .append(entity.getName()).append(CLOSE_FUNCTION);
                                    })

                            )

            );

            stringBuilder.append("}");

            try (PrintWriter out = new PrintWriter(daoSrcDir.getPath()
                    + "/" + it.getName() + "DTO.java")) {
                out.println(stringBuilder.toString());
            } catch (FileNotFoundException e) {
                logger.log(Level.WARNING, e.getMessage());
            }

        });

    }

    private void writeDb2DaoFactory() throws IOException {

        File directoryDB2 = new File(daoSrcDir.getPath() + "/db2");
        if (!directoryDB2.mkdirs())
            throw new IOException("Directory db2 non creata");

        StringBuilder stringBuilderImport = new StringBuilder();
        StringBuilder stringBuilderOverride = new StringBuilder();

        entityList.forEach(entity -> {
            stringBuilderImport.append("import dao.").append(entity.getName()).append("DAO;\n");
            stringBuilderOverride.append("\t@Override\n\tpublic ").append(entity.getName())
            .append("DAO get").append(entity.getName())
                    .append("DAO() { return new Db2").append(entity.getName()).append("DAO(); }\n\n");
                }
        );

        ClassLoader classLoader = getClass().getClassLoader();
        File daoFactoryTemplateFile = new File(Objects.requireNonNull(
                classLoader.getResource("dao/db2-dao-factory-template.java")).getFile());

        String daoFactoryTemplate = new String(Files.readAllBytes(Paths.get(daoFactoryTemplateFile.getAbsolutePath())));
        daoFactoryTemplate = daoFactoryTemplate
                .replace(DTO_OVERRIDE_PLACEHOLDR, stringBuilderOverride.toString())
                .replace(IMPORT_DAO_PLACEHOLDER, stringBuilderImport.toString());

        try (PrintWriter out = new PrintWriter(daoSrcDir.getPath() + "/db2/Db2DAOFactory.java")) {
            out.println(daoFactoryTemplate);
        } catch (FileNotFoundException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public void writeDb2DaoEntity(){

        entityList.forEach(entity -> {
                StringBuilder stringBuilderImport = new StringBuilder();
                stringBuilderImport.append("import dao.").append(entity.getName()).append("DAO;\n");
                stringBuilderImport.append("import dao.").append(entity.getName()).append("DTO;\n");
                entity.getAssociations().forEach(association ->
                        stringBuilderImport.append("import dao.").append(association.getClassName()).append("DTO;\n"));


                //write class for entity
                StringBuilder body = new StringBuilder();

                body.append("public class Db2").append(entity.getName()).append("DAO implements ").append(entity.getName()).append("DAO {\n\n");

                }
        );
    }
}
