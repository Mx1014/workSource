package com.everhomes.discover;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.hibernate.mapping.Collection;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;

/**
 * 
 * Performs REST API discovery
 * 
 * @author Kelven Yang
 *
 */
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
                
                populateRestMethodReturnInfo(restMethod, method);
                
                RestDoc doc = method.getAnnotation(RestDoc.class);
                if(doc != null)
                    restMethod.setDescription(doc.value());
                methods.add(restMethod);
            }
        }
        
        return methods;
    }
    
    private static void populateRestMethodReturnInfo(RestMethod restMethod, Method method) {
        RestReturn restReturn = method.getAnnotation(RestReturn.class);
        if(restReturn != null) {
            restMethod.setReturnCollection(restReturn.collection());
            if(restReturn.value() != null) {
                restMethod.setReturnTypeName(restReturn.value().getSimpleName());
                StringBuffer sb = new StringBuffer();
                if(restReturn.html()) {
                    sb.append("HTML page");
                } else {
                    if(restReturn.collection())
                        sb.append("[");
                    
                    populateTypeReturn(sb, restReturn.value(), null);
                    
                    if(restReturn.collection())
                        sb.append("]");
                }
                restMethod.setReturnTemplate(sb.toString());
            }
        }
    }
    
    private static void populateTypeReturn(StringBuffer sb, Class<?> type, ItemType item) {
        if(type.isPrimitive() || type == String.class || type == Date.class ||
            Number.class.isAssignableFrom(type) ||
            Enum.class.isAssignableFrom(type)) {
            
            sb.append(type.getSimpleName());
            return;
        }
        
        if(Collection.class.isAssignableFrom(type) || type.isArray()) {
            if(item != null) {
                sb.append("[");
                populateTypeReturn(sb, item.value(), null);
                sb.append("]");
            } else {
                sb.append("[ Object ]");
            }
            return;
        }
        
        sb.append("{");
        int count = 0;
        for(Field field: getFlattenFields(type)) {
            if(count > 0)
                sb.append(", ");
            sb.append(field.getName() + ": ");
            populateTypeReturn(sb, field.getType(), field.getAnnotation(ItemType.class));
            count++;
        }
        sb.append("}");
    }
    
    @SuppressWarnings("unchecked")
    private static <T extends Annotation> T getAnnotation(Class<T> clz, Annotation[] annotations) {
        for(Annotation annotation : annotations) {
            if(clz.isInstance(annotation))
                return (T)annotation;
        }
        return (T)null;
    }
    
    private static void populateRestMethodParam(RestMethod restMethod,  
        Class<?> paramType, Annotation[] paramAnnotations) {
       
        RequestParam requestParam = getAnnotation(RequestParam.class, paramAnnotations);
        PathVariable pathParam = getAnnotation(PathVariable.class, paramAnnotations);
        
        if(requestParam != null) {
            RestParam restParam = new RestParam();
            restParam.setParamName(requestParam.value());
            restParam.setRequired(requestParam.required());
            restParam.setTypeName(paramType.getSimpleName());
            
            for(Annotation annotation: paramAnnotations) {
                if(annotation instanceof RestDoc) {
                    restParam.setDescription(((RestDoc)annotation).value());
                    break;
                }
            }
            
            restMethod.addParam(restParam);
        } else if (pathParam != null) {
            RestParam restParam = new RestParam();
            restParam.setParamName(pathParam.value());
            restParam.setPathVariable(true);
            restParam.setTypeName(paramType.getSimpleName());
            
            for(Annotation annotation: paramAnnotations) {
                if(annotation instanceof RestDoc) {
                    restParam.setDescription(((RestDoc)annotation).value());
                    break;
                }
            }
            
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
        List<Field> fields = getFlattenFields(paramType);
        
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
            
            RestDoc doc = field.getAnnotation(RestDoc.class);
            if(doc != null)
                restParam.setDescription(doc.value());
            
            restMethod.addParam(restParam);
        }
    }
    
    private static boolean isTypeToSkip(Class<?> type) {
        if(type == HttpServletRequest.class || type == HttpServletResponse.class || type == Model.class)
            return true;
        
        return false;
    }
    
    private static List<Field> getFlattenFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        Class<?> clz = type;
        while(clz != Object.class) {
            for(Field f: clz.getDeclaredFields()) {
                if((f.getModifiers() & Modifier.STATIC) == 0)
                    fields.add(f);
            }
            
            clz = clz.getSuperclass();
        }
        
        return fields;
    }
}
