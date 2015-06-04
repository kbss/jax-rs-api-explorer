package org.forsp.jax.rs.explorer;

import com.google.gson.Gson;
import org.forsp.jax.rs.explorer.model.JaxRsClass;
import org.forsp.jax.rs.explorer.model.JaxRsMethod;
import org.forsp.jax.rs.explorer.model.JaxRsObject;
import org.forsp.jax.rs.explorer.model.JaxRsParam;
import org.forsp.jax.rs.explorer.model.JaxRsPathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Serhii Kryvtsov
 * @since 1.0
 */
public class JaxRSReader {

    public static final String REGEXP_PATTERN = "\\{(.*?)\\}";
    public static final Pattern PATH_PARAM_PATTERN = Pattern.compile(REGEXP_PATTERN);
    public static final String PATH_PARAM_DELIMITER = ":";
    private static final Logger LOGGER = LoggerFactory.getLogger(JaxRSReader.class);

    public static void main(String[] args) {
        new JaxRSReader().inspectClass(JaxRsExample.class);
    }

    private JaxRsClass readClassAnnotations(Class<?> clazz) {
        JaxRsClass jaxRsClass = new JaxRsClass();
        for (Annotation annotation : clazz.getAnnotations()) {
            if (annotation instanceof Path) {
                Path path = (Path) annotation;
                jaxRsClass.setPath(path.value());
                LOGGER.trace("Class root path: {}", jaxRsClass.getPath());
            }
        }
        jaxRsClass.setName(clazz.getSimpleName());
        return jaxRsClass;
    }

    public JaxRsClass inspectClass(Class<?> clazz) {
        if (clazz == null) {
            LOGGER.warn("Null class not allowed");
            return null;
        }
        if (clazz.getMethods().length < 1) {
            return null;
        }
        LOGGER.debug("Analyzing class: {}", clazz.getName());
        JaxRsClass jaxRsClass = readClassAnnotations(clazz);
        Collection<JaxRsMethod> jaxRsMethods = new ArrayList<JaxRsMethod>(clazz.getMethods().length);
        jaxRsClass.setMethods(jaxRsMethods);
        for (Method method : clazz.getMethods()) {
            JaxRsMethod jaxRsMethod = new JaxRsMethod();
            String methodName = method.getName();
            LOGGER.trace("Method name: {}", methodName);
            Annotation[] methodAnnotations = method.getAnnotations();
            for (Annotation annotation : methodAnnotations) {
                if (isHttpMethod(annotation)) {
                    jaxRsMethod.setHttpMethod(annotation.annotationType().getSimpleName());
                    LOGGER.trace("Type: {}", jaxRsMethod.getHttpMethod());
                    jaxRsMethod.setHttpMethod(jaxRsMethod.getHttpMethod());
                }
                if (annotation instanceof Path) {
                    Path path = (Path) annotation;
                    String methodPath = path.value();
                    LOGGER.trace("Method path: {}", methodPath);
                    Collection<JaxRsPathParam> pathParams = jaxRsMethod.getPathParams();
                    Matcher matcher = PATH_PARAM_PATTERN.matcher(path.value());
                    int order = 0;
                    while (matcher.find()) {
                        String pathParam = matcher.group(1);
                        LOGGER.trace("Path param: {}", matcher.group(1));
                        JaxRsPathParam param = new JaxRsPathParam();
                        if (pathParam != null) {
                            //if path param contains regexp
                            if (pathParam.contains(PATH_PARAM_DELIMITER)) {
                                String pathParts[] = pathParam.split(PATH_PARAM_DELIMITER);
                                param.setName(pathParts[0].trim());
                                param.setPattern(pathParts[1].trim());
                            } else {
                                param.setName(pathParam.trim());
                            }
                            methodPath = methodPath.replaceFirst(Pattern.quote(String.format("{%s}", pathParam)), String.format("\\$\\{%s\\}", ++order));
                            pathParams.add(param);
                        }
                    }
                    jaxRsMethod.setPath(methodPath);
                    jaxRsMethod.setMethodName(methodName);
                }
            }
            processParams(method, jaxRsMethod);
            setProduceObject(method, jaxRsMethod);

            if (jaxRsMethod.getHttpMethod() != null) {
                jaxRsMethods.add(jaxRsMethod);
            }
        }
        LOGGER.debug("{}", jaxRsClass);
        Gson gson = new Gson();
        System.out.println(gson.toJson(jaxRsClass));
        return jaxRsClass;
    }

    private void setProduceObject(Method method, JaxRsMethod jaxRsMethod) {
        Class<?> returnType = method.getReturnType();
        JaxRsObject returnObject = new JaxRsObject();
        jaxRsMethod.setProduceObject(returnObject);
        returnObject.setVoid(returnType == Void.TYPE);
        returnObject.setArray(returnType.isArray());
        returnObject.setEnum(returnType.isEnum());
        returnObject.setPrimitive(returnType.isPrimitive());
        //Collections represented in JSON as array.
        if (Collection.class.isAssignableFrom(returnType)) {
            returnObject.setArray(true);
        }

    }

    /**
     * Returns true if given annotation is JAX-RS @HttpMethod.
     *
     * @param annotation
     * @return false if given annotation is not @HttpMethod.
     */
    private boolean isHttpMethod(Annotation annotation) {
        return annotation instanceof GET
                || annotation instanceof POST
                || annotation instanceof PUT
                || annotation instanceof DELETE
                || annotation instanceof HEAD
                || annotation instanceof OPTIONS;
    }

    private void processParams(Method method, JaxRsMethod jaxRsMethod) {
        for (Annotation[] ans : method.getParameterAnnotations()) {
            for (Annotation annotation : ans) {
//                    if (annotation instanceof PathParam) {
//                        PathParam pathParam = (PathParam) annotation;
//                        LOGGER.trace("Path param: {}", pathParam.value());
//                    }
                if (annotation instanceof QueryParam) {
                    QueryParam pathParam = (QueryParam) annotation;
                    JaxRsParam queryParam = new JaxRsParam();
                    queryParam.setName(pathParam.value());
                    jaxRsMethod.getQueryParams().add(queryParam);
                    LOGGER.trace("Query param: {}", queryParam.getName());
                }
                if (annotation instanceof HeaderParam) {
                    HeaderParam pathParam = (HeaderParam) annotation;
                    JaxRsParam headerParam = new JaxRsParam();
                    headerParam.setName(pathParam.value());
                    jaxRsMethod.getHeaderParams().add(headerParam);
                    LOGGER.trace("Query param: {}", headerParam.getName());
                    LOGGER.trace("Head param: {}", pathParam.value());
                }
            }
        }
    }


}
