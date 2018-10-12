package com.everhomes.controller;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@WebFilter(filterName = "xssFilter", urlPatterns = "/*", asyncSupported = true)
public class XssFilter extends GenericFilterBean {

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

        if (!xssExcludeUriSet.contains(uri)) {
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
                return XssCleaner.clean(super.getParameter(name));
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
