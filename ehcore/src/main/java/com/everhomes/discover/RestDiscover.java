package com.everhomes.discover;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.hibernate.mapping.Collection;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;

public class RestDiscover {
    public static List<RestMethod> discover(Class<?> controllerClass, List<RestMethod> methods) {
        if(methods == null)
            methods = new ArrayList<RestMethod>();
        
        RequestMapping clzRequestMapping = controllerClass.getAnnotation(RequestMapping.class);
        String root = "/";
        if(clzRequestMapping != null && clzRequestMapping.value() != null)
            root = clzRequestMapping.value()[0];
        
        for(Method method : controllerClass.getMethods()) {
            RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
            if(methodRequestMapping != null) {
                RestMethod restMethod = new RestMethod();
                if(methodRequestMapping.value() != null) {
                    String uri = methodRequestMapping.value()[0];
                    if(uri.startsWith("/"))
                        restMethod.setUri(uri);
                    else
                        restMethod.setUri(root + "/" + uri);
                }
                
                Class<?>[] paramTypes = method.getParameterTypes();
                Annotation[][] annotations = method.getParameterAnnotations();
                for(int i = 0; i < paramTypes.length; i++) {
                    if(!isTypeToSkip(paramTypes[i])) {
                        populateRestMethodParam(restMethod, paramTypes[i], annotations[i]);
                    }
                }
                
                methods.add(restMethod);
            }
        }
        
        return methods;
    }
    
    private static void populateRestMethodParam(RestMethod restMethod,  
        Class<?> paramType, Annotation[] paramAnnotations) {
       
        RequestParam requestParam = null;
        for(Annotation annotation: paramAnnotations) {
            if(annotation instanceof RequestParam) {
                requestParam = (RequestParam)annotation;
                break;
            }
        }
        
        if(requestParam != null) {
            RestParam restParam = new RestParam();
            restParam.setParamName(requestParam.value());
            restParam.setRequired(requestParam.required());
            restParam.setTypeName(paramType.getSimpleName());
            restMethod.addParam(restParam);
        } else {
            if(paramType.isPrimitive() || paramType == String.class || paramType == Date.class ||
                Number.class.isAssignableFrom(paramType) ||
                Enum.class.isAssignableFrom(paramType) || 
                Collection.class.isAssignableFrom(paramType)) {
                
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE, "Unsupported REST method declaration");
            }
            
            flattenFieldsToRestParams(restMethod, paramType);
        }
    }
    
    private static void flattenFieldsToRestParams(RestMethod restMethod, Class<?> paramType) {
        List<Field> fields = new ArrayList<Field>();
        Class<?> clz = paramType;
        while(clz != Object.class) {
            for(Field f: clz.getDeclaredFields())
                fields.add(f);
            
            clz = clz.getSuperclass();
        }
        
        for(Field field: fields) {
            Class<?> fieldType = field.getType();
            
            if(!(fieldType.isPrimitive() || fieldType == String.class || fieldType == Date.class ||
                Number.class.isAssignableFrom(fieldType) ||
                Enum.class.isAssignableFrom(fieldType))) 
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE, "Unsupported REST method declaration");
            
            RestParam restParam = new RestParam();
            restParam.setParamName(field.getName());
            restParam.setRequired(field.getAnnotation(NotNull.class) != null);
            restParam.setTypeName(fieldType.getSimpleName());
            restMethod.addParam(restParam);
        }
    }
    
    private static boolean isTypeToSkip(Class<?> type) {
        if(type == HttpServletRequest.class || type == HttpServletResponse.class)
            return true;
        
        return false;
    }
}
