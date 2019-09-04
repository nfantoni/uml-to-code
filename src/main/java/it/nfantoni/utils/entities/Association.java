package it.nfantoni.utils.entities;

public class Association {
    private String className;

    public Association(String className, String multiplicity) {
        this.className = className;
        this.multiplicity = multiplicity;
    }

    private String multiplicity;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(String multiplicity) {
        this.multiplicity = multiplicity;
    }
}
