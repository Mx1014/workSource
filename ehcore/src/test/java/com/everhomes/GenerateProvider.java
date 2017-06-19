package com.everhomes.generate;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class GenerateProvider { 
	private static final String FTL_PATH = "E:/workspace/ehnextgen/ehcore/src/test/java/com/everhomes/generate/ftl";
	private static final String EH_CORE_API_PROJECT_PATH = "E:/workspace/ehnextgen/ehnextgen-api";
	private static final String EH_CORE_PROJECT_PATH = "E:/workspace/ehnextgen/ehcore";
	private static final String PROVIDER_PACKAGE = "com.everhomes.talent2";
	
	private static final Random RANDOM = new Random();
	
	public static void main(String[] args) {
		//请在此处填写要生成的provider，直接填写表名即可
		
//		String[] entities = {"eh_express_service_addresses","eh_express_companies","eh_express_users","eh_express_addresses","eh_express_orders","eh_express_query_histories"};
		String[] entities = {"eh_talents"};
		  
		for (String entity : entities) {
			generateAll(underlineToCamel(entity.replace("eh_", "").toLowerCase()));
		} 
	}
	
	private static void generateAll(String entityName){
		if (entityName == null || entityName.length() == 0) {
			return;
		}
		generateProvider(entityName);
		generateProviderImpl(entityName);
		generateEntity(entityName);
	}
	
	private static void generateProvider(String entityName){
		Map<String, String> map = new HashMap<>();
		map.put("packageName", PROVIDER_PACKAGE);
		map.put("entityName", entityName);
		generate("provider.ftl", getProviderFilePath(entityName), map);
	}
	
	private static void generateProviderImpl(String entityName){
		Map<String, String> map = new HashMap<>();
		map.put("packageName", PROVIDER_PACKAGE);
		map.put("entityName", entityName);
		map.put("underlineCapEntityName", camelToUnderline(entityName).toUpperCase());
		generate("providerImpl.ftl", getProviderImplFilePath(entityName), map);
	}
	
	private static void generateEntity(String entityName){
		Map<String, String> map = new HashMap<>();
		map.put("packageName", PROVIDER_PACKAGE);
		map.put("entityName", entityName);
		map.put("serialNumber", String.valueOf(RANDOM.nextLong()));
		generate("entity.ftl", getEntityFilePath(entityName), map);
	}

	@SuppressWarnings("deprecation")
	private static void generate(String templateName, String filePath, Object dataModel){
		Configuration configuration = new Configuration();
		try {
			configuration.setDirectoryForTemplateLoading(new File(FTL_PATH));
			configuration.setObjectWrapper(new DefaultObjectWrapper());
			
			Template template = configuration.getTemplate(templateName);
			
			Writer out = new FileWriter(new File(filePath));
			template.process(dataModel, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	private static String getProviderFilePath(String entityName){
		return EH_CORE_API_PROJECT_PATH+"/src/main/java/"+PROVIDER_PACKAGE.replace(".", "/")+"/"+capFirst(formatEntityName(entityName))+"Provider.java";
	}
	
	private static String getEntityFilePath(String entityName){
		return EH_CORE_API_PROJECT_PATH+"/src/main/java/"+PROVIDER_PACKAGE.replace(".", "/")+"/"+capFirst(formatEntityName(entityName))+".java";
	}
	
	private static String getProviderImplFilePath(String entityName){
		
		return EH_CORE_PROJECT_PATH+"/src/main/java/"+PROVIDER_PACKAGE.replace(".", "/")+"/"+capFirst(formatEntityName(entityName))+"ProviderImpl.java";
	}
	
	private static String formatEntityName(String entityName){
		if (entityName.endsWith("s")) {
			entityName = entityName.substring(0, entityName.length()-1);
		}
		if (entityName.endsWith("ie")) {
			entityName = entityName.substring(0, entityName.length()-2)+"y";
		}
		if (entityName.endsWith("sse")) {
			entityName = entityName.substring(0, entityName.length()-1);
		}
		return entityName;
	}
	
	private static String camelToUnderline(String param) {
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
	
	private static String capFirst(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
}
