// @formatter:off
package com.everhomes.test.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.jooq.Table;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhNamespaceResources;
import com.everhomes.test.core.base.BaseServerTestCase;
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
    
    @Ignore @Test
    public void prepareDemoJsonData() {
        /*
         * 执行注意事项：
         * 1、先找个数据库有数据的，然后开通一个访问权限（使用下面语句，需要修改数据库名、用户名、密码）
         *    GRANT ALL ON dbname.* to user_name@`%` identified by 'password';
         *    GRANT process ON dbname.* TO user_name@`%`;
         *    flush privileges;
         * 2、先修改ehtest-integration/src/test/config/ehcore.properties里的数据库配置，按上面开通权限信息配置；
         * 3、修改本方法最后一句里的输出文件路径；
         * 4、执行本方法得到数据；
         * 5、注意把ehcore.properties的信息修改回来，以免在测试时把有数据的库都truncate掉了
         */
        Map<String, Object> map = new HashMap<String, Object>();
        DSLContext dslContext = dbProvider.getDslContext();
        for(Table<?> r : dslContext.meta().getTables()) {
            if(!r.getName().toLowerCase().startsWith("eh_")) {
                continue;
            }
            SelectQuery<?> query = dslContext.selectQuery(r);
            query.addLimit(2);
            Result<?> records = query.fetch();
            
            map.put(r.getName(), dbProvider.convertRecordToMap(records));
        }
        
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

