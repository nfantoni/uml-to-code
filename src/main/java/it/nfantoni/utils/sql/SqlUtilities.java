package it.nfantoni.utils.sql;

import java.util.HashMap;
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

        Optional<Map.Entry<String,String>> result = map.entrySet()
                .stream().filter(entry -> entry.getKey().contains(sqlType)).findFirst();

        return result.isPresent()?result.get().getValue():"";
    }
}
