// @formatter:off
package com.everhomes.test.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhNamespaceResources;
import com.everhomes.test.core.BaseServerTestCase;
import com.everhomes.test.core.persist.DbProvider;
import com.everhomes.test.core.util.GsonHelper;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class PrepareDataTest extends BaseServerTestCase {
    @Autowired
    private DbProvider dbProvider; 
    
    /**
     * <p>先找一个有数据的数据库，把配置文件的数据库改为指定数据库，把指定的数据查出来转化为JSON字符串，并格式化写到一个文件上</p>
     */
    @Ignore @Test
    public void prepareJsonData() {
        List<Map<String, Object>> sceneList = dbProvider.convertRecordToMap(Tables.EH_SCENE_TYPES, null);
        
        Condition condition = Tables.EH_NAMESPACE_DETAILS.ID.lessThan(1003L);
        List<Map<String, Object>> nsDetailList = dbProvider.convertRecordToMap(Tables.EH_NAMESPACE_DETAILS, condition);
        
        /*
         * 数据格式：
         * 1、 一个文件就是一个整体的JSON对象；
         * 2、JSON对象的第一层Key就是表名，与数据库的名称对应；
         * 3、JSON对象第一层Key对应的值可以是一个JSON对象（一行记录），也可以是JSONArray对象（多行记录）； 
         */
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Tables.EH_SCENE_TYPES.getName(), sceneList);
        map.put(Tables.EH_NAMESPACE_DETAILS.getName(), nsDetailList);
        String jsonString = StringHelper.toJsonString(map);
        String formatString = GsonHelper.formatJsonString(jsonString);
        GsonHelper.writeTextFile("d:/abcd.txt", formatString);
    }
    
    /**
     * 导入数据
     */
    @Ignore @Test
    public void loadData() {
        String csFilePath = "data/demo/3.4.x-test-data-001.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(csFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, true);
    }
    
    @Ignore @Test
    public void queryData() {
        DSLContext dslContext = dbProvider.getDslContext();
        List<EhNamespaceResources> nsResources = new ArrayList<EhNamespaceResources>();
        dslContext.selectFrom(Tables.EH_NAMESPACE_RESOURCES).where(Tables.EH_NAMESPACE_RESOURCES.ID.eq(1L)).fetch().map((r) -> {
            nsResources.add(ConvertHelper.convert(r, EhNamespaceResources.class));
            return null;
        });
    }
}

