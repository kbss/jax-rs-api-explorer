package org.forsp.jax.rs.explorer.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Serhii Kryvtsov
 * @since 1.0
 */
public class JaxRsMethod {
    private String methodName;
    private String httpMethod;
    private String path;
    private Collection<JaxRsPathParam> pathParams;
    private Collection<JaxRsParam> queryParams;
    private Collection<JaxRsParam> headerParams;
    private JaxRsObject consumeObject;
    private JaxRsObject produceObject;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "JaxRsMethod{" +
                "methodName='" + methodName + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                ", path='" + path + '\'' +
                ", pathParams=" + pathParams +
                ", queryParams=" + queryParams +
                ", headerParams=" + headerParams +
                ", consumeObject=" + consumeObject +
                ", produceObject=" + produceObject +
                '}';
    }

    public Collection<JaxRsParam> getHeaderParams() {
        if (headerParams == null) {
            headerParams = new ArrayList<JaxRsParam>();
        }
        return headerParams;
    }

    public void setHeaderParams(Collection<JaxRsParam> headerParams) {
        this.headerParams = headerParams;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Collection<JaxRsPathParam> getPathParams() {
        if (pathParams == null) {
            pathParams = new ArrayList<JaxRsPathParam>();
        }
        return pathParams;
    }

    public void setPathParams(Collection<JaxRsPathParam> pathParams) {
        this.pathParams = pathParams;
    }

    public Collection<JaxRsParam> getQueryParams() {
        if (queryParams == null) {
            queryParams = new ArrayList<JaxRsParam>();
        }
        return queryParams;
    }

    public void setQueryParams(Collection<JaxRsParam> queryParams) {
        this.queryParams = queryParams;
    }

    public JaxRsObject getConsumeObject() {
        return consumeObject;
    }

    public void setConsumeObject(JaxRsObject consumeObject) {
        this.consumeObject = consumeObject;
    }

    public JaxRsObject getProduceObject() {
        return produceObject;
    }

    public void setProduceObject(JaxRsObject produceObject) {
        this.produceObject = produceObject;
    }
}
