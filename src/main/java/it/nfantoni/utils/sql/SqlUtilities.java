package it.nfantoni.utils.sql;

import it.nfantoni.utils.entities.Attributes;
import it.nfantoni.utils.entities.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SqlUtilities {

    private SqlUtilities() {
    }

    public static String javaType(String sqlType){

        HashMap<String,String> map = new HashMap<>();
        map.put("VARCHAR", "String");
        map.put("DATE", "Date");
        map.put("INT", "int");
        map.put("INTEGER", "int");
        map.put("BIGINT", "long");
        map.put("FLOAT", "double");

        Optional<Map.Entry<String,String>> result = map.entrySet()
                .stream().filter(entry -> sqlType.contains(entry.getKey())).findFirst();

        return result.isPresent()?result.get().getValue():"";
    }

    public static String sqlCreate(Entity entity, List<Entity> entityList){
        StringBuilder result = new StringBuilder();

        result.append("CREATE TABLE ").append(entity.getName().toUpperCase()).append(" ( ");

        entity.getAttributes().forEach(attributes ->
                result.append(attributes.getName().toUpperCase()).append(" ")
                .append(attributes.getSqlType()).append(" ")
                        .append(!attributes.getNull()?"NOT NULL ":"")
                        .append(attributes.getPrimaryKey()?"PRIMARY KEY , ":", ")
        );

        entity.getAssociations().stream().filter(item -> item.getMultiplicity().equals("*")).forEach(ass ->
                entityList.stream().filter(ent -> ent.getName().equals(ass.getClassName()))
                        .forEach(it -> it.getAttributes().stream()
                                .filter(Attributes::getPrimaryKey).forEach(attributes ->
                                    result.append(attributes.getName().toUpperCase()).append(it.getName().toUpperCase())
                                            .append(" ").append(attributes.getSqlType()).append( " NOT NULL REFERENCES ")
                                            .append(it.getName().toUpperCase()).append("(")
                                            .append(attributes.getName().toUpperCase()).append("), ")
                                )

                        )

        );

        return result.toString().substring(0,result.toString().length()-2) + ")";
    }

    public static String sqlDrop(Entity entity){
        return "DROP TABLE " + entity.getName().toUpperCase();
    }

    public static String sqlInsert(Entity entity, List<Entity> entityList){
        StringBuilder result = new StringBuilder();
        StringBuilder value = new StringBuilder();

        result.append("INSERT INTO ").append(entity.getName().toUpperCase()).append(" (");

        entity.getAttributes().forEach(attributes ->{
                    result.append(attributes.getName().toUpperCase()).append(" , ");
                    value.append("?,");
                }

        );

        entity.getAssociations().stream().filter(item -> item.getMultiplicity().equals("*")).forEach(ass ->
                entityList.stream().filter(ent -> ent.getName().equals(ass.getClassName()))
                        .forEach(it -> it.getAttributes().stream()
                                .filter(Attributes::getPrimaryKey).forEach(attributes -> {
                                        result.append(attributes.getName().toUpperCase()).append(it.getName().toUpperCase()).append(" , ");
                                        value.append("?,");
                                }
                                )
                        )
        );

        return result.toString().substring(0,result.toString().length()-2) +
                ") VALUES (" + value.toString().substring(0, value.toString().length()-1) + ")";
    }

    public static String sqlDelete(Entity entity){

        StringBuilder result = new StringBuilder();

        result.append("DELETE FROM ");
        result.append(entity.getName().toUpperCase());
        result.append(" WHERE ");
        result.append(
                entity.getAttributes().stream()
                        .filter(Attributes::getPrimaryKey)
                        .findFirst().map(Attributes::getName)
                        .orElse("<key not found>").toUpperCase());
        result.append(" = ?");


        return result.toString();
    }

    public static String sqlReadById(Entity entity){

        StringBuilder result = new StringBuilder();

        result.append("SELECT * FROM ");
        result.append(entity.getName().toUpperCase());
        result.append(" WHERE ");
        result.append(
                entity.getAttributes().stream()
                        .filter(Attributes::getPrimaryKey)
                        .findFirst().map(Attributes::getName)
                        .orElse("<key not found>").toUpperCase());
        result.append(" = ?");


        return result.toString();
    }

    public static String sqlRead(Entity entity){

        return "SELECT * FROM " + entity.getName().toUpperCase();
    }

    public static String sqlUpdate(Entity entity, List<Entity> entityList){
        StringBuilder result = new StringBuilder();

        result.append("UPDATE ").append(entity.getName().toUpperCase()).append(" SET ");

        entity.getAttributes().forEach(attributes ->
                    result.append(attributes.getName().toUpperCase()).append(" = ?, ")


        );

        entity.getAssociations().stream().filter(item -> item.getMultiplicity().equals("*")).forEach(ass ->
                entityList.stream().filter(ent -> ent.getName().equals(ass.getClassName()))
                        .forEach(it -> it.getAttributes().stream()
                                .filter(Attributes::getPrimaryKey).forEach(attributes ->
                                            result.append(attributes.getName().toUpperCase()).
                                                    append(it.getName().toUpperCase()).append(" = ?, ")


                                )
                        )
        );

        return result.toString().substring(0,result.toString().length()-2) +
                " WHERE " + entity.getAttributes().stream()
                .filter(Attributes::getPrimaryKey)
                .findFirst().map(Attributes::getName)
                .orElse("<key not found>").toUpperCase() + " = ?";
    }
}
