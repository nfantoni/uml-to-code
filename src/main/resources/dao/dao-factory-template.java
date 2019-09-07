package dao;

import dao.db2.Db2DAOFactory;

public abstract class DAOFactory {

    // --- List of supported DAO types ---

    /**
     * Numeric constant '0' corresponds to explicit DB2 choice
     */
    public static final int DB2 = 0;


    // --- Actual factory method ---

    /**
     * Depending on the input parameter
     * this method returns one out of several possible
     * implementations of this factory spec
     */
    public static DAOFactory getDAOFactory(int whichFactory) {
        switch ( whichFactory ) {
            case DB2:
                return new Db2DAOFactory();
            default:
                return null;
        }
    }

    // --- Factory specification: concrete factories implementing this spec must provide these methods! ---
{$abstract-dao}
}