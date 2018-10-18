package com.everhomes.category;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.category.GetCategoryCommand;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.controller.ControllerBase;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.discover.RestReturn;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.category.ListCategoryCommand;
import com.everhomes.rest.category.ListCategoryV2Command;
import com.everhomes.rest.category.UpdateCategoryLogoUriCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.EtagHelper;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

/**
 * Category REST API controller
 * 
 * @author Kelven Yang
 *
 */
@RestController
@RequestMapping("/category")
public class CategoryController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
	private static final String DEFAULT_SORT = "default_order";
	//    private static final String CATEGORY_PATH = "path";
	//    private static final long CATEOGRY_RECOMMEND = 9999L;
	//    private static final String CATEOGRY_RECOMMEND_NAME = "推荐";
	@Autowired
	private CategoryProvider categoryProvider;    
	@Autowired 
	private ContentServerService contentServerService;


	/**
	 * <b>URL: /category/listAllCategories</b> 
	 * <p>列出所有支持的类型</p>
	 */
	@RequireAuthentication(false)
	@RequestMapping("listAllCategories")
	@RestReturn(value = CategoryDTO.class, collection = true)
	public RestResponse listAllCategories(HttpServletRequest request, HttpServletResponse response) {
		List<Category> list = this.categoryProvider.listAllCategories();

		List<CategoryDTO> dtoResultList = list.stream().map(r -> {
			return ConvertHelper.convert(r, CategoryDTO.class);
		}).collect(Collectors.toList());

		if(dtoResultList != null){
			int hashCode = dtoResultList.hashCode();
			if(EtagHelper.checkHeaderEtagOnly(30,hashCode+"", request, response)) {
				return new RestResponse(dtoResultList);
			}
		}
		return new RestResponse();
	}

	/**
	 * <b>URL: /category/listChildren</b> 列出指定类型的第一层孩子类型
	 */
	@SuppressWarnings("unchecked")
	@RequireAuthentication(false)
	@RequestMapping("listChildren")
	@RestReturn(value = CategoryDTO.class, collection = true)
	public RestResponse listChildren(@Valid ListCategoryCommand cmd, HttpServletRequest request, HttpServletResponse response) {
		Tuple[] orderBy = null;
		// 暂不向客户端开放排序字段指定 by lqs 20150505
		// if(cmd.getSortBy() != null)
		// orderBy = new Tuple<String, SortOrder>(cmd.getSortBy(),
		// SortOrder.fromCode(cmd.getSortOrder()));

		if(cmd.getSortOrder() == null){
			orderBy = defaultSort();
		}

		Integer namespaceId = UserContext.getCurrentNamespaceId();
		namespaceId = cmd.getNamespaceId() == null ? namespaceId : cmd.getNamespaceId();
		List<Category> entityResultList = this.categoryProvider.listChildCategories(namespaceId, cmd.getParentId(),
				CategoryAdminStatus.fromCode(cmd.getStatus()), orderBy);

		List<CategoryDTO> dtoResultList = entityResultList.stream().map(r -> {
			return ConvertHelper.convert(r, CategoryDTO.class);
		}).collect(Collectors.toList());

		if(dtoResultList != null){
			int hashCode = dtoResultList.hashCode();
			if(EtagHelper.checkHeaderEtagOnly(30,hashCode+"", request, response)) {
				return new RestResponse(dtoResultList);
			}
		}
		return new RestResponse();
	}
	@SuppressWarnings("rawtypes")
	private Tuple[] defaultSort(){
		Tuple[] tuples = new Tuple[1]; 
		tuples[0] = new Tuple<String, SortOrder>(DEFAULT_SORT, SortOrder.ASC);
		//tuples[1] = new Tuple<String, SortOrder>(CATEGORY_PATH, SortOrder.ASC);
		return tuples;
	}


	/**
	 * <b>URL: /category/listDescendants</b> 列出指定类型下的所有孩子类型
	 */
	@RequireAuthentication(false)
	@RequestMapping("listDescendants")
	@RestReturn(value = CategoryDTO.class, collection = true)
	public RestResponse listDescendants(@Valid ListCategoryCommand cmd, HttpServletRequest request, HttpServletResponse response) {
		Tuple<String, SortOrder> orderBy = null;
		// 暂不向客户端开放排序字段指定 by lqs 20150505
		// if(cmd.getSortBy() != null)
		// orderBy = new Tuple<String, SortOrder>(cmd.getSortBy(),
		// SortOrder.fromCode(cmd.getSortOrder()));

		@SuppressWarnings("unchecked")
		List<Category> entityResultList = this.categoryProvider.listDescendantCategories(cmd.getParentId(),
				CategoryAdminStatus.fromCode(cmd.getStatus()), orderBy);

		List<CategoryDTO> dtoResultList = entityResultList.stream().map(r -> {
			return ConvertHelper.convert(r, CategoryDTO.class);
		}).collect(Collectors.toList());

		if(dtoResultList != null){
			int hashCode = dtoResultList.hashCode();
			if(EtagHelper.checkHeaderEtagOnly(30,hashCode+"", request, response)) {
				return new RestResponse(dtoResultList);
			}
		}
		return new RestResponse();
	}




	/**
	 * <b>URL: /category/listRoot</b> 列出所有的大类
	 */
	@RequireAuthentication(false)
	@RequestMapping("listRoot")
	@RestReturn(value = CategoryDTO.class, collection = true)
	public RestResponse listRoot(HttpServletRequest request, HttpServletResponse response) {
		List<Category> categories = this.categoryProvider.listRootCategories();
		List<CategoryDTO> convertCategories = categories.stream().map(item -> {
			return ConvertHelper.convert(item, CategoryDTO.class);
		}).collect(Collectors.toList());

		if(convertCategories != null){
			int hashCode = convertCategories.hashCode();
			if(EtagHelper.checkHeaderEtagOnly(30,hashCode+"", request, response)) {
				return new RestResponse(convertCategories);
			}
		}
		return new RestResponse();
	}

	/**
	 * <b>URL: /category/listInterestCategories</b> 列出所有兴趣分类，客户端建圈使用
	 */
	@RequireAuthentication(false)
	@RequestMapping("listInterestCategories")
	@RestReturn(value = CategoryDTO.class, collection = true)
	public RestResponse listInterestCategories(ListCategoryV2Command cmd, HttpServletRequest request, HttpServletResponse response) {
	    LOGGER.info("listInterestCategories, cmd=" + cmd);
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		@SuppressWarnings("rawtypes")
		Tuple[] orderBy = defaultSort();
		@SuppressWarnings("unchecked")
		List<Category> entityResultList = this.categoryProvider.listChildCategories(namespaceId, CategoryConstants.CATEGORY_ID_INTEREST,
				CategoryAdminStatus.ACTIVE, orderBy);

		List<CategoryDTO> dtoResultList = entityResultList.stream().map(r -> {
			return ConvertHelper.convert(r, CategoryDTO.class);
		}).collect(Collectors.toList());

		if(dtoResultList != null){
			int hashCode = dtoResultList.hashCode();
			if(EtagHelper.checkHeaderEtagOnly(30,hashCode+"", request, response)) {
				return new RestResponse(dtoResultList);
			}
		}
		return new RestResponse();
	}

	/**
	 * <b>URL: /category/listContentCategories</b> 
	 * 获取帖子操作分类列表
	 */
	@RequireAuthentication(false)
	@RequestMapping("listContentCategories")
	@RestReturn(value = CategoryDTO.class, collection = true)
	public RestResponse listContentCategories(HttpServletRequest request, HttpServletResponse response) {

		List<Category> entityResultList = this.categoryProvider.listContentCategories();
		List<CategoryDTO> dtoResultList = entityResultList.stream().map(r -> {
			return ConvertHelper.convert(r, CategoryDTO.class);
		}).collect(Collectors.toList());

		if(dtoResultList != null){
			int hashCode = dtoResultList.hashCode();
			if(EtagHelper.checkHeaderEtagOnly(30,hashCode+"", request, response)) {
				return new RestResponse(dtoResultList);
			}
		}
		return new RestResponse();
	}

	/**
	 * <b>URL: /category/listActionCategories</b> 
	 * 根据帖子操作分类获取具体分类
	 */
	@RequireAuthentication(false)
	@RequestMapping("listActionCategories")
	@RestReturn(value = CategoryDTO.class, collection = true)
	public RestResponse listContentCategories(ListCategoryCommand cmd, HttpServletRequest request, HttpServletResponse response) {
		if(cmd.getParentId() == null)
			cmd.setParentId(1l);
		List<Category> entityResultList = this.categoryProvider.listActionCategories(cmd.getParentId());

		List<CategoryDTO> dtoResultList = entityResultList.stream().map(r -> {
			return ConvertHelper.convert(r, CategoryDTO.class);
		}).collect(Collectors.toList());

		if(dtoResultList != null){
			int hashCode = dtoResultList.hashCode();
			if(EtagHelper.checkHeaderEtagOnly(30,hashCode+"", request, response)) {
				return new RestResponse(dtoResultList);
			}
		}
		return new RestResponse();
	}


	/**
	 * <b>URL: /category/listBusinessCategories</b> 列出商家分类列表
	 */
	@SuppressWarnings("unchecked")
	@RequireAuthentication(false)
	@RequestMapping("listBusinessCategories")
	@RestReturn(value = CategoryDTO.class, collection = true)
	public RestResponse listBusinessCategories(HttpServletRequest request, HttpServletResponse response) {
		User user = UserContext.current().getUser();
		long userId = user.getId();

		Integer namespaceId = UserContext.getCurrentNamespaceId();
		Tuple[] orderBy = defaultSort();
		//暂时去掉
		//        Category category = new Category();
		//        category.setParentId(CategoryConstants.CATEGORY_ID_SERVICE);
		//        category.setId(CATEOGRY_RECOMMEND);
		//        category.setName(CATEOGRY_RECOMMEND_NAME);
		//        category.setStatus(CategoryAdminStatus.ACTIVE.getCode());
		List<Category> entityResultList = new ArrayList<Category>();
		//entityResultList.add(category);
		List<Category> result = this.categoryProvider.listChildCategories(namespaceId, CategoryConstants.CATEGORY_ID_SERVICE,CategoryAdminStatus.ACTIVE, orderBy);
		if(result==null||result.isEmpty())
			result = this.categoryProvider.listChildCategories(0, CategoryConstants.CATEGORY_ID_SERVICE,CategoryAdminStatus.ACTIVE, orderBy);
		
		if(result != null && !result.isEmpty())
			entityResultList.addAll(result);
		List<CategoryDTO> dtoResultList = entityResultList.stream().map(r -> {
			CategoryDTO dto = ConvertHelper.convert(r, CategoryDTO.class);

			String logoUri = r.getLogoUri();
			if(!StringUtils.isEmpty(logoUri)){
				if(logoUri.startsWith("testhttp://")){
					dto.setIconUri(logoUri.substring(4));
					dto.setIconUrl(logoUri.substring(4));
				}
				else{
					dto.setIconUri(logoUri);
					dto.setIconUrl(parserUri(logoUri,EntityType.USER.getCode(),userId));
				}
			}

			String des = r.getDescription();
			if(StringUtils.isEmpty(des))
				des = "";
			dto.setDescription(des);
			return dto;
		}).collect(Collectors.toList());

		if(dtoResultList != null){
			int hashCode = dtoResultList.hashCode();
			if(EtagHelper.checkHeaderEtagOnly(30,hashCode+"", request, response)) {
				return new RestResponse(dtoResultList);
			}
		}
		return new RestResponse();
	}

	@RequestMapping("updateCategoryLogoUri")
	@RestReturn(String.class)
	public RestResponse updateCategoryLogoUri(UpdateCategoryLogoUriCommand cmd) {
		this.checkIdIsNull(cmd.getId());
		Category cat = this.checkCategory(cmd.getId(),true);
		cat.setLogoUri(cmd.getLogoUri());
		this.categoryProvider.updateCategory(cat);

		RestResponse response =  new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	private Category checkCategory(Long id,boolean isThrowExcept) {
		Category cat = this.categoryProvider.findCategoryById(id);
		if(cat == null){
			LOGGER.error("Category is not exist.id="+id);
			if(isThrowExcept)
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
						"Category is not exist.");
		}
		return cat;
	}

	private void checkIdIsNull(Long id) {
		if(id == null){
			LOGGER.error("Category id is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Category id is null or empty.");
		}
	}

	private String parserUri(String uri,String ownerType, long ownerId){
		try {
			if(!org.apache.commons.lang.StringUtils.isEmpty(uri))
				return contentServerService.parserUri(uri,ownerType,ownerId);

		} catch (Exception e) {
			LOGGER.error("Parser uri is error." + e.getMessage());
		}
		return null;

	}

	/**
	 * <b>URL: /category/listBusinessSubCategories</b> 列出所有商家子分类
	 */
	@RequestMapping("listBusinessSubCategories")
	@RestReturn(value = CategoryDTO.class, collection = true)
	public RestResponse listBusinessSubCategories() {

		Tuple<String, SortOrder> orderBy = new Tuple<String, SortOrder>(DEFAULT_SORT, SortOrder.ASC);;
		@SuppressWarnings("unchecked")
		List<Category> entityResultList = this.categoryProvider.listBusinessSubCategories(CategoryConstants.CATEGORY_ID_SERVICE,
				CategoryAdminStatus.ACTIVE, orderBy);

		List<CategoryDTO> dtoResultList = entityResultList.stream().map(r -> {
			return ConvertHelper.convert(r, CategoryDTO.class);
		}).collect(Collectors.toList());

		return new RestResponse(dtoResultList);
	}

	/**
	 * <b>URL: /category/getCategory</b>
	 */
	@RequestMapping("getCategory")
	@RestReturn(value = CategoryDTO.class)
	public RestResponse getCategory(GetCategoryCommand cmd) {
        CategoryDTO categoryDTO = new CategoryDTO();
        if (this.categoryProvider.findCategoryById(cmd.getId()) != null) {
            categoryDTO = ConvertHelper.convert(this.categoryProvider.findCategoryById(cmd.getId()), CategoryDTO.class);
        }
		return new RestResponse(categoryDTO);
	}
}
