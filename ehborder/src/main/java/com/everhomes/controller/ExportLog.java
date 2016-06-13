package com.everhomes.controller;

import java.io.BufferedReader;
import java.io.FileReader;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(method = RequestMethod.GET, value = "/borderlog")
public class ExportLog {
    @Value("${logFileName:/tmp/log.txt}")
    private String logFileName;
    
    @RequestMapping(method = RequestMethod.GET)
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(logFileName));
        try {
            response.setContentType("text/plain");
            //response.setHeader("Content-Disposition","attachment;filename=myFile.txt");
            ServletOutputStream out = response.getOutputStream();
            
            String line = br.readLine();
            StringBuilder sb = new StringBuilder();

            while (line != null) {
                //out.println(line + System.lineSeparator());
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            
            if(sb.length() > 10240) {
                out.println(sb.substring(sb.length()-10240));
            }
            
            out.flush();
            out.close();
            
        } finally {
            try{
                if(br != null) {
                    br.close();
                    }
                
            } catch(Exception ex) {
                
            }
            
        }
    }
}
