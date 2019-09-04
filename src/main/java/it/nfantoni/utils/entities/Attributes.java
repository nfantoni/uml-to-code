package it.nfantoni.utils.entities;

public class Attributes {

    private String name;
    private String sqlType;
    private Boolean isNull;
    private Boolean isPrimaryKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public Boolean getNull() {
        return isNull;
    }

    public void setNull(Boolean aNull) {
        isNull = aNull;
    }

    public Boolean getPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public Attributes(String name, String sqlType, Boolean isNull, Boolean isPrimaryKey) {
        this.name = name;
        this.sqlType = sqlType;
        this.isNull = isNull;
        this.isPrimaryKey = isPrimaryKey;
    }
}