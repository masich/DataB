package com.masich.datab.provider.datatype;

public final class InternalDataTypes {

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>BIT</code>.
     */
    public final static int BIT = 1;

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>TINYINT</code>.
     */
    public final static int TINYINT = 1 << 1;

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>SMALLINT</code>.
     */
    public final static int SMALLINT = 1 << 2;

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>INTEGER</code>.
     */
    public final static int INTEGER = 1 << 3;

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>BIGINT</code>.
     */
    public final static int BIGINT = 1 << 4;

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>FLOAT</code>.
     */
    public final static int FLOAT = 1 << 5;

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>REAL</code>.
     */
    public final static int REAL = 1 << 6;


    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>DOUBLE</code>.
     */
    public final static int DOUBLE = 1 << 7;

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>NUMERIC</code>.
     */
    public final static int NUMERIC = 1 << 8;

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>DECIMAL</code>.
     */
    public final static int DECIMAL = 1 << 9;

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>CHAR</code>.
     */
    public final static int CHAR = 1 << 10;

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>VARCHAR</code>.
     */
    public final static int VARCHAR = 1 << 11;

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>LONGVARCHAR</code>.
     */
    public final static int LONGVARCHAR = 1 << 12;


    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>DATE</code>.
     */
    public final static int DATE = 1 << 13;

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>TIME</code>.
     */
    public final static int TIME = 1 << 14;

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>TIMESTAMP</code>.
     */
    public final static int TIMESTAMP = 1 << 15;


    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>BINARY</code>.
     */
    public final static int BINARY = 1 << 16;

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>VARBINARY</code>.
     */
    public final static int VARBINARY = 1 << 17;

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>LONGVARBINARY</code>.
     */
    public final static int LONGVARBINARY = 1 << 18;

    /**
     * <P>The constant in the Java programming language
     * that identifies the generic SQL value
     * <code>NULL</code>.
     */
    public final static int NULL = 0;

    /**
     * The constant in the Java programming language that indicates
     * that the SQL type is database-specific and
     * gets mapped to a Java object that can be accessed via
     * the methods <code>getObject</code> and <code>setObject</code>.
     */
    public final static int OTHER = 1 << 19;

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * <code>BLOB</code>.
     */
    public final static int BLOB = 1 << 20;

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * <code>CLOB</code>.
     */
    public final static int CLOB = 1 << 21;

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type <code>BOOLEAN</code>.
     */
    public final static int BOOLEAN = 1 << 22;

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type <code>ROWID</code>
     */
    public final static int ROWID = 1 << 23;

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type <code>NCHAR</code>
     */
    public static final int NCHAR = 1 << 24;

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type <code>NVARCHAR</code>.
     */
    public static final int NVARCHAR = 1 << 25;

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type <code>LONGNVARCHAR</code>.
     */
    public static final int LONGNVARCHAR = 1 << 26;

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type <code>NCLOB</code>.
     */
    public static final int NCLOB = 1 << 27;

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type <code>XML</code>.
     */
    public static final int SQLXML = 1 << 28;

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type {@code REF CURSOR}.
     */
    public static final int REF_CURSOR = 1 << 29;

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * {@code TIME WITH TIMEZONE}.
     */
    public static final int TIME_WITH_TIMEZONE = 1 << 30;

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * {@code TIMESTAMP WITH TIMEZONE}.
     */
    public static final int TIMESTAMP_WITH_TIMEZONE = 1 << 31;

    public static final int ALL_TYPES = Integer.MAX_VALUE;

    public static int fromClassType(Class classType) {
        if (classType == boolean.class || classType == Boolean.class) {
            return BOOLEAN;
        } else if (classType == byte.class || classType == Byte.class) {
            return TINYINT;
        } else if (classType == short.class || classType == Short.class) {
            return SMALLINT;
        } else if (classType == int.class || classType == Integer.class) {
            return INTEGER;
        } else if (classType == long.class || classType == Long.class) {
            return BIGINT;
        } else if (classType == float.class || classType == Float.class) {
            return REAL;
        } else if (classType == double.class || classType == Double.class) {
            return DOUBLE;
        } else if (classType == byte[].class) {
            return BLOB;
        } else if (classType == java.sql.Date.class) {
            return DATE;
        } else if (classType == java.sql.Time.class) {
            return TIME;
        } else if (classType == java.sql.Timestamp.class) {
            return TIMESTAMP;
        } else if (classType == java.math.BigDecimal.class) {
            return DECIMAL;
        }
        return VARCHAR;
    }

    private InternalDataTypes() {
    }
}
