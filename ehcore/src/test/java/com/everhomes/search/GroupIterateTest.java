// @formatter:off
package com.everhomes.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.forum.Forum;
import com.everhomes.forum.ForumProvider;
import com.everhomes.group.Group;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
import com.everhomes.group.GroupService;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.group.CreateGroupCommand;
import com.everhomes.rest.group.GroupDTO;
import com.everhomes.rest.group.GroupPrivacy;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.user.SendMessageCommand;
import com.everhomes.rest.visibility.VisibilityScope;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserService;
import com.everhomes.user.base.LoginAuthTestCase;
import com.everhomes.user.base.UserGenerator;
import com.everhomes.util.StringHelper;

//public class GroupServiceTest extends UserGenerator {
public class GroupIterateTest extends LoginAuthTestCase {
    
    /** fdfasdf  */
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private GroupProvider groupProvider;
    
    @Autowired
    private GroupService groupService;
    
    @Autowired
    private ForumProvider forumProvider;
    
    @Autowired
    private CategoryProvider categoryProvider;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private GroupSearcher groupSearcher;
    
    @Autowired
    private PostSearcher postSearcher;
    
    @Autowired
    private CommunitySearcher communitySearcher;
    
    private String phone;
    
    private String password;
    
    private long categoryId;
    
    private String[][] groupInfoArray;

    @Configuration
    @ComponentScan(basePackages = {
        "com.everhomes"
    })
    @EnableAutoConfiguration(exclude={
            DataSourceAutoConfiguration.class, 
            HibernateJpaAutoConfiguration.class,
        })
    static class ContextConfiguration {
    }
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }
    
    public void setUp2() throws Exception {
        super.setUp();
        
        phone = "13911112222";
        password = "123456";
        categoryId = 10101;
        
        cleanData();
        long creatorId = createPhoneUser(phone, password);
        
        List<Category> cates = createCategory();
        
        groupInfoArray = new String[][] {
            {"打篮球", "球,篮球,体育馆"},
            {"爬山", "莲花山,组队,踏青"},
            {"音乐", "听,欣赏,天籁"},
            {"悄悄话", "心语,静静"},
            {"育儿", "育儿"},
            {"减肥瘦身", "减肥,瘦身,美体,丰胸"},
            {"风睿轮滑", "轮滑,兴隆,北苑,培训"},
            {"精品水果", "樱桃,草莓,葡萄"},
            {"随性自由", "午夜福利"},
            {"护肤购物", "护肤"},
            {"女人小屋", "女人,小孩,美, 钱"},
            {"萌娃娃", "宝贝教育,亲子互动"},
            {"足球小队", "足球"},
            {"笑着", "美"},
            {"话集锦", "笑话"},
            {"萌宠天地", "宠物,狗狗,猫猫,猫咪,狗,猫,蛇,鼠,萌,可爱,养狗 "},
            {"股票", "股票"},
            {"英皇钢琴", "钢琴"},
            {"开心畅聊", "交友"}
        };
        
        try {
            //logon(phone, password);
            
            for(int i = 0; i < groupInfoArray.length; i++) {
                //createGroup(groupInfoArray[i][0], groupInfoArray[i][1]);
                Group group = new Group();
                //group.setName(groupInfoArray[i][0]);syncDb
                //group.setTag(groupInfoArray[i][1]);
                group.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
                group.setDiscriminator(GroupDiscriminator.GROUP.getCode());
                group.setPrivateFlag(GroupPrivacy.PUBLIC.getCode());
                group.setCreatorUid(creatorId);
                group.setMemberCount(0L);   // initialize it to 0
                
                group.setCategoryId(cates.get(i % 3).getId());
                group.setCategoryPath(cates.get(i % 3).getPath());
                groupProvider.createGroup(group);
                
             // 插点非group数据
                if(i != 0 && (i % 6) == 0) {
                    Group f = new Group();
                    f.setName("家庭 " + i);
                    f.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
                    f.setDiscriminator(GroupDiscriminator.FAMILY.getCode());
                    f.setPrivateFlag(GroupPrivacy.PRIVATE.getCode());
                    f.setCreatorUid(creatorId);
                    f.setMemberCount(0L);   // initialize it to 0
                    groupProvider.createGroup(f);
                }
            }
        } catch(Exception e) {
            Assert.fail(e.toString());
        } finally {
            //logout();
        }
    }
    
    @After
    public void tearDown() {
        //cleanData();
    }
    
    @Test
    public void testIterateGroup() {
        //this.groupSearcher.syncFromDb();
        this.communitySearcher.syncDb();
        
        /* GroupQueryFilter filter = new GroupQueryFilter();
        filter.setQueryString("深圳");
        filter.addQueryTerm(GroupQueryFilter.TERM_NAME);
        List<Long> ids = this.groupSearcher.query(filter);
        Assert.assertTrue("found name", ids.size() == 0);
        
        filter = new GroupQueryFilter();
        filter.setQueryString("爬山");
        filter.addQueryTerm(GroupQueryFilter.TERM_NAME);
        ids = this.groupSearcher.query(filter);
        Assert.assertTrue("found name", ids.size() > 0);
        
        for(Long id : ids) {
            Group g = this.groupProvider.findGroupById(id);
            System.out.println("id:" + g.getId() + " name:" + g.getName() + " tag:" + g.getTag());
        }
        
        filter = new GroupQueryFilter();
        filter.setQueryString("葡萄");
        filter.addQueryTerm(GroupQueryFilter.TERM_TAG);
        ids = this.groupSearcher.query(filter);
        Assert.assertTrue("found tag", ids.size() > 0); */
        
    }
    
    //@Test
    public void testStringHelper() {
        SendMessageCommand cmd = new SendMessageCommand();
        cmd.setAppId(32434L);
        cmd.setBody("This is a body");
        List<MessageChannel> channels = new ArrayList<MessageChannel>();
        MessageChannel c1 = new MessageChannel();
        c1.setChannelToken("channel 1");
        c1.setChannelType("Group");
        channels.add(c1);
        MessageChannel c2 = new MessageChannel();
        c2.setChannelToken("channel 2");
        c2.setChannelType("User");
        channels.add(c2);
        cmd.setChannels(channels);
        System.out.println(StringHelper.toJsonString(cmd));
        
        Map<String, String> maps = new HashMap<String, String>();
        StringHelper.toStringMap(null, cmd, maps);
        System.out.println(StringHelper.toJsonString(maps));
    }
    
    private GroupDTO createGroup(String name, String tag) {
        //String name = "旅游(组名称)";
        String description = "找一帮趣味相投的人一起旅游";
        String avatar = "MTo4ZTk2OGNmMWExMjkwOWU1OGI4Nzk1YmIwZTgwOGYyNA";
        Byte visibilityScope = null;
        Long visibilityScopeId = null;
        Byte privateFlag = 0; // 公有
        //Long categoryId = categoryId; // 类型
        //String tag = null;
        String explicitRegionDescriptorsJson = null;
        CreateGroupCommand cmd = new CreateGroupCommand();
        cmd.setName(name);
        cmd.setDescription(description);
        cmd.setAvatar(avatar);
        cmd.setVisibilityScope(visibilityScope);
        cmd.setVisibilityScopeId(visibilityScopeId);
        cmd.setPrivateFlag(privateFlag);
        cmd.setCategoryId(categoryId);
        cmd.setTag(tag);
        cmd.setExplicitRegionDescriptorsJson(explicitRegionDescriptorsJson);
        
//        return groupService.createGroup(cmd);
        return null;
    }
    
    private void cleanData() {
        // 删除用户
        List<Long> userIdList = deletePhoneUser(phone);
        // 删除用户创建的group
        List<Long> groupIdList = deleteGroup(userIdList);
        // 删除组中的用户数据
        deleteGroupMemberByGroup(groupIdList);
        // 删除组关联的论坛
        deleteForumByGroup(groupIdList);
        
        deleteCategory();
    }
    
    private List<Category> createCategory() {
        List<Category> cateList = new ArrayList<Category>();
        
        Long id = categoryId;
        Category category = new Category();
        category.setId(id++);
        category.setName("餐饮");
        category.setParentId(0L);
        category.setPath("/生活");
        category.setDefaultOrder(10);
        category.setStatus((byte)2);
        categoryProvider.createCategory(category);
        cateList.add(category);
        
        category = new Category();
        category.setId(id++);
        category.setName("风景");
        category.setParentId(0L);
        category.setPath("/生活/真美丽");
        category.setDefaultOrder(10);
        category.setStatus((byte)2);
        categoryProvider.createCategory(category);
        cateList.add(category);
        
        category = new Category();
        category.setId(id++);
        category.setName("跳舞");
        category.setParentId(0L);
        category.setPath("/生活/真美丽/变胖");
        category.setDefaultOrder(10);
        category.setStatus((byte)2);
        categoryProvider.createCategory(category);
        cateList.add(category);
        
        return cateList;
    }
    
    private void deleteCategory() {
        Long id = categoryId;
       
        categoryProvider.deleteCategoryById(id++);
        categoryProvider.deleteCategoryById(id++);
        categoryProvider.deleteCategoryById(id++);
    }
    
    private List<Long> deleteGroup(List<Long> userIdList) {
        List<Long> groupIdList = new ArrayList<Long>();
        
        List<Group> tmpGroupList = null;
        for(long creatorId : userIdList) {
            tmpGroupList = groupProvider.findGroupByCreatorId(creatorId);
            for(Group group : tmpGroupList) {
                groupProvider.deleteGroup(group);
                groupIdList.add(group.getId());
            }
        }
        
        return groupIdList;
    }
    
    private List<Long> deleteGroupMemberByGroup(List<Long> groupIdList) {
        List<Long> groupMemberIdList = new ArrayList<Long>();
        
        List<GroupMember> tmpGroupMemberList = null;
        for(long groupId : groupIdList) {
            tmpGroupMemberList = groupProvider.findGroupMemberByGroupId(groupId);
            for(GroupMember member : tmpGroupMemberList) {
                groupProvider.deleteGroupMember(member);
                groupMemberIdList.add(member.getId());
            }
        }
        
        return groupMemberIdList;
    }
    
    private List<Long> deleteForumByGroup(List<Long> groupIdList) {
        List<Long> forumIdList = new ArrayList<Long>();
        
        List<Forum> tmpForumList = null;
        for(long groupId : groupIdList) {
            tmpForumList = forumProvider.findForumByOwner(EntityType.GROUP.getCode(), groupId);
            for(Forum forum : tmpForumList) {
                forumProvider.deleteForum(forum);
                forumIdList.add(forum.getId());
            }
        }
        
        return forumIdList;
    }
}
