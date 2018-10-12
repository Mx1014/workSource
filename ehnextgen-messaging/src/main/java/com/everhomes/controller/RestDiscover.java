package com.everhomes.controller;

import com.everhomes.discover.*;
import com.everhomes.util.ReflectionHelper;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 因为 com.everhomes.discover.RestDiscover 不能修改，但是又需要拓展东西，所以将它拷贝一份
 */
public class RestDiscover {
    public static List<ExtendRestMethod> discover(Class<?> controllerClass, List<ExtendRestMethod> methods) {
        if (methods == null) {
            methods = new ArrayList();
        }

        RequestMapping clzRequestMapping = (RequestMapping)controllerClass.getAnnotation(RequestMapping.class);
        String root = "/";
        if (clzRequestMapping != null && clzRequestMapping.value() != null) {
            root = clzRequestMapping.value()[0];
        }

        RestDoc controllerRestDoc = (RestDoc)controllerClass.getAnnotation(RestDoc.class);
        Method[] var5 = controllerClass.getMethods();
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Method method = var5[var7];
            RequestMapping methodRequestMapping = (RequestMapping)method.getAnnotation(RequestMapping.class);
            SuppressDiscover suppressDiscover = (SuppressDiscover)method.getAnnotation(SuppressDiscover.class);
            XssExclude xssExclude = (XssExclude)method.getAnnotation(XssExclude.class);
            if (methodRequestMapping != null && suppressDiscover == null) {
                ExtendRestMethod restMethod = new ExtendRestMethod();
                restMethod.setXssExclude(xssExclude);
                if (methodRequestMapping.value() != null) {
                    String uri = methodRequestMapping.value()[0];
                    if (uri.startsWith("/")) {
                        restMethod.setUri(uri);
                    } else {
                        restMethod.setUri(root + "/" + uri);
                    }
                }

                restMethod.setPackageName(controllerClass.getPackage().getName());
                if (controllerRestDoc != null) {
                    restMethod.setJavadocSite(controllerRestDoc.site());
                }

                restMethod.setJavadocUrl(getJavadocUrl(controllerClass, method, methodRequestMapping));
                Class<?>[] paramTypes = method.getParameterTypes();
                Annotation[][] annotations = method.getParameterAnnotations();

                for(int i = 0; i < paramTypes.length; ++i) {
                    if (!isTypeToSkip(paramTypes[i])) {
                        populateRestMethodParam(restMethod, paramTypes[i], annotations[i]);
                    }
                }

                populateRestMethodReturnInfo(restMethod, method);
                RestDoc doc = (RestDoc)method.getAnnotation(RestDoc.class);
                if (doc != null) {
                    restMethod.setDescription(doc.value());
                }

                ((List)methods).add(restMethod);
            }
        }

        return (List)methods;
    }

    private static ItemType inferCollectionFieldItemType(Field field) {
        final Type[] fieldArgTypes = ReflectionHelper.getGenericFieldParameterizedTypeArgumentTypes(field);
        if (fieldArgTypes != null) {
            if (List.class.isAssignableFrom(field.getType())) {
                return new ItemType() {
                    public Class<? extends Annotation> annotationType() {
                        return ItemType.class;
                    }

                    public Class<?> value() {
                        return (Class)fieldArgTypes[0];
                    }

                    public int sampleRepeats() {
                        return 4;
                    }
                };
            }

            if (Map.class.isAssignableFrom(field.getType())) {
                return new ItemType() {
                    public Class<? extends Annotation> annotationType() {
                        return ItemType.class;
                    }

                    public Class<?> value() {
                        return (Class)fieldArgTypes[1];
                    }

                    public int sampleRepeats() {
                        return 4;
                    }
                };
            }
        }

        return null;
    }

    private static void populateRestMethodReturnInfo(RestMethod restMethod, Method method) {
        RestReturn restReturn = (RestReturn)method.getAnnotation(RestReturn.class);
        if (restReturn != null) {
            restMethod.setReturnCollection(restReturn.collection());
            if (restReturn.value() != null) {
                restMethod.setReturnType(restReturn.value());
                StringBuffer sb = new StringBuffer();
                if (restReturn.html()) {
                    sb.append("\"HTML page\"");
                } else {
                    if (restReturn.collection()) {
                        sb.append("[");
                    }

                    populateTypeReturn(sb, restReturn.value(), (ItemType)null);
                    if (restReturn.collection()) {
                        sb.append("]");
                    }
                }

                restMethod.setReturnTemplate(sb.toString());
            }
        }

    }

    private static void populateTypeReturn(StringBuffer sb, Class<?> type, ItemType item) {
        if (!type.isPrimitive() && type != String.class && !Date.class.isAssignableFrom(type) && type != Timestamp.class && !Number.class.isAssignableFrom(type) && !Enum.class.isAssignableFrom(type)) {
            if (!Collection.class.isAssignableFrom(type) && !type.isArray()) {
                sb.append("{");
                int count = 0;

                for(Iterator var4 = getFlattenFields(type).iterator(); var4.hasNext(); ++count) {
                    Field field = (Field)var4.next();
                    if (count > 0) {
                        sb.append(", ");
                    }

                    sb.append("\"" + field.getName() + "\": ");
                    ItemType itemTypeOnField = (ItemType)field.getAnnotation(ItemType.class);
                    if (itemTypeOnField == null) {
                        itemTypeOnField = inferCollectionFieldItemType(field);
                    }

                    if (itemTypeOnField != null && (itemTypeOnField == null || itemTypeOnField.value() == type)) {
                        sb.append("{\"type\": \"recursive object\"}");
                    } else {
                        populateTypeReturn(sb, field.getType(), itemTypeOnField);
                    }
                }

                sb.append("}");
            } else {
                if (item != null) {
                    sb.append("[");
                    populateTypeReturn(sb, item.value(), (ItemType)null);
                    sb.append("]");
                } else {
                    sb.append("[{\"type\": \"object\"}]");
                }

            }
        } else {
            sb.append(String.format("\"%s\"", type.getSimpleName()));
        }
    }

    private static <T extends Annotation> T getAnnotation(Class<T> clz, Annotation[] annotations) {
        Annotation[] var2 = annotations;
        int var3 = annotations.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Annotation annotation = var2[var4];
            if (clz.isInstance(annotation)) {
                return (T) annotation;
            }
        }

        return (T) null;
    }

    private static void populateRestMethodParam(RestMethod restMethod, Class<?> paramType, Annotation[] paramAnnotations) {
        RequestParam requestParam = (RequestParam)getAnnotation(RequestParam.class, paramAnnotations);
        PathVariable pathParam = (PathVariable)getAnnotation(PathVariable.class, paramAnnotations);
        ItemType itemType = (ItemType)getAnnotation(ItemType.class, paramAnnotations);
        RestParam restParam;
        Annotation[] var7;
        int var8;
        int var9;
        Annotation annotation;
        if (requestParam != null) {
            restParam = new RestParam();
            restParam.setParamName(requestParam.value());
            restParam.setRequired(requestParam.required());
            restParam.setType(paramType);
            if (itemType != null) {
                restParam.setItemType(itemType.value());
            }

            restParam.setTypeName(paramType.getSimpleName());
            var7 = paramAnnotations;
            var8 = paramAnnotations.length;

            for(var9 = 0; var9 < var8; ++var9) {
                annotation = var7[var9];
                if (annotation instanceof RestDoc) {
                    restParam.setDescription(((RestDoc)annotation).value());
                    break;
                }
            }

            restMethod.addParam(restParam);
        } else if (pathParam != null) {
            restParam = new RestParam();
            restParam.setParamName(pathParam.value());
            restParam.setPathVariable(true);
            restParam.setType(paramType);
            if (itemType != null) {
                restParam.setItemType(itemType.value());
            }

            restParam.setTypeName(paramType.getSimpleName());
            var7 = paramAnnotations;
            var8 = paramAnnotations.length;

            for(var9 = 0; var9 < var8; ++var9) {
                annotation = var7[var9];
                if (annotation instanceof RestDoc) {
                    restParam.setDescription(((RestDoc)annotation).value());
                    break;
                }
            }

            restMethod.addParam(restParam);
        } else {
            if (paramType.isPrimitive() || paramType == String.class || Date.class.isAssignableFrom(paramType) || Timestamp.class == paramType || Number.class.isAssignableFrom(paramType) || Enum.class.isAssignableFrom(paramType) || Collection.class.isAssignableFrom(paramType)) {
                throw RuntimeErrorException.errorWith("general", 503, "Unsupported REST method declaration", new Object[0]);
            }

            flattenFieldsToRestParams((String)null, restMethod, paramType);
        }

    }

    private static void flattenFieldsToRestParams(String prefix, RestMethod restMethod, Class<?> paramType) {
        List<Field> fields = getFlattenFields(paramType);
        Iterator var4 = fields.iterator();

        while(true) {
            while(true) {
                while(var4.hasNext()) {
                    Field field = (Field)var4.next();
                    Class<?> fieldType = field.getType();
                    if (List.class.isAssignableFrom(fieldType)) {
                        ItemType itemType = (ItemType)field.getAnnotation(ItemType.class);
                        if (itemType == null) {
                            itemType = inferCollectionFieldItemType(field);
                        }

                        if (itemType == null) {
                            throw RuntimeErrorException.errorWith("general", 503, "No ItemType annoated on field %s", new Object[]{field.getName()});
                        }

                        String p;
                        int i;
                        if (!itemType.value().isPrimitive() && itemType.value() != String.class && !Date.class.isAssignableFrom(itemType.value()) && Timestamp.class != itemType.value() && !Number.class.isAssignableFrom(itemType.value()) && !Enum.class.isAssignableFrom(itemType.value())) {
                            for(i = 0; i < itemType.sampleRepeats(); ++i) {
                                p = prefix != null ? prefix + "." + field.getName() : field.getName();
                                if (itemType.value() != paramType) {
                                    flattenFieldsToRestParams(p + "[" + i + "]", restMethod, itemType.value());
                                }
                            }
                        } else {
                            for(i = 0; i < itemType.sampleRepeats(); ++i) {
                                p = prefix != null ? prefix + "." + field.getName() : field.getName();
                                RestParam restParam = new RestParam();
                                restParam.setParamName(p + "[" + i + "]");
                                restParam.setRequired(false);
                                restParam.setType(fieldType);
                                restParam.setItemType(itemType.value());
                                restParam.setTypeName(itemType.value().getSimpleName());
                                restMethod.addParam(restParam);
                            }
                        }
                    } else if (!fieldType.isPrimitive() && fieldType != String.class && !Date.class.isAssignableFrom(fieldType) && Timestamp.class != fieldType && !Number.class.isAssignableFrom(fieldType) && !Enum.class.isAssignableFrom(fieldType)) {
                        if (fieldType != paramType) {
                            flattenFieldsToRestParams(prefix != null ? prefix + "." + field.getName() : field.getName(), restMethod, fieldType);
                        }
                    } else {
                        RestParam restParam = new RestParam();
                        restParam.setParamName(prefix != null ? prefix + "." + field.getName() : field.getName());
                        restParam.setRequired(field.getAnnotation(NotNull.class) != null);
                        restParam.setType(fieldType);
                        restParam.setTypeName(fieldType.getSimpleName());
                        RestDoc doc = (RestDoc)field.getAnnotation(RestDoc.class);
                        if (doc != null) {
                            restParam.setDescription(doc.value());
                        }

                        restMethod.addParam(restParam);
                    }
                }

                return;
            }
        }
    }

    private static boolean isTypeToSkip(Class<?> type) {
        return type == HttpServletRequest.class || type == HttpServletResponse.class || type == Model.class;
    }

    private static List<Field> getFlattenFields(Class<?> type) {
        List<Field> fields = ReflectionHelper.getFlattenFields(type);
        return (List)fields.stream().filter((f) -> {
            return (f.getModifiers() & 8) == 0;
        }).collect(Collectors.toList());
    }

    private static String getJavadocUrl(Class<?> controllerClass, Method method, RequestMapping methodRequestMapping) {
        StringBuffer sb = new StringBuffer();
        sb.append(controllerClass.getName().replace('.', '/'));
        sb.append(".html#").append(methodRequestMapping.value()[0]);
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes != null) {
            Class[] var5 = paramTypes;
            int var6 = paramTypes.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Class<?> paramType = var5[var7];
                sb.append("-").append(paramType.getName());
            }
        }

        sb.append("-");
        return sb.toString();
    }
}
