package org.forsp.jax.rs.explorer;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.forsp.jax.rs.explorer.model.JaxRsClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;


/**
 * @author Serhii Kryvtsov
 * @since 1.0
 */
public class JaxRsApiExplorerServlet extends HttpServlet {

    public static final String SERVICE_PATH = "/services/analyze";
    private static final Logger LOGGER = LoggerFactory.getLogger(JaxRsApiExplorerServlet.class);
    private String scanPackage;
    private String servicePath;
    private String[] classes;
    private JaxRSReader reader;
    private String result;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        this.scanPackage = servletConfig.getInitParameter("scanPackage");
        String classesParam = servletConfig.getInitParameter("classes");
        if (classesParam != null) {
            classes = classesParam.split(",");
        }
        servicePath = servletConfig.getInitParameter("servicePath");
        init();
    }

    @Override
    public void init() throws ServletException {
        reader = new JaxRSReader();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String path = request.getRequestURI().replace(request.getServletPath(), "").trim();
        boolean redirectToRoot = request.getRequestURI().equals(request.getServletPath());
        if (path.equals(SERVICE_PATH)) {
            if (result == null) {
                Collection<JaxRsClass> jaxRsClasses = new ArrayList<JaxRsClass>();
                Gson gson = new Gson();
                if (classes != null) {
                    readStringClasses(jaxRsClasses);
                }
                result = gson.toJson(jaxRsClasses);
            }
            out.println(result);
            return;
        }
        if (redirectToRoot) {
            String redirectUrl = request.getServletPath() + "/";
            LOGGER.debug("Redirecting to servlet root");

            response.sendRedirect(redirectUrl);
            return;
        }

        boolean redirectToIndex = path.equals("/") || path.isEmpty() || !path.contains(".");
        if (redirectToIndex) {
            String content = getContent("/index.html");
            if (content == null) {
                response.sendError(404);
                return;
            }
            out.println(content);
            return;
        }
        String content = getContent(path);
        if (content == null) {
            response.sendError(404);
            return;
        }
        out.println(content);
    }

    private String getContent(String url) throws IOException {
        StringWriter sw = new StringWriter();
        InputStream input = getClass().getResourceAsStream(url);
        if (input == null) {
            LOGGER.warn("Resource not found: {}", url);
            return null;
        }
        IOUtils.copy(input, sw);
        IOUtils.closeQuietly(sw);
        IOUtils.closeQuietly(input);
        return sw.toString();
    }

    private void readStringClasses(Collection<JaxRsClass> jaxRsClasses) {
        for (String clazz : this.classes) {
            clazz = clazz.trim();
            try {
                Class<?> cls = Class.forName(clazz);
                if (cls == null) {
                    LOGGER.warn("Can't find class for name: {}", clazz);
                }
                inspectClass(jaxRsClasses, cls);
            } catch (ClassNotFoundException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private void inspectClass(Collection<JaxRsClass> jaxRsClasses, Class<?> cls) {
        JaxRsClass result = reader.inspectClass(cls);
        if (result != null) {
            jaxRsClasses.add(result);
        }
    }

    @Override
    public void destroy() {
        //DO NOTHING.
    }
}