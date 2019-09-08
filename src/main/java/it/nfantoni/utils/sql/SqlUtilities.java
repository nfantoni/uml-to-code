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
}
