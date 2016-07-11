package com.everhomes.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.io.output.TeeOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class ResponseLoggingFilter extends GenericFilterBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletResponse responseWrapper = loggingResponseWrapper((HttpServletResponse) response);
            filterChain.doFilter(request, responseWrapper);
        } catch(Throwable e) {
            LOGGER.error("Unexpected exception", e);
            throw e;
        }
    }

    private HttpServletResponse loggingResponseWrapper(HttpServletResponse response) {
       return new HttpServletResponseWrapper(response) {
          @Override
          public ServletOutputStream getOutputStream() throws IOException {
             return new DelegatingServletOutputStream(
                new TeeOutputStream(super.getOutputStream(), loggingOutputStream())
             );
          }
       };
    }

    private OutputStream loggingOutputStream() {
       return new LoggingOutputstream();
    }
    
    private static class LoggingOutputstream extends OutputStream {
        private ByteArrayOutputStream bos = new ByteArrayOutputStream();
        
        public LoggingOutputstream() {
        }
        
        @Override
        public void write(int b) throws IOException {
            bos.write(b);
        }
        
        @Override
        public void flush() throws IOException {
            bos.flush();
            super.flush();
            if(bos.size() > 0)
                LOGGER.debug("Response content: {}", bos.toString());
            bos.close();
        }
    }
}
