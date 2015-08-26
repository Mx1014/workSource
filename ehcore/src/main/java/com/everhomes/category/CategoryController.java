package com.everhomes.category;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.category.Category;
import com.everhomes.category.CategoryAdminStatus;
import com.everhomes.category.CategoryDTO;
import com.everhomes.category.CategoryProvider;
import com.everhomes.category.ListCategoryCommand;
import com.everhomes.controller.ControllerBase;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.EtagHelper;
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
    private static final String CATEGORY_PATH = "path";
    @Autowired
    private CategoryProvider categoryProvider;    
    
    
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

        List<Category> entityResultList = this.categoryProvider.listChildCategories(cmd.getParentId(),
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
        Tuple[] tuples = new Tuple[2]; 
        tuples[0] = new Tuple<String, SortOrder>(DEFAULT_SORT, SortOrder.ASC);
        tuples[1] = new Tuple<String, SortOrder>(CATEGORY_PATH, SortOrder.ASC);
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
    public RestResponse listInterestCategories(HttpServletRequest request, HttpServletResponse response) {
        
        @SuppressWarnings("rawtypes")
        Tuple[] orderBy = defaultSort();
        @SuppressWarnings("unchecked")
        List<Category> entityResultList = this.categoryProvider.listChildCategories(CategoryConstants.CATEGORY_ID_INTEREST,
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
}
