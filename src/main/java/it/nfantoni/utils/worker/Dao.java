package it.nfantoni.utils.worker;

import it.nfantoni.utils.entities.Association;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dao implements Worker {

    private static Logger logger = Logger.getLogger("DAO");

    private static final String ABSTRACT_DAO_PLACEHOLDER = "{$abstract-dao}";
    private static final String SRC_DIRECTORY = "src/dao";
    private static final String CLOSE_METHODS = ");" + System.lineSeparator() + System.lineSeparator();
    private static final String PUBLIC = "    public ";
    private static final String CLOSE_FUNCTION = "; }" + System.lineSeparator() + System.lineSeparator();
    private static final String DTO_OVERRIDE_PLACEHOLDR = "{$dto-override}";
    private static final String IMPORT_DAO_PLACEHOLDER = "{$import-dao}";
    private static final String IMPORT_DAO = "import dao.";
    private static final String TAB = "    ";
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
        writeDb2DaoEntity();
    }

    private void writeDaoFactory() throws IOException {

        daoSrcDir = new File(String.format("%s/%s", settings.getOutputPath(), SRC_DIRECTORY));
        if (!daoSrcDir.mkdirs())
            throw new IOException("Directory non creata");

        StringBuilder stringBuilder = new StringBuilder();

        entityList.forEach(it -> stringBuilder.append("    public abstract ")
                .append(it.getName()).append("DAO get").append(it.getName()).append("DAO();").append(System.lineSeparator()));

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

            stringBuilder.append("package dao;").append(System.lineSeparator()).append(System.lineSeparator());
            stringBuilder.append("public interface ").append(it.getName()).append("DAO {").append(System.lineSeparator()).append(System.lineSeparator());
            stringBuilder.append("    // --- CRUD -------------").append(System.lineSeparator()).append(System.lineSeparator());
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

            stringBuilder.append("    // ----------------------------------").append(System.lineSeparator()).append(System.lineSeparator());

            stringBuilder.append("    public boolean createTable();").append(System.lineSeparator()).append(System.lineSeparator());

            stringBuilder.append("    public boolean dropTable();}").append(System.lineSeparator());

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
            stringBuilder.append("package dao;").append(System.lineSeparator()).append(System.lineSeparator());

            //check if present sqltype DATE
            if (it.getAttributes().stream().anyMatch(att -> att.getSqlType().contains("DATE")))
                stringBuilder.append("import java.util.Date;").append(System.lineSeparator()).append(System.lineSeparator());

            stringBuilder.append("public class ").append(it.getName()).append("DTO {").append(System.lineSeparator()).append(System.lineSeparator());

            it.getAttributes().forEach(attributes -> stringBuilder.append("    private ")
                    .append(SqlUtilities.javaType(attributes.getSqlType())).append(" ")
                    .append(StringUtils.decapitalize(attributes.getName())).append(";").append(System.lineSeparator()));

            it.getAssociations().stream().filter(item -> item.getMultiplicity().equals("*")).forEach(ass ->
                    entityList.stream().filter(ent -> ent.getName().equals(ass.getClassName()))
                            .forEach(entity -> entity.getAttributes().stream()
                                    .filter(Attributes::getPrimaryKey).forEach(attributes ->
                                            stringBuilder.append("    private ")
                                                    .append(SqlUtilities.javaType(attributes.getSqlType())).append(" ")
                                                    .append(StringUtils.decapitalize(attributes.getName()))
                                                    .append(entity.getName()).append(";").append(System.lineSeparator())
                                    )

                            )

            );

            stringBuilder.append(System.lineSeparator())
                    .append(PUBLIC).append(it.getName()).append("DTO() {" + System.lineSeparator()
                    + System.lineSeparator() +
                    "    }").append(System.lineSeparator()).append(System.lineSeparator());

            it.getAttributes().forEach(attributes -> {
                stringBuilder.append(PUBLIC)
                        .append(SqlUtilities.javaType(attributes.getSqlType()))
                        .append(" get")
                        .append(attributes.getName())
                        .append("() { return ").append(StringUtils.decapitalize(attributes.getName()))
                        .append(CLOSE_FUNCTION);

                stringBuilder.append("    public void ")
                        .append(" set")
                        .append(attributes.getName()).append("(")
                        .append(SqlUtilities.javaType(attributes.getSqlType()))
                        .append(" ").append(StringUtils.decapitalize(attributes.getName())).append(") { this.")
                        .append(StringUtils.decapitalize(attributes.getName())).append(" = ")
                        .append(StringUtils.decapitalize(attributes.getName()))
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

                                        stringBuilder.append("    public void set").append(attributes.getName()).append(entity.getName())
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
                    stringBuilderImport.append(IMPORT_DAO).append(entity.getName()).append("DAO;").append(System.lineSeparator());
                    stringBuilderOverride.append("    @Override").append(System.lineSeparator()).append("    public ").append(entity.getName())
                            .append("DAO get").append(entity.getName())
                            .append("DAO() { return new Db2").append(entity.getName()).append("DAO(); }")
                            .append(System.lineSeparator()).append(System.lineSeparator());
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

    public void writeDb2DaoEntity() throws IOException {

        ClassLoader classLoader = getClass().getClassLoader();
        File daoFactoryTemplateFile = new File(Objects.requireNonNull(
                classLoader.getResource("dao/db2-dao-template.java")).getFile());
        String db2DaoTemplate = new String(Files.readAllBytes(Paths.get(daoFactoryTemplateFile.getAbsolutePath())));

        entityList.forEach(entity -> {

                    String entityTemplate = db2DaoTemplate;

                    StringBuilder preparedUpdate = new StringBuilder();
                    StringBuilder readByIdRes = new StringBuilder();
                    StringBuilder preparedCreate = new StringBuilder();

                    StringBuilder stringBuilderImport = new StringBuilder();
                    stringBuilderImport.append(IMPORT_DAO).append(entity.getName()).append("DAO;").append(System.lineSeparator());
                    stringBuilderImport.append(IMPORT_DAO).append(entity.getName()).append("DTO;").append(System.lineSeparator());
                    entity.getAssociations().forEach(association ->
                            stringBuilderImport.append(IMPORT_DAO).append(association.getClassName())
                                    .append("DTO;").append(System.lineSeparator()));

                    String entityKeyType = entity.getAttributes().stream()
                            .filter(Attributes::getPrimaryKey).findFirst()
                            .map(it -> SqlUtilities.javaType(it.getSqlType())).orElse("type not found");

                    String entityKey = StringUtils.decapitalize(entity.getAttributes().stream()
                            .filter(Attributes::getPrimaryKey).findFirst()
                            .map(Attributes::getName).orElse("type not found"));

                    final AtomicInteger index = new AtomicInteger(1);

                    entityTemplate = entityTemplate.replace("{$import-dao-class}", stringBuilderImport.toString())
                            .replace("{$entity-name}", entity.getName())
                            .replace("{$entity-name-lower}", StringUtils.decapitalize(entity.getName()))
                            .replace("{$entity-key}", entityKey).replace("{$entity-key-type}", entityKeyType);

                    StringBuilder initializer = new StringBuilder();

                    initializer.append(TAB).append(TAB).append("// Definisco nome tabella").append(System.lineSeparator()).append(System.lineSeparator())
                            .append(TAB).append(TAB).append("static final String TABLE = \"").append(entity.getName().toUpperCase()).append("\";")
                            .append(System.lineSeparator()).append(System.lineSeparator())
                            .append(TAB).append(TAB).append("// Definisco parametri tabella")
                            .append(System.lineSeparator()).append(System.lineSeparator());

                    entity.getAttributes().forEach(attributes -> {
                        initializer.append(TAB).append(TAB).append("static final String ")
                                .append(attributes.getName().toUpperCase())
                                .append(" = \"")
                                .append(attributes.getName().toUpperCase()).append("\";")
                                .append(System.lineSeparator());
                        preparedUpdate.append(TAB).append(TAB).append("prep_stmt.set")
                                .append(SqlUtilities.javaType(attributes.getSqlType()))
                                .append("(").append(index).append(", ")
                                .append(StringUtils.decapitalize(entity.getName())).append(".get")
                                .append(attributes.getName()).append("());").append(System.lineSeparator());
                        preparedCreate.append(TAB).append(TAB).append(TAB).append(TAB).append("prep_stmt.set")
                                .append(SqlUtilities.javaType(attributes.getSqlType()))
                                .append("(").append(index).append(", ")
                                .append(StringUtils.decapitalize(entity.getName())).append(".get")
                                .append(attributes.getName()).append("());").append(System.lineSeparator());
                        index.getAndAdd(1);
                        readByIdRes.append(TAB).append(TAB).append("entry.set").append(attributes.getName())
                                .append("(rs.get").append(SqlUtilities.javaType(attributes.getSqlType()))
                                .append("(").append(attributes.getName().toUpperCase()).append("));").append(System.lineSeparator());

                    });
                    entity.getAssociations().stream()
                            .filter(association -> association.getMultiplicity().equals("*"))
                            .forEach(association ->
                                    entityList.stream().filter(ent -> ent.getName().equals(association.getClassName()))
                                            .forEach(ent -> ent.getAttributes().stream().filter(Attributes::getPrimaryKey)
                                                    .forEach(attributes -> {
                                                                initializer.append(TAB).append(TAB).append("static final String ")
                                                                        .append(attributes.getName().toUpperCase())
                                                                        .append(ent.getName().toUpperCase())
                                                                        .append(" = \"")
                                                                        .append(attributes.getName().toUpperCase())
                                                                        .append(ent.getName().toUpperCase()).append("\";")
                                                                        .append(System.lineSeparator());
                                                                preparedUpdate.append(TAB).append(TAB).append("prep_stmt.set")
                                                                        .append(SqlUtilities.javaType(attributes.getSqlType()))
                                                                        .append("(").append(index).append(", ")
                                                                        .append(StringUtils.decapitalize(entity.getName())).append(".get")
                                                                        .append(attributes.getName()).append("());").append(System.lineSeparator());
                                                                preparedCreate.append(TAB).append(TAB).append(TAB).append(TAB).append("prep_stmt.set")
                                                                        .append(SqlUtilities.javaType(attributes.getSqlType()))
                                                                        .append("(").append(index).append(", ")
                                                                        .append(StringUtils.decapitalize(entity.getName())).append(".get")
                                                                        .append(attributes.getName()).append(ent.getName()).append("());")
                                                                        .append(System.lineSeparator());
                                                                index.getAndAdd(1);
                                                            }

                                                    ))
                            );


                    preparedUpdate.append(TAB).append(TAB).append("prep_stmt.set").append(entityKeyType).append("(").append(index).append(", ")
                            .append(StringUtils.decapitalize(entity.getName())).append(".get")
                            .append(entityKey).append("());").append(System.lineSeparator());

                    entityTemplate = entityTemplate.replace("{$initializer}", initializer.toString());
                    entityTemplate = entityTemplate.replace("{$prepared-readbyid}", readByIdRes);
                    entityTemplate = entityTemplate.replace("{$prepared-create}", preparedCreate.toString());

                    entityTemplate = entityTemplate.replace("{$sql-create}", SqlUtilities.sqlCreate(entity, entityList));
                    entityTemplate = entityTemplate.replace("{$sql-drop}", SqlUtilities.sqlDrop(entity));
                    entityTemplate = entityTemplate.replace("{$sql-insert}", SqlUtilities.sqlInsert(entity, entityList));
                    entityTemplate = entityTemplate.replace("{$sql-delete}", SqlUtilities.sqlDelete(entity));
                    entityTemplate = entityTemplate.replace("{$sql-read-by-id}", SqlUtilities.sqlReadById(entity));
                    entityTemplate = entityTemplate.replace("{$sql-read}", SqlUtilities.sqlRead(entity));
                    entityTemplate = entityTemplate.replace("{$sql-update}", SqlUtilities.sqlUpdate(entity, entityList));
                    entityTemplate = entityTemplate.replace("{$prepared-update}", preparedUpdate.toString());

                    try (PrintWriter out = new PrintWriter(daoSrcDir.getPath() + "/db2/Db2" + entity.getName() + "DAO.java")) {
                        out.println(entityTemplate);
                    } catch (FileNotFoundException e) {
                        logger.log(Level.WARNING, e.getMessage());
                    }
                }

        );
    }
}
