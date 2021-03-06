package org.forsp.jax.rs.explorer.model;

/**
 * @author Serhii Kryvtsov
 * @since 1.0
 */
public class JaxRsParam {
    private String name;
    private String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "JaxRsParam{" +
                "name='" + name + '\'' +
                '}';
    }
}
