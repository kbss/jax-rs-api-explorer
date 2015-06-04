package org.forsp.jax.rs.explorer.model;

import java.util.Collection;

/**
 * @author Serhii Kryvtsov
 * @since 1.0
 */
public class JaxRsClass {
    private String name;
    private String path;
    private Collection<JaxRsMethod> methods;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {

        return "JaxRsClass{" +
                "methods=" + methods +
                '}';
    }

    public Collection<JaxRsMethod> getMethods() {
        return methods;
    }

    public void setMethods(Collection<JaxRsMethod> methods) {
        this.methods = methods;
    }
}
