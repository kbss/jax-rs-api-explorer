package org.forsp.jax.rs.explorer.model;

/**
 * @author Serhii Kryvtsov
 * @since 1.0
 */
public class JaxRsPathParam extends JaxRsParam {
    private String pattern;

    @Override
    public String toString() {
        return "JaxRsPathParam{" +
                "pattern='" + pattern + '\'' +
                "} " + super.toString();
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
