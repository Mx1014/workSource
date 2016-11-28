package com.everhomes.discover;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.RequireAuthentication;

/**
 * Author: tt
 * Date: 2016-10-25 
 * Description: 用于生成API文档
 */
@RequireAuthentication(false)
@RestController
@RequestMapping("/api")
public class ApiDiscoverDocController extends ControllerBase {

	private static List<MyMethod> LIST_MY_METHOD;
	private static Map<String, MyMethod> MAP_MY_METHOD;
	
	// 工程源码的路径，例如，E:/workspace/ehnextgen
	@Value("${src.path:E:/workspace/ehnextgen}")
	private String srcPath;

	@Value("${javadoc.root}")
    private String javadocRoot;

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiDiscoverDocController.class);

	/**
	 * <p>生成API文档</p>
	 * <b>URL: /api/generateApiDocs</b>
	 */
	@RequestMapping("generateApiDocs")
	@RestReturn(String.class)
	public RestResponse generateApiDocs(@RequestParam("url") String url, @RequestParam("reload") Boolean reload) {
		getMethods(reload);
		if (url == null || url.isEmpty()) {
			return new RestResponse(LIST_MY_METHOD);
		}
		return new RestResponse(MAP_MY_METHOD.get(url));
	}

	private void getMethods(Boolean reload) {
		if (LIST_MY_METHOD != null && (reload == null || reload == false)) {
			return ;
		}
		List<RestMethod> restMethodList = getRestMethodList(javadocRoot, "core");
		Map<String, MyMethod> myMethodMap = scanController();
		Map<String, Map<String, String>> restMap = scanRest();
		
		List<MyMethod> myMethodList = restMethodList.stream().map(r->{
			MyMethod myMethod = getMyMethod(r, myMethodMap);
			
			if (myMethod.getCommandType() != null) {
				List<MyParam> commandParamList = calculateParamType(myMethod.getCommandType(), 0, restMap);
				if (myMethod.getParamList() == null) {
					myMethod.setParamList(commandParamList);
				}else {
					myMethod.getParamList().addAll(commandParamList);
				}
			}
			
			if (myMethod.getReturnType() != null) {
				myMethod.setReturnParamList(calculateParamType(myMethod.getReturnType(), 0, restMap));
			}
			
			return myMethod;
		}).collect(Collectors.toList());
		
		LIST_MY_METHOD = myMethodList;
		MAP_MY_METHOD = myMethodMap;
	}
	
	private MyMethod getMyMethod(RestMethod restMethod, Map<String, MyMethod> myMethodMap) {
		MyMethod myMethod = myMethodMap.get(restMethod.getUri());
		if (myMethod == null) {
			myMethod = new MyMethod();
			myMethod.setUrl(restMethod.getUri());
			myMethod.setDescription(restMethod.getDescription());
			myMethodMap.put(myMethod.getUrl(), myMethod);
		}
		return myMethod;
	}
	
	private List<MyParam> calculateParamType(Class<?> paramType, int level, Map<String, Map<String, String>> restMap) {
		List<MyParam> resultList = new ArrayList<>();
		if (paramType.isEnum()) {
			try {
				Field[] fields = getAllFields(paramType);
				for (Field field : fields) {
					if (field.getModifiers() == 16409) {
						Object object = field.get(null);
						Method getCodeMethod = paramType.getMethod("getCode");
						Object value = getCodeMethod.invoke(object);
						
						String description = getDescription(paramType, field.getName(), restMap);
						if (description == null || description.isEmpty()) {
							description = value.toString();
						} else if (!description.contains(value.toString())) {
							description = value.toString() + ", " + description;
						}
						
						MyParam myParam = new MyParam();
						myParam.setParamType(getCodeMethod.getReturnType());
						myParam.setParamName(field.getName());
						myParam.setLevel(level);
						myParam.setIsEnum(true);
						myParam.setDescription(description);
						resultList.add(myParam);
					}
				}
			} catch (Exception e) {
				LOGGER.debug("getSubParams: enum error: " + paramType.getName());
			}
		}else {
			Field[] fields = getAllFields(paramType);
			for (Field field : fields) {
				// 表示为private类型
				if (field.getModifiers() == 2) {
					MyParam myParam = new MyParam();
					myParam.setParamType(field.getType());
					myParam.setParamName(field.getName());
					myParam.setLevel(level);
					myParam.setDescription(getDescription(paramType, field.getName(), restMap));
					resultList.add(myParam);
					if (List.class.isAssignableFrom(field.getType())) {
						Class<?> listActualType = getCollectionFieldActualType(field);
						myParam.setGenericType(listActualType);
						if (isNotPrimaryType(listActualType) && !listActualType.getName().equals(paramType.getName())) {
							myParam.addSubParams(calculateParamType(listActualType, level+1, restMap));
						}
					} else if (isNotPrimaryType(field.getType())) {
						if (!field.getType().getName().equals(paramType.getName()) && field.getType() != Map.class) {
							myParam.addSubParams(calculateParamType(field.getType(), level+1, restMap));
						}
					} else {
						// 如果为基本类型，查看其注释后面有没有@link，如果有表示为枚举或多对象类型，取出其的注解
						String enumComment = getDescription(paramType, field.getName(), restMap);
						if (enumComment != null && enumComment.contains("@link")) {
							Pattern pattern = Pattern.compile("\\{@link (.*?)\\}");
							Matcher matcher = pattern.matcher(enumComment);
							while (matcher.find()) {
								String linkName = matcher.group(1);
								try {
									linkName = linkName.replace(" ", "");
									if ("com.everhomes.rest.organization.organizationType".equals(linkName)) {
										linkName = "com.everhomes.rest.organization.OrganizationType";
									}else if ("com.everhomes.rest.ui.user.sceneType".equals(linkName)) {
										linkName = "com.everhomes.rest.ui.user.SceneType";
									}
									myParam.addSubParams(calculateParamType(Class.forName(linkName), level+1, restMap));
								} catch (Exception e) {
									LOGGER.debug("getSubParams: link class not found: " + linkName);
								}
							}
						}
					}
				}
			}
		}
		
		return resultList;
	}
	
	private Field[] getAllFields(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		if (clazz.getSuperclass() == Object.class) {
			return clazz.getDeclaredFields();
		}else{
			Field[] aFields = getAllFields(clazz.getSuperclass());
			Field[] bFields = clazz.getDeclaredFields();
			if (aFields == null) {
				return bFields;
			}
			Field[] fields = new Field[aFields.length+bFields.length];
			System.arraycopy(aFields, 0, fields, 0, aFields.length);
			System.arraycopy(bFields, 0, fields, aFields.length, bFields.length);
			
			return fields;
		}
	}
	
	private String getDescription(Class<?> clazz, String fieldName, Map<String, Map<String, String>> restMap) {
		if (clazz != null) {
			Map<String, String> map = restMap.get(clazz.getName());
			if (map != null) {
				return map.get(fieldName);
			}
		}
		
		return null;
	}
	
	public static boolean isNotPrimaryType(Class<?> fieldType) {
		return !fieldType.isPrimitive() && fieldType != String.class
				&& !Date.class.isAssignableFrom(fieldType) && Timestamp.class != fieldType
				&& !Number.class.isAssignableFrom(fieldType) && !Enum.class.isAssignableFrom(fieldType);
	}
	
	private String getControllerPath() {
		return (srcPath + "/ehcore/src/main/java").replaceAll("//", "/").replaceAll(" ", "");
	}

	private String getRestPath() {
		return (srcPath + "/ehrest/src/main/java").replaceAll("//", "/").replaceAll(" ", "");
	}

	// 获取所有controller和rest的绝对路径
	private List<String> getAllClassPath(String path, String postfix) {
		List<String> filePaths = new ArrayList<>();
		File root = new File(path);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				filePaths.addAll(getAllClassPath(file.getAbsolutePath(), postfix));
			} else {
				if (StringUtils.isBlank(postfix) || file.getAbsolutePath().endsWith(postfix)) {
					filePaths.add(file.getAbsolutePath());
				}
			}
		}
		return filePaths;
	}

	private Map<String, Class<?>> getAllControllers(String path) {
		return getAllClasses(path, "Controller.java");
	}

	private Map<String, Class<?>> getAllRests(String path) {
		return getAllClasses(path, null);
	}

	// key为类绝对路径，value为类的Class对象
	private Map<String, Class<?>> getAllClasses(String path, String postfix) {
		Map<String, Class<?>> map = new HashMap<>();
		List<String> list = getAllClassPath(path, postfix);
		list.forEach(s -> {
			String className = s.substring(s.indexOf("com")).replaceAll("\\/", "\\.").replaceAll("\\\\", "\\.")
					.replace(".java", "");
			try {
				map.put(s, Class.forName(className));
			} catch (Exception e) {
				LOGGER.debug("not found class: " + className);
			}
		});

		return map;
	}

	// key为url, value为方法实体
	private Map<String, MyMethod> scanController() {
		Map<String, MyMethod> map = new HashMap<>();

		Map<String, Class<?>> clazzes = getAllControllers(getControllerPath());
		clazzes.forEach((key, value) -> {
			List<MyMethod> myMethods = getEffectiveMethod(key, value);
			myMethods.forEach(m -> {
				map.put(m.getUrl(), m);
			});
		});

		return map;
	}

	private List<MyMethod> getEffectiveMethod(String path, Class<?> clazz) {
		List<MyMethod> resultList = new ArrayList<>();

		String fileContent = getFileContent(path);

		RequestMapping clazzMapping = clazz.getAnnotation(RequestMapping.class);
		String clazzMappingValue = "";
		if (clazzMapping != null) {
			clazzMappingValue = clazzMapping.value()[0];
		}

		Method[] methods = clazz.getMethods();

		for (Method method : methods) {
			RequestMapping methodMapping = null;
			if ((methodMapping = method.getAnnotation(RequestMapping.class)) != null
					&& method.getAnnotation(SuppressDiscover.class) == null) {
				MyMethod myMethod = new MyMethod();
				String methodMappingValue = methodMapping.value()[0];
				String url = (clazzMappingValue + "/" + methodMappingValue).replaceAll("//", "/");
				if (!url.startsWith("/")) {
					url = "/" + url;
				}
				myMethod.setUrl(url);
				String methodName = method.getName();
				myMethod.setMethodName(methodName);
				myMethod.setDescription(getMethodDescription(methodName, methodMappingValue, fileContent));
				myMethod.setCommandType(getCommandType(method));
				myMethod.setReturnType(getReturnType(method));
				myMethod.setParamList(getParamListWithoutCommand(method));
				resultList.add(myMethod);
			}
		}

		return resultList;
	}

	//获取除command以外的参数
	private List<MyParam> getParamListWithoutCommand(Method method) {
		List<MyParam> resultList = new ArrayList<>();
		Class<?>[] params = method.getParameterTypes();
		Annotation[][] annotations = method.getParameterAnnotations();
		if (params != null && params.length > 0) {
			for (int i=0; i < params.length; i++) {
				Class<?> paramType = params[i];
				Annotation[] paramAnnotations = annotations[i];
				RequestParam requestParam = null;
				for (Annotation annotation : paramAnnotations) {
					if (RequestParam.class.isInstance(annotation)) {
						requestParam = (RequestParam) annotation;
						break;
					}
				}
				if (requestParam != null) {
					String paramName = requestParam.value();
					MyParam myParam = new MyParam();
					myParam.setParamType(paramType);
					myParam.setParamName(paramName);
					myParam.setLevel(0);
					resultList.add(myParam);
				}
			}
		}
		return resultList;
	}

	private Class<?> getReturnType(Method method) {
		RestReturn restReturn = null;
		if ((restReturn = method.getAnnotation(RestReturn.class)) != null
				&& restReturn.value() != String.class && isNotPrimaryType(restReturn.value())) {
			return restReturn.value();
		}
		return null;
	}

	private Class<?> getCommandType(Method method) {
		Class<?>[] params = method.getParameterTypes();
		if (params != null) {
			for (Class<?> paramType : params) {
				if (isNotPrimaryType(paramType) && !paramType.isInterface()) {
					return paramType;
				}
			}
		}
		
		return null;
	}

	private String getMethodDescription(String methodName, String requestMappingName, String content) {
		Pattern pattern = Pattern
				.compile("(/\\*\\*[^@]*?\\*/)[^\\*]*" + requestMappingName + "[^\\*]*" + methodName + "\\(");
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			String comment = matcher.group(1);
			Pattern pattern2 = Pattern.compile("<p>(.*)</p>");
			matcher = pattern2.matcher(comment);
			if (matcher.find()) {
				return matcher.group(1).replaceAll(" ", "").replaceAll("\\*", "").replaceAll("\t", "");
			}
		}

		return null;
	}

	// key1为类绝对路径，key2为字段名，value为描述
	private Map<String, Map<String, String>> scanRest() {
		Map<String, Map<String, String>> map = new HashMap<>();

		Map<String, Class<?>> clazzes = getAllRests(getRestPath());
		clazzes.forEach((key, value) -> {
			Map<String, String> fields = getCommentFields(key, value);
			if (!fields.isEmpty()) {
				map.put(value.getName(), fields);
			}
		});

		return map;
	}

	// key为字段名，value为描述
	private Map<String, String> getCommentFields(String path, Class<?> clazz) {
		Map<String, String> map = new HashMap<>();

		String content = getFileContent(path);

		Pattern pattern = Pattern.compile("(/\\*\\*.*?\\*/)[^\\*]*public");
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			String comment = matcher.group(1);
			Pattern pattern2 = Pattern.compile("<li>(.*?)</li>");
			matcher = pattern2.matcher(comment);
			while (matcher.find()) {
				String li = matcher.group(1).replaceAll("\\*", "").replaceAll("\n", "").replaceAll(" ", "")
						.replaceAll("@link", "@link ").replaceAll("：", ":").replaceAll("\t", "");
				if (li.contains(":")) {
					String[] kv = li.split(":");
					if (kv.length >= 2) {
						String key = kv[0];
						if (key.contains("(")) {
							key = key.replaceAll("（", "(").replaceAll("）", ")").replaceAll("\\(.*\\)", "");
						}
						String value = li.substring(li.indexOf(":") + 1);
						map.put(key, value);
					}
				}
			}
		}

		return map;
	}

	private static Class<?> getCollectionFieldActualType(Field field) {
		Type genType = field.getGenericType();
		if (genType instanceof ParameterizedType) {
			Type[] parameterizedTypes = ((ParameterizedType) genType).getActualTypeArguments();
			if (parameterizedTypes.length > 0) {
				return ((Class<?>) parameterizedTypes[0]);
			} else {
				throw new RuntimeException("All List type must set its generic type!");
			}
		} else {
			throw new RuntimeException("All List type must set its generic type!");
		}
	}
	
	public static String getFileContent(String filePath) {
		StringBuilder content = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(filePath)));
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				content.append(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content.toString();
	}

	private class MyMethod {
		private String url;
		private String methodName;
		private String description;
		private List<MyParam> paramList;
		private Class<?> commandType;
		private List<MyParam> returnParamList;
		private Class<?> returnType;

		public Class<?> getReturnType() {
			return returnType;
		}

		public void setReturnType(Class<?> returnType) {
			this.returnType = returnType;
		}

		public List<MyParam> getReturnParamList() {
			return returnParamList;
		}

		public void setReturnParamList(List<MyParam> returnParamList) {
			this.returnParamList = returnParamList;
		}

		public Class<?> getCommandType() {
			return commandType;
		}

		public void setCommandType(Class<?> commandType) {
			this.commandType = commandType;
		}

		public List<MyParam> getParamList() {
			return paramList;
		}

		public void setParamList(List<MyParam> paramList) {
			this.paramList = paramList;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getMethodName() {
			return methodName;
		}

		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	private class MyParam {
		private Class<?> paramType;
		private Class<?> genericType;
		private String paramName;
		private String description;
		private Integer level;
		private Boolean isEnum;
		private List<List<MyParam>> subParams;

		public Boolean getIsEnum() {
			return isEnum;
		}

		public void setIsEnum(Boolean isEnum) {
			this.isEnum = isEnum;
		}

		public Class<?> getGenericType() {
			return genericType;
		}

		public void setGenericType(Class<?> genericType) {
			this.genericType = genericType;
		}

		public Class<?> getParamType() {
			return paramType;
		}

		public void setParamType(Class<?> paramType) {
			this.paramType = paramType;
		}

		public Integer getLevel() {
			return level;
		}

		public void setLevel(Integer level) {
			this.level = level;
		}

		public String getParamName() {
			return paramName;
		}

		public void setParamName(String paramName) {
			this.paramName = paramName;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public List<List<MyParam>> getSubParams() {
			return subParams;
		}

		public void setSubParams(List<List<MyParam>> subParams) {
			this.subParams = subParams;
		}
		
		public void addSubParams(List<MyParam> subParams) {
			if (this.subParams == null) {
				this.subParams = new ArrayList<>();
			}
			this.subParams.add(subParams);
		}
	}
}
