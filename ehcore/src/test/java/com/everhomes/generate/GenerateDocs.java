package com.everhomes.generate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.bind.annotation.RequestMapping;

import com.everhomes.discover.RestReturn;
import com.everhomes.discover.SuppressDiscover;
import com.everhomes.rentalv2.Rentalv2Controller;
import com.everhomes.rentalv2.admin.Rentalv2AdminController;

public class GenerateDocs {
	public static int urlIndex = 1;
	public static int methodIndex = 1;
	public static final boolean GENERATE_TEST_METHOD_SWITCH = true;

	public static void main(String[] args) {
		// 请输入您需要生成文档的类
//		Class<?>[] clazzes = { PropertyMgrController.class };
		Class<?>[] clazzes = { Rentalv2Controller.class , Rentalv2AdminController.class};
//
		generate(clazzes);
		
		//传null则会寻找整个工程下所有的controller
//		generate(null);
//		System.out.println(new File("").getCanonicalPath());
	}

	private static void generate(Class<?>[] clazzes) {
		if (clazzes == null) {
			clazzes = GUtils.getAllControllers();
		}
		StringBuilder sbUrl = new StringBuilder();
		StringBuilder sbMethod = new StringBuilder();

		for (int i = 0; i < clazzes.length; i++) {
			Class<?> clazz = clazzes[i];
			GInterfaces gs = new GInterfaces(clazz);
			String content = gs.toString();
			boolean append = true;
			if (i == 0) {
				append = false;
				content = "<html>\n<head>\n<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>\n<title>左邻API文档</title></head>\n<body>\n<h1>左邻API文档</h1>\n<span style='color:red'>其中，绿色表示枚举类型，部分link的类不对，请以下面的黑体标识的类为准。</span>\n\n<ol>\n"
						+ content;
			}
			if (i == clazzes.length - 1) {
				content = content + "</ol>\n</body>\n</html>";
			}
			GUtils.writeFile(content, append);

			sbUrl.append(gs.sbUrl);
			sbMethod.append(gs.sbMethod);
		}

		if (GENERATE_TEST_METHOD_SWITCH) {
			System.out.println(sbUrl);
			System.out.println();
			System.out.println(sbMethod);
		}
	}
}

class GClass {
	List<GComment> comments;
	Class<?> clazz;

	public GClass(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	public String toString() {
		comments = GUtils.getComments(clazz);
		StringBuilder sb = new StringBuilder();
		// sb.append("<ul>\n");
		sb.append("<b>" + clazz.getName() + "</b>\n");
		comments.forEach(c -> sb.append(c.toString()));
		// sb.append("</ul>");
		return sb.toString();
	}

	public String getCommandSetter() {
		return GUtils.getCommandSetter(clazz);
	}
}

class GComment {
	String name;
	String comment;
	List<String> classNames = new ArrayList<>();
	List<List<GComment>> lists = new ArrayList<>();

	public GComment(String name, String comment) {
		this.name = name;
		this.comment = comment == null ? "" : comment;
	}

	public void addClassName(String className) {
		classNames.add(className);
	}

	public void addList(List<GComment> list) {
		lists.add(list);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (name.startsWith("xxenum")) {
			name = name.replace("xxenum", "");
			sb.append("<li style='color:green'>").append(name).append(": ")
					.append(GUtils.isEmpty(comment) ? "" : comment);
		} else {
			sb.append("<li>").append(name).append(": ").append(GUtils.isEmpty(comment) ? "" : comment);
		}
		for (int i = 0; i < lists.size(); i++) {
			List<GComment> list = lists.get(i);
			String className = classNames.get(i);
			if (GUtils.isNotEmpty(list)) {
				sb.append("\n<ul>\n");
				if (GUtils.isNotEmpty(className)) {
					sb.append("<b>" + className + "</b>\n");
				}
				list.forEach(c -> sb.append(c.toString()));
				sb.append("</ul>");
			}
		}

		sb.append("</li>\n");
		return sb.toString();
	}
}

class GInterfaces {
	List<GInterface> list;
	Class<?> clazz;

	StringBuilder sbUrl = new StringBuilder();
	StringBuilder sbMethod = new StringBuilder();

	public GInterfaces(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	public String toString() {
		list = GUtils.getInterfaces(clazz);
		list.sort(null);
		StringBuilder sb = new StringBuilder();
		// sb.append("<html>\n<head>\n<meta http-equiv='Content-Type'
		// content='text/html; charset=UTF-8'>\n</head>\n<body>\n<ol>\n");

		list.forEach(g -> {
			String content = g.toString();
			if (content != null) {
				sb.append("<li>\n").append(content).append("\n</li>\n");
				sbUrl.append(g.generateTestUrl());
				sbMethod.append(g.generateTestMethod());
			}
		});
		// sb.append("</ol>\n</body>\n</html>");

		return sb.toString();
	}
}

class GInterface implements Comparable<GInterface> {
	String methodName;
	String requestMappingName;
	String url;
	String description;
	String commandComment;
	String responseComment;

	String commandSetter;

	@Override
	public String toString() {
		if (description == null) {
			description = methodName;
		}
		try {
			if (description.indexOf(".") > -1) {
				description = description.substring(description.indexOf(".") + 1);
			}
			return "<b>" + description + "\t" + url + "</b>\n" + "<p>参数：</p>\n"
					+ (commandComment.equals("<ul>\n</ul>") ? "\t无" : commandComment) + "\n<p>返回值：</p>\n"
					+ (GUtils.isEmpty(responseComment) ? "\tOK" : responseComment);
		} catch (Exception e) {
			return null;
		}
	}

	public String generateTestUrl() {
		// 生成test url
		return "//" + (GenerateDocs.urlIndex++) + ". " + description + "\nprivate static final String "
				+ GUtils.camelToUnderline(methodName).toUpperCase() + "_URL = \"" + url.replace(" ", "").replace("URL:", "") + "\";\n";
	}

	public String generateTestMethod() {
		String myUrl = url.replace(" ", "").replace("URL:", "");
		String responseName = null;
		if (GUtils.isEmpty(responseComment)) {
			responseName = "RestResponseBase";
		} else {
			responseName = GUtils.underlineToCamel(myUrl.substring(myUrl.indexOf("/", 1)).replace("/", "_"))
					+ "RestResponse";
		}

		String commandName = GUtils.capFirst(methodName) + "Command";

		// 生成test方法
		String result = "//" + (GenerateDocs.methodIndex++) + ". " + description + "\n//@Test" + "\npublic void test"
				+ GUtils.capFirst(methodName) + "() {" + "\n\tString url = "
				+ GUtils.camelToUnderline(methodName).toUpperCase() + "_URL;" + "\n\tlogon();\n" 
				+ (commandComment.equals("<ul>\n</ul>") ? "" : "\n\t" + commandName + " cmd = new " + commandName + "();" + commandSetter + "\n" )
				+ "\n\t" + responseName
				+ " response = httpClientService.restPost(url, "+(commandComment.equals("<ul>\n</ul>") ? "null" : "cmd")+", " + responseName + ".class);"
				+ "\n\tassertNotNull(response);"
				+ "\n\tassertTrue(\"response= \" + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));"
				+ "\n"
				+ ("RestResponseBase".equals(responseName) ? ""
						: "\n\t" + GUtils.capFirst(methodName) + "Response myResponse = response.getResponse();"
								+ "\n\tassertNotNull(myResponse);")
				+ "\n\n\n}\n\n";

		return result;
	}

	@Override
	public int compareTo(GInterface o) {
		try {
			int thisNum = Integer.parseInt(description.substring(0, description.indexOf(".")));
			int thatNum = Integer.parseInt(o.description.substring(0, o.description.indexOf(".")));
			if (thisNum > thatNum) {
				return 1;
			} else if (thisNum < thatNum) {
				return -1;
			}
			return 0;
		} catch (Exception e) {
			return 0;
		}

	}
}

class GUtils {
	private final static Map<String, Object> CACHE = new HashMap<>();

	public static String capFirst(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static String uncapFirst(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	public static String getCommandSetter(Class<?> clazz) {
		StringBuilder sb = new StringBuilder();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			// 表示为private类型
			if (field.getModifiers() == 2) {
				if (List.class.isAssignableFrom(field.getType())) {
					Class<?> listActualType = getCollectionFieldActualType(field);
					if (isNotPrimaryType(listActualType)) {
						sb.append("\n\tList<" + listActualType.getSimpleName() + "> "
								+ GUtils.uncapFirst(listActualType.getSimpleName()) + "List = new ArrayList<>();");
						sb.append("\n\t" + listActualType.getSimpleName() + " "
								+ GUtils.uncapFirst(listActualType.getSimpleName()) + " = new "
								+ listActualType.getSimpleName() + "();");
						sb.append(getCommandSetter(listActualType));
						sb.append("\n\t" + GUtils.uncapFirst(listActualType.getSimpleName()) + "List.add("
								+ GUtils.uncapFirst(listActualType.getSimpleName()) + ");");
						if (clazz.getSimpleName().endsWith("Command")) {
							sb.append("\n\tcmd.set" + GUtils.capFirst(field.getName()) + "("
									+ GUtils.uncapFirst(listActualType.getSimpleName()) + "List);");
						} else {
							sb.append("\n\t" + GUtils.uncapFirst(clazz.getSimpleName()) + ".set"
									+ GUtils.capFirst(field.getName()) + "("
									+ GUtils.uncapFirst(listActualType.getSimpleName()) + "List);");
						}
					} else {
						sb.append("\n\tList<" + listActualType.getSimpleName() + "> "
								+ GUtils.uncapFirst(listActualType.getSimpleName()) + "List = new ArrayList<>();");
						sb.append("\n\t" + GUtils.uncapFirst(listActualType.getSimpleName()) + "List.add("
								+ getValueFromPrimaryType(listActualType) + ");");
						if (clazz.getSimpleName().endsWith("Command")) {
							sb.append("\n\tcmd.set" + GUtils.capFirst(field.getName()) + "("
									+ GUtils.uncapFirst(listActualType.getSimpleName()) + "List);");
						} else {
							sb.append("\n\t" + GUtils.uncapFirst(clazz.getSimpleName()) + ".set"
									+ GUtils.capFirst(field.getName()) + "("
									+ GUtils.uncapFirst(listActualType.getSimpleName()) + "List);");
						}
					}
				} else if (isNotPrimaryType(field.getType())) {
					Class<?> fieldType = field.getType();
					sb.append("\n\t" + fieldType.getSimpleName() + " " + GUtils.uncapFirst(fieldType.getSimpleName())
							+ " = new " + fieldType.getSimpleName() + "();");
					sb.append(getCommandSetter(fieldType));
					if (clazz.getSimpleName().endsWith("Command")) {
						sb.append("\n\tcmd.set" + GUtils.capFirst(field.getName()) + "("
								+ GUtils.uncapFirst(fieldType.getSimpleName()) + ");");
					} else {
						sb.append("\n\t" + GUtils.uncapFirst(clazz.getSimpleName()) + ".set"
								+ GUtils.capFirst(field.getName()) + "(" + GUtils.uncapFirst(fieldType.getSimpleName())
								+ ");");
					}
				} else {
					Class<?> fieldType = field.getType();
					if (clazz.getSimpleName().endsWith("Command")) {
						sb.append("\n\tcmd.set" + GUtils.capFirst(field.getName()) + "("
								+ getValueFromPrimaryType(fieldType) + ");");
					} else {
						sb.append("\n\t" + GUtils.uncapFirst(clazz.getSimpleName()) + ".set"
								+ GUtils.capFirst(field.getName()) + "(" + getValueFromPrimaryType(fieldType) + ");");
					}
				}
			}
		}
		return sb.toString();
	}

	private static String getValueFromPrimaryType(Class<?> clzz) {
		String value = null;
		if (clzz == Long.class) {
			value = "1L";
		} else if (clzz == Integer.class) {
			value = "0";
		} else if (clzz == Byte.class) {
			value = "(byte)1";
		} else if (clzz == Timestamp.class) {
			value = "new Timestamp(DateHelper.currentGMTTime().getTime())";
		} else if (clzz == BigDecimal.class) {
			value = "new BigDecimal(\"10.23\")";
		} else {
			value = "\"\"";
		}
		return value;
	};

	public static String camelToUnderline(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append('_');
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static Field[] getAllFields(Class<?> clazz) {
		if (clazz.getSuperclass() == Object.class) {
			return clazz.getDeclaredFields();
		}else{
			Field[] aFields = getAllFields(clazz.getSuperclass());
			Field[] bFields = clazz.getDeclaredFields();
			
			Field[] fields = new Field[aFields.length+bFields.length];
			System.arraycopy(aFields, 0, fields, 0, aFields.length);
			System.arraycopy(bFields, 0, fields, aFields.length, bFields.length);
			
			return fields;
		}
	}
	
	public static List<GComment> getComments(Class<?> clazz) {
//		System.err.println(clazz.getName());
		List<GComment> result = new ArrayList<>();
		Map<String, String> comments = getComments(clazz.getName());
		if (comments == null) {
			// return result;
			comments = new HashMap<>();
		}
		if (clazz.isEnum()) {
			try {
				Field[] fields = getAllFields(clazz);
				for (Field field : fields) {
					if (field.getModifiers() == 16409) {
						Object object = field.get(null);
						Method getCodeMethod = clazz.getMethod("getCode");
						Object value = getCodeMethod.invoke(object);
						GComment comment = new GComment("xxenum" + field.getName(), comments.get(field.getName()));
						if (!comment.comment.contains(value.toString())) {
							comment.comment = value.toString()
									+ (comment.comment.equals("") ? "" : (", " + comment.comment));
						}
						result.add(comment);
					}
				}
			} catch (Exception e) {
				System.err.println("error: " + clazz.getName());
			}
		} else {
			Field[] fields = getAllFields(clazz);
			for (Field field : fields) {
				// 表示为private类型
				if (field.getModifiers() == 2) {
					GComment comment = new GComment(field.getName(), comments.get(field.getName()));
					if (List.class.isAssignableFrom(field.getType())) {
						Class<?> listActualType = getCollectionFieldActualType(field);
						if (isNotPrimaryType(listActualType) && !listActualType.getName().equals(clazz.getName())) {
							if (!comment.comment.contains(listActualType.getName())) {
								comment.addClassName(listActualType.getName());
							} else {
								comment.addClassName("");
							}
							comment.addList(getComments(listActualType));
						}
					} else if (isNotPrimaryType(field.getType())) {
						if (!field.getType().getName().equals(clazz.getName()) && field.getType() != Map.class) {
							if (!comment.comment.contains(field.getType().getName())) {
								comment.addClassName(field.getType().getName());
							} else {
								comment.addClassName("");
							}
							comment.addList(getComments(field.getType()));
						}
					} else {
						// 如果为基本类型，查看其注释后面有没有@link，如果有表示为枚举或多对象类型，取出其的注解
						String enumComment = comments.get(field.getName());
						if (GUtils.isNotEmpty(enumComment) && enumComment.contains("@link")) {
							Pattern pattern = Pattern.compile("\\{@link (.*)\\}");
							Matcher matcher = pattern.matcher(enumComment);
							if (matcher.find()) {
								String linkName = matcher.group(1);
								// 如果有多个@link
								if (linkName.contains("@link")) {
									String[] multiClass = linkName.split("\\}.*?\\{@link ");
									for (String className : multiClass) {
										comment.addClassName(className);
										try {
											comment.addList(getComments(Class.forName(className)));
										} catch (Exception e) {
											System.err.println(
													"1class not found: in " + clazz.getName() + " " + className);
										}
									}
								} else {
									try {
										linkName = linkName.replace("}", "");
										if (!comment.comment.contains(linkName)) {
											comment.addClassName(linkName);
										} else {
											comment.addClassName("");
										}
										if ("com.everhomes.rest.organization.organizationType".equals(linkName)) {
											linkName = "com.everhomes.rest.organization.OrganizationType";
										}else if ("com.everhomes.rest.ui.user.sceneType".equals(linkName)) {
											linkName = "com.everhomes.rest.ui.user.SceneType";
										}
										comment.addList(getComments(Class.forName(linkName)));
									} catch (Exception e) {
										System.err.println("2class not found: in " + clazz.getName() + " " + linkName);
									}
								}

							}
						}
					}
					result.add(comment);
				}
			}
		}

		return result;
	}

	public static boolean isNotPrimaryType(Class<?> fieldType) {
		return !fieldType.isPrimitive() && fieldType != String.class
				&& !Date.class.isAssignableFrom(fieldType) && Timestamp.class != fieldType
				&& !Number.class.isAssignableFrom(fieldType) && !Enum.class.isAssignableFrom(fieldType);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> getComments(String classname) {
		Object cache = CACHE.get(classname);
		if (cache != null && cache instanceof Map) {
			return (Map<String, String>) cache;
		}
		Map<String, String> map = new HashMap<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(getFilePath(classname))));
			String temp;
			while ((temp = reader.readLine()) != null) {
				temp.replace("：", ":");
				if (temp.contains("</ul>")) {
					CACHE.put(classname, map);
					return map;
				}
				if (temp.contains("<li>")) {
					temp = temp.replace("<li>", "").replace("</li>", "").replace("*", "").replace("\n", "")
							.replaceAll(" ", "").replace("@link", "@link ").replace("：", ":");
					if (isNotEmpty(temp) && temp.indexOf(":") > -1) {
						map.put(temp.substring(0, temp.indexOf(":")).trim().replaceAll("（", "(").replaceAll("）", ")")
								.replaceAll("\\(.*\\)", ""), temp.substring(temp.indexOf(":") + 1).trim());
						continue;
					}
				}
			}
		} catch (Exception e) {
			System.err.println("file not found: " + getFilePath(classname));
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public static String getFilePath(String className) {
		try {
			String postfix = "\\src\\main\\java\\" + className.replaceAll("\\.", "\\\\") + ".java";
			if (className.endsWith("Controller")) {
				return new File("").getCanonicalPath() + postfix;
			}
			return new File("").getCanonicalPath().replace("ehcore", "ehrest") + postfix;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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

	public static boolean isNotEmpty(Object object) {
		return !isEmpty(object);
	}

	public static boolean isEmpty(Object object) {
		if (object == null) {
			return true;
		}
		if (object instanceof String) {
			return ((String) object).length() == 0;
		}
		if (object instanceof List<?>) {
			return ((List<?>) object).size() == 0;
		}
		if (object instanceof Map<?, ?>) {
			return ((Map<?, ?>) object).keySet().isEmpty();
		}

		return false;
	}

	private static List<GInterface> getMethodInfo(Class<?> clazz) {
		List<GInterface> result = new ArrayList<>();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.getAnnotation(RequestMapping.class) != null
					&& method.getAnnotation(SuppressDiscover.class) == null) {
				GInterface in = new GInterface();
				in.requestMappingName = method.getAnnotation(RequestMapping.class).value()[0];
				in.methodName = method.getName();
				if (method.getParameterTypes() != null) {
					in.commandComment = "<ul>\n";
					Class<?>[] paramTypes = method.getParameterTypes();
					for (Class<?> paramType : paramTypes) {
						if (paramType.getName().endsWith("Command")
								|| paramType.getName().toUpperCase().endsWith("DTO")) {
							GClass gc = new GClass(paramType);
							in.commandComment += gc.toString();
							in.commandSetter = gc.getCommandSetter();
						} else {
							in.commandComment += "<li>" + paramType.getName() + "</li>\n";
						}
					}
					in.commandComment = in.commandComment + "</ul>";
				}
				RestReturn restReturn = null;
				if ((restReturn = method.getAnnotation(RestReturn.class)) != null
						&& restReturn.value() != String.class && GUtils.isNotPrimaryType(restReturn.value())) {
					in.responseComment = "<ul>\n" + new GClass(restReturn.value()).toString() + "</ul>";

				}
				result.add(in);
			}
		}
		return result;
	}

	public static List<GInterface> getInterfaces(final Class<?> clazz) {
		String clazzRequestMappingName = clazz.getAnnotation(RequestMapping.class).value()[0];
		List<GInterface> list = getMethodInfo(clazz);
		List<GInterface> resultList = new ArrayList<>();

		final String content = getFileContent(getFilePath(clazz.getName()));
		list.forEach(g -> {
			Pattern pattern = Pattern
					.compile("(/\\*\\*[^@]*?\\*/)[^\\*]*" + g.requestMappingName + "[^\\*]*" + g.methodName + "\\(");
			Matcher matcher = pattern.matcher(content);
			if (matcher.find()) {
				String comment = matcher.group(1);

				// Pattern pattern1 = Pattern.compile("<b>(.*)</b>");
				// Matcher matcher1 = pattern1.matcher(comment);
				// if (matcher1.find()) {
				// g.url = matcher1.group(1);
				// }

				Pattern pattern2 = Pattern.compile("<p>(.*)</p>");
				matcher = pattern2.matcher(comment);
				if (matcher.find()) {
					g.description = matcher.group(1).replaceAll(" ", "").replaceAll("\\*", "");
				}

			}

			g.url = ("URL:" + clazzRequestMappingName + "/" + g.requestMappingName).replace("//", "/");
			
			// if (g.description.indexOf(".") > 0) {
			resultList.add(g);
			// }
		});

		return resultList;
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

	public static void writeFile(String content, boolean append) {
		String path;
		try {
			path = new File("").getCanonicalPath() + "\\src\\test\\java\\com\\everhomes\\generate\\docs.html";
			createFile(path, content, append);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void createFile(String path, String content, boolean append) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(file, append);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String underlineToCamel(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (c == '_') {
				if (++i < len) {
					sb.append(Character.toUpperCase(param.charAt(i)));
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static Class<?>[] getAllControllers() {
		List<Class<?>> clazzes = new ArrayList<>();
		try {
			String path = new File("").getCanonicalPath() + "\\src\\main\\java";
			
			List<String> controllerPaths = getAllControllerFilePath(path);
			
			controllerPaths.forEach(c->{
				String className = c.substring(c.indexOf("com")).replaceAll("\\/", "\\.").replaceAll("\\\\", "\\.").replace(".java", "");
				try {
					clazzes.add(Class.forName(className));
				} catch (Exception e) {
					System.err.println("get all controller error, not found class: "+className);
				}
			});
			return clazzes.toArray(new Class<?>[controllerPaths.size()]);
		} catch (Exception e) {
			System.err.println("get all controller error");
		}
		return null;
	}

	public static List<String> getAllControllerFilePath(String filePath) {
		List<String> filePaths = new ArrayList<>();
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				filePaths.addAll(getAllControllerFilePath(file.getAbsolutePath()));
			} else {
				if (file.getAbsolutePath().endsWith("Controller.java")) {
					filePaths.add(file.getAbsolutePath());
				}
			}
		}
		return filePaths;
	}
}
