package com.everhomes.generate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class GenerateApi { 
	private static final String FTL_PATH = "E:/workspace/ehnextgen/ehcore/src/test/java/com/everhomes/generate/ftl";
	private static final String EH_CORE_API_PROJECT_PATH = "E:/workspace/ehnextgen/ehnextgen-api";
	private static final String EH_CORE_PROJECT_PATH = "E:/workspace/ehnextgen/ehcore";
	private static final String EH_REST_PROJECT_PATH = "E:/workspace/ehnextgen/ehrest";
	private static final String TARGET_PACKAGE = "com.everhomes.talent2";
	private static final String TARGET_REST_PACKAGE = "com.everhomes.rest.talent2";
	private static final String TARGET_CONTROLLER = "Talent2Controller";
	private static final String TARGET_SERVICE = "Talent2Service";
	private static final String CONTROLLER_MAPPING = "/talent2";

	public static void main(String[] args) { 
		String[] apis = {
//				"listContracts(1.合同列表);buildingName(String|楼栋名称),keywords(String|查询关键词),pageAnchor(Long|锚点),pageSize(Integer|每页大小);nextPageAnchor(Long|下页锚点),contracts(List<ContractDTO>|合同列表，参考{@link com.everhomes.rest.contract.ContractDTO})"
				"listTalentCateogry(1.分类列表);ownerType(String|所属类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}),ownerId(Long|所属id),organizationId(Long|管理公司id);talentCategories(List<TalentCategoryDTO>|分类列表，参考{@link com.everhomes.rest.talent.TalentCategoryDTO})",
				"createOrUpdateTalentCateogry(2.新增或更新分类);ownerType(String|所属类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}),ownerId(Long|所属id),organizationId(Long|管理公司id),talentCategories(List<TalentCategoryDTO>|分类列表，参考{@link com.everhomes.rest.talent.TalentCategoryDTO});",
				"deleteTalentCateogry(3.删除分类);ownerType(String|所属类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}),ownerId(Long|所属id),organizationId(Long|管理公司id),id(Long|id);",
				
				"listTalent(4.人才列表);ownerType(String|所属类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}),ownerId(Long|所属id),organizationId(Long|管理公司id),categoryId(Long|分类id),gender(Byte|性别，参考{@link com.everhomes.rest.user.UserGender}),experience(Byte|经验，参考{@link com.everhomes.rest.talent.TalentExperienceConditionEnum}),degree(Byte|学历，参考{@link com.everhomes.rest.talent.TalentDegreeConditionEnum}),keyword(String|关键词),pageAnchor(Long|锚点),pageSize(Integer|每页大小);nextPageAnchor(Long|下页锚点),talents(List<TalentDTO>|人才列表，参考{@link com.everhomes.rest.talent.TalentDTO})",
				"createOrUpdateTalent(5.更新人才信息);ownerType(String|所属类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}),ownerId(Long|所属id),organizationId(Long|管理公司id),id(Long|id),name(String|姓名),avatarUri(String|头像),position(String|职位),categoryId(Long|分类id),gender(Byte|性别，参考{@link com.everhomes.rest.user.UserGender}),experience(Integer|经验年数),graduateSchool(String|毕业院校),degree(Byte|学历，参考{@link com.everhomes.rest.talent.TalentDegreeEnum}),phone(String|联系电话),remark(String|详细介绍);",
				"enableTalent(6.打开/关闭人才信息);ownerType(String|所属类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}),ownerId(Long|所属id),organizationId(Long|管理公司id),id(Long|id),enabled(Byte|1是0否，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag});",
				"deleteTalent(7.删除人才信息);ownerType(String|所属类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}),ownerId(Long|所属id),organizationId(Long|管理公司id),id(Long|id);",
				"topTalent(8.置顶人才信息);ownerType(String|所属类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}),ownerId(Long|所属id),organizationId(Long|管理公司id),id(Long|id);",
				"importTalent(9.导入人才信息);ownerType(String|所属类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}),ownerId(Long|所属id),organizationId(Long|管理公司id),attachment(MultipartFile[]|文件);",
				"getTalentDetail(10.获取人才信息详情);ownerType(String|所属类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}),ownerId(Long|所属id),organizationId(Long|管理公司id),id(Long|id);talent(TalentDTO|人才信息)",
				
				"listTalentQueryHistory(11.人才信息查询记录);ownerType(String|所属类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}),ownerId(Long|所属id),organizationId(Long|管理公司id),id(Long|id);talentQueryHistories(List<TalentQueryHistoryDTO>|历史记录)"
//				"deleteTalentQueryHistory(12.删除人才信息查询记录);ownerType(String|所属类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}),ownerId(Long|所属id),organizationId(Long|管理公司id),id(Long|id);",
//				"clearTalentQueryHistory(13.清空人才信息查询记录);ownerType(String|所属类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}),ownerId(Long|所属id),organizationId(Long|管理公司id);",
				
				
				
				};

//		System.out.println(apis.length);
		generate(apis);
	}

	private static void generate(String[] apis) {
		for (String api : apis) {
			GApi gApi = fetch(api);
			generateController(gApi);
			generateService(gApi);
			generateServiceImpl(gApi);
			generateCommand(gApi);
			generateResponse(gApi);
		}
	}

	private static GApi fetch(String api) {
		api = api.trim().replace(") ;", ");").replace(") ,", "),");
		GApi gApi = new GApi();

		String[] parts = api.split("\\);");
		if (parts.length == 1) {
			gApi.setMethod(fetchMethod(parts[0]));
		} else if (parts.length == 2) {
			gApi.setMethod(fetchMethod(parts[0]));
			gApi.setParams(fetchParams(parts[1]));
		} else {
			gApi.setMethod(fetchMethod(parts[0]));
			gApi.setParams(fetchParams(parts[1]));
			gApi.setResults(fetchParams(parts[2]));
		}

		return gApi;
	}

	private static GAMethod fetchMethod(String str) {
		GAMethod method = new GAMethod();
		method.setName(str.substring(0, str.indexOf("(")));
		method.setDesc(str.substring(str.indexOf("(") + 1));
		return method;
	}

	private static List<GAParam> fetchParams(String str) {
		List<GAParam> params = new ArrayList<>();
		if (StringUtils.isBlank(str)) {
			return params;
		}

		String[] parts = str.split("\\),");
		for (String string : parts) {
			if (StringUtils.isNotBlank(string)) {
				GAParam param = new GAParam();
				param.setName(string.substring(0, string.indexOf("(")));
				param.setType(string.substring(string.indexOf("(") + 1, string.indexOf("|")));
				param.setDesc(string.substring(string.indexOf("|") + 1));
				if (param.getDesc().endsWith(")")) {
					param.setDesc(param.getDesc().substring(0, param.getDesc().length()-1));
				}
				params.add(param);
			}
		}

		return params;
	}

	private static void generateController(GApi gApi) {
		if (!checkFileExist(getControllerPath())) {
			Map<String, Object> map = new HashMap<>();
			map.put("targetPackage", TARGET_PACKAGE);
			map.put("controllerMapping", CONTROLLER_MAPPING);
			map.put("targetController", TARGET_CONTROLLER);
			map.put("targetService", TARGET_SERVICE);
			generateFile("controller.ftl", getControllerPath(), map);
			System.out.println("生成controller完毕！");
		}

		String methodContent = generateControllerMethod(gApi);
		writeContentToFile(getControllerPath(), methodContent);
	}

	private static String generateControllerMethod(GApi gApi) {
		Map<String, Object> map = new HashMap<>();
		map.put("methodName", gApi.getMethod().getName());
		map.put("methodDesc", gApi.getMethod().getDesc());
		map.put("controllerMapping", CONTROLLER_MAPPING);
		if (gApi.getParams() != null && gApi.getParams().size() > 0) {
			map.put("hasCommand", "1");
		}
		if (gApi.getResults() != null && gApi.getResults().size() > 0) {
			map.put("hasResponse", "1");
		}
		map.put("serviceName", TARGET_SERVICE);

		return generateString("controllerMethod.ftl", map);
	}

	private static void generateService(GApi gApi) {
		if (!checkFileExist(getServicePath())) {
			Map<String, Object> map = new HashMap<>();
			map.put("targetPackage", TARGET_PACKAGE);
			map.put("targetService", TARGET_SERVICE);
			generateFile("service.ftl", getServicePath(), map);
			System.out.println("生成service完毕！");
		}

		String methodContent = generateServiceMethod(gApi);
		writeContentToFile(getServicePath(), methodContent);
	}

	private static String generateServiceMethod(GApi gApi) {
		Map<String, Object> map = new HashMap<>();
		if (gApi.getParams() != null && gApi.getParams().size() > 0) {
			map.put("hasCommand", "1");
		}
		if (gApi.getResults() != null && gApi.getResults().size() > 0) {
			map.put("hasResponse", "1");
		}
		map.put("method", objectToMap(gApi.getMethod()));
		return generateString("serviceMethod.ftl", map);
	}

	private static void generateServiceImpl(GApi gApi) {
		if (!checkFileExist(getServiceImplPath())) {
			Map<String, Object> map = new HashMap<>();
			map.put("targetPackage", TARGET_PACKAGE);
			map.put("targetService", TARGET_SERVICE);
			generateFile("serviceImpl.ftl", getServiceImplPath(), map);
			System.out.println("生成service完毕！");
		}

		String methodContent = generateServiceImplMethod(gApi);
		writeContentToFile(getServiceImplPath(), methodContent);
	}

	private static String generateServiceImplMethod(GApi gApi) {
		Map<String, Object> map = new HashMap<>();
		if (gApi.getParams() != null && gApi.getParams().size() > 0) {
			map.put("hasCommand", "1");
		}
		if (gApi.getResults() != null && gApi.getResults().size() > 0) {
			map.put("hasResponse", "1");
		}
		map.put("method", objectToMap(gApi.getMethod()));
		map.put("isImpl", "1");

		return generateString("serviceMethod.ftl", map);
	}

	private static void generateCommand(GApi gApi) {
		Map<String, Object> map = new HashMap<>();
		if (gApi.getParams() != null && !gApi.getParams().isEmpty()) {
			for (GAParam gAParam : gApi.getParams()) {
				if (gAParam.getType().startsWith("List<")) {
					map.putIfAbsent("hasList", "1");
				}else if (gAParam.getType().startsWith("BigDecimal")) {
					map.putIfAbsent("hasDecimal", "1");
				}
			}
			map.put("targetRestPackage", TARGET_REST_PACKAGE);
			map.put("isParam", "1");
			map.put("fields", objectToMap(gApi.getParams()));
			map.put("modelName", capFirst(gApi.getMethod().getName()) + "Command");
			generateFile("model.ftl", getCommandPath(gApi.getMethod().getName()), map);
		}
	}

	private static void generateResponse(GApi gApi) {
		Map<String, Object> map = new HashMap<>();
		if (gApi.getResults() != null && !gApi.getResults().isEmpty()) {
			for (GAParam gAParam : gApi.getResults()) {
				if (gAParam.getType().startsWith("List<")) {
					map.putIfAbsent("hasList", "1");
				}else if (gAParam.getType().startsWith("BigDecimal")) {
					map.putIfAbsent("hasDecimal", "1");
				}
			}
			map.put("targetRestPackage", TARGET_REST_PACKAGE);
			map.put("fields", objectToMap(gApi.getResults()));
			map.put("modelName", capFirst(gApi.getMethod().getName()) + "Response");
			generateFile("model.ftl", getResponsePath(gApi.getMethod().getName()), map);
		}
		
	}

	private static String getControllerPath() {
		return EH_CORE_PROJECT_PATH + "/src/main/java/" + TARGET_PACKAGE.replace(".", "/") + "/" + TARGET_CONTROLLER
				+ ".java";
	}

	private static String getServicePath() {
		return EH_CORE_API_PROJECT_PATH + "/src/main/java/" + TARGET_PACKAGE.replace(".", "/") + "/" + TARGET_SERVICE
				+ ".java";
	}

	private static String getServiceImplPath() {
		return EH_CORE_PROJECT_PATH + "/src/main/java/" + TARGET_PACKAGE.replace(".", "/") + "/" + TARGET_SERVICE
				+ "Impl.java";
	}

	private static String getCommandPath(String methodName) {
		return EH_REST_PROJECT_PATH + "/src/main/java/" + TARGET_REST_PACKAGE.replace(".", "/") + "/"
				+ capFirst(methodName) + "Command.java";
	}

	private static String getResponsePath(String methodName) {
		return EH_REST_PROJECT_PATH + "/src/main/java/" + TARGET_REST_PACKAGE.replace(".", "/") + "/"
				+ capFirst(methodName) + "Response.java";
	}

	private static boolean checkFileExist(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public static void generateFile(String templateName, String path, Object dataModel) {
		Configuration configuration = new Configuration();
		try {
			// 1. 设置模板文件所在目录
			configuration.setDirectoryForTemplateLoading(new File(FTL_PATH));
			configuration.setObjectWrapper(new DefaultObjectWrapper());

			// 2. 获取模板文件
			Template template = configuration.getTemplate(templateName,"utf-8");

			// 3. 合并模板和数据模型（输出到文件）
			Writer out = new FileWriter(new File(path));
			template.process(dataModel, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	@SuppressWarnings("deprecation")
	public static String generateString(String templateName, Object dataModel) {
		Configuration configuration = new Configuration();
		try {
			// 1. 设置模板文件所在目录
			configuration.setDirectoryForTemplateLoading(new File(FTL_PATH));
			configuration.setObjectWrapper(new DefaultObjectWrapper());

			// 2. 获取模板文件
			Template template = configuration.getTemplate(templateName,"utf-8");

			// 4. 合并模板和数据模型（输出到文件）
			Writer out = new StringWriter();
			template.process(dataModel, out);
			out.flush();
			out.close();

			return out.toString();
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	private static void writeContentToFile(String filePath, String content) {
		try {
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(filePath, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length() - 2;
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.write(("\n" + content + "\n\n}").getBytes());
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String capFirst(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static String uncapFirst(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	} 
	
	public static Object objectToMap(Object obj){
		if (obj instanceof List) {
			return JSON.parseArray(JSON.toJSONString(obj));
		}
		return JSON.parseObject(JSON.toJSONString(obj));
	}

}

class GApi {
	private GAMethod method;
	private List<GAParam> params;
	private List<GAParam> results;

	public GAMethod getMethod() {
		return method;
	}

	public void setMethod(GAMethod method) {
		this.method = method;
	}

	public List<GAParam> getParams() {
		return params;
	}

	public void setParams(List<GAParam> params) {
		this.params = params;
	}

	public List<GAParam> getResults() {
		return results;
	}

	public void setResults(List<GAParam> results) {
		this.results = results;
	}

}

class GAMethod {
	private String name;
	private String desc;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}

class GAParam {
	private String name;
	private String type;
	private String desc;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}