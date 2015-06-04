package org.forsp.jax.rs.explorer.model;

/**
 * @author Serhii Kryvtsov
 * @since 1.0
 */
public class JaxRsObject {
    private String className;
    private boolean isPrimitive;
    private boolean isVoid;
    private boolean isEnum;
    private boolean isArray;
    private String[] enumValues;

    public String[] getEnumValues() {
        return enumValues;
    }

    public void setEnumValues(String[] enumValues) {
        this.enumValues = enumValues;
    }

    @Override
    public String toString() {
        return "JaxRsObject{" +
                "className='" + className + '\'' +
                ", isPrimitive=" + isPrimitive +
                ", isVoid=" + isVoid +
                ", isEnum=" + isEnum +
                ", isArray=" + isArray +
                '}';
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }

    public void setPrimitive(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    public boolean isVoid() {
        return isVoid;
    }

    public void setVoid(boolean isVoid) {
        this.isVoid = isVoid;
    }

    public boolean isEnum() {
        return isEnum;
    }

    public void setEnum(boolean isEnum) {
        this.isEnum = isEnum;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean isArray) {
        this.isArray = isArray;
    }
}
