package com.everhomes.controller;

import com.google.gson.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@WebFilter(filterName = "xssFilter", urlPatterns = "/*")
public class XssFilter extends GenericFilterBean {

    private static final Gson gson;
    static {
        gson = new GsonBuilder()
                .disableHtmlEscaping()
                .create();
    }

    @Value("${xss.enabled:false}")
    private boolean xssEnabled;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final Set<String> xssExcludeUriSet = ControllerBase.getRestMethodList("", "")
                .stream()
                .map(r -> (ExtendRestMethod) r)
                .filter(r -> r.getXssExclude() != null)
                .map(ExtendRestMethod::getUri)
                .collect(Collectors.toSet());

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String contextPath = httpServletRequest.getContextPath();
        String requestURI = httpServletRequest.getRequestURI();
        String uri = requestURI.replaceFirst(contextPath, "");

        if (xssEnabled && !xssExcludeUriSet.contains(uri)) {
            chain.doFilter(xssHttpServletRequestWrapper(httpServletRequest), response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private HttpServletRequestWrapper xssHttpServletRequestWrapper(HttpServletRequest request) {
        return new HttpServletRequestWrapper(request) {
            @Override
            public String getHeader(String name) {
                return XssCleaner.clean(super.getHeader(name));
            }

            @Override
            public String getParameter(String name) {
                String value = super.getParameter(name);
                try {
                    // json 里面的数据需要单独的过滤
                    return jsonFilter(value);
                } catch(JsonSyntaxException ex) {
                    return XssCleaner.clean(value);
                }
            }

            private String jsonFilter(String value) {
                final JsonElement jsonElement = gson.fromJson(value, JsonElement.class);
                if (jsonElement.isJsonObject()) {
                    final JsonObject jsonObject = jsonElement.getAsJsonObject();
                    final Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                    final Map<Object, Object> newMap = new HashMap<>(entrySet.size());
                    for (Map.Entry<String, JsonElement> entry : entrySet) {
                        if (entry.getValue() != null) {
                            newMap.put(entry.getKey(), XssCleaner.clean(entry.getValue().toString()));
                        }
                    }
                    return gson.toJson(newMap);
                } else if (jsonElement.isJsonArray()) {
                    final JsonArray jsonArray = jsonElement.getAsJsonArray();
                    final List<Object> newArray = new ArrayList<>(jsonArray.size());
                    for (JsonElement element : jsonArray) {
                        newArray.add(XssCleaner.clean(element.toString()));
                    }
                    return gson.toJson(newArray);
                }
                return XssCleaner.clean(value);
            }

            @Override
            public String getQueryString() {
                return XssCleaner.clean(super.getQueryString());
            }

            @Override
            public Map<String, String[]> getParameterMap() {
                Map<String, String[]> parameterMap = new HashMap<>();
                super.getParameterMap().forEach((k, v) -> {
                    for (int i = 0; i < v.length; i++) {
                        v[i] = XssCleaner.clean(v[i]);
                    }
                    parameterMap.put(k, v);
                });
                return parameterMap;
            }

            @Override
            public String[] getParameterValues(String name) {
                String[] values = super.getParameterValues(name);
                if (values != null) {
                    int length = values.length;
                    String[] escapseValues = new String[length];
                    for (int i = 0; i < length; i++) {
                        escapseValues[i] = XssCleaner.clean(values[i]);
                    }
                    return escapseValues;
                }
                return super.getParameterValues(name);
            }
        };
    }

    @Override
    public void destroy() {

    }
}
