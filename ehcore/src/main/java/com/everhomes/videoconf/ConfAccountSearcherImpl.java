package com.everhomes.videoconf;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.enterprise.Enterprise;
import com.everhomes.enterprise.EnterpriseContact;
import com.everhomes.enterprise.EnterpriseContactEntry;
import com.everhomes.enterprise.EnterpriseContactGroup;
import com.everhomes.enterprise.EnterpriseContactGroupMember;
import com.everhomes.enterprise.EnterpriseContactProvider;
import com.everhomes.enterprise.EnterpriseProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.videoconf.ConfAccountDTO;
import com.everhomes.rest.videoconf.ConfCategoryDTO;
import com.everhomes.rest.videoconf.EnterpriseUsersDTO;
import com.everhomes.rest.videoconf.ListEnterpriseVideoConfAccountCommand;
import com.everhomes.rest.videoconf.ListEnterpriseVideoConfAccountResponse;
import com.everhomes.rest.videoconf.ListUsersWithoutVideoConfPrivilegeResponse;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.ConfAccountSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserProvider;
import com.everhomes.util.DateHelper;
import com.mysql.jdbc.StringUtils;

@Component
public class ConfAccountSearcherImpl extends AbstractElasticSearch implements
		ConfAccountSearcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfAccountSearcherImpl.class);
	
	@Autowired
    private UserProvider userProvider;
	
	
	@Autowired
	private VideoConfProvider vcProvider;
	
	@Autowired
    private ConfigurationProvider configProvider;
	
	@Autowired
    private OrganizationProvider organizationProvider;
	
	@Override
	public void deleteById(Long id) {
		deleteById(id.toString());
	}

	@Override
	public void bulkUpdate(List<ConfAccounts> accounts) {
		BulkRequestBuilder brb = getClient().prepareBulk();
        for (ConfAccounts account : accounts) {
//        	if(account.getOwnerId() != null && account.getOwnerId() != 0) {
	            XContentBuilder source = createDoc(account);
	            if(null != source) {
	                LOGGER.info("conf account id:" + account.getId());
	                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
	                        .id(account.getId().toString()).source(source));    
	                }
 //       	}
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }

	}

	@Override
	public void feedDoc(ConfAccounts account) {
//		if(account.getOwnerId() != 0) {
			XContentBuilder source = createDoc(account);
	        
	        feedDoc(account.getId().toString(), source);
//		}

	}

	@Override
	public void syncFromDb() {
		int pageSize = 200;      
        this.deleteAll();
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<ConfAccounts> accounts = vcProvider.listConfAccountsByEnterpriseId(null, null, locator, pageSize);
            
            if(accounts.size() > 0) {
                this.bulkUpdate(accounts);
            }
            
            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();
        
        LOGGER.info("sync for conference account ok");

	}

	@Override
	public ListEnterpriseVideoConfAccountResponse query(ListEnterpriseVideoConfAccountCommand cmd) {
		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
		QueryBuilder qb = null;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                    .field("userName", 5.0f)
                    .field("enterpriseName", 3.0f)
                    .field("department", 2.0f)
                    .field("contact", 1.0f);
            
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("userName").addHighlightedField("enterpriseName").addHighlightedField("department").addHighlightedField("contact");
        }

        FilterBuilder fb = null;
        if(cmd.getIsAssigned() == null || cmd.getIsAssigned() == 0) {
        	FilterBuilder nfb = FilterBuilders.termFilter("ownerId", 0);
        	fb = FilterBuilders.notFilter(nfb);
        }
        if(cmd.getEnterpriseId() != null) {
        	if(fb == null) {
        		fb = FilterBuilders.termFilter("enterpriseId", cmd.getEnterpriseId());
        	} else {
        		fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("enterpriseId", cmd.getEnterpriseId()));
        	}
        }
        if(cmd.getStatus() != null) {
        	if(fb == null) {
        		fb = FilterBuilders.termFilter("status", cmd.getStatus());
        	} else {
        		fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("status", cmd.getStatus()));
        	}
        }
        
        if(fb == null) {
    		fb = FilterBuilders.termFilter("deleteUid", 0);
    	} else {
    		fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("deleteUid", 0));
    	}
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
        
        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);

		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("ListEnterpriseVideoConfAccount query : {}", builder);
		}
        SearchResponse rsp = builder.execute().actionGet();

		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("ListEnterpriseVideoConfAccount query result: {}", rsp);
		}
        List<Long> ids = getIds(rsp);
        
        ListEnterpriseVideoConfAccountResponse response = new ListEnterpriseVideoConfAccountResponse();
        if(ids.size() > pageSize) {
        	response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
         } else {
        	 response.setNextPageAnchor(null);
            }
        
        List<ConfAccountDTO> confAccounts = new ArrayList<ConfAccountDTO>();
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        for(Long id : ids) {
        	ConfAccountDTO dto = new ConfAccountDTO();
        	ConfAccounts account = vcProvider.findVideoconfAccountById(id);
        	if(null == account)
        		continue;
        	dto.setId(account.getId());
			dto.setUserId(account.getOwnerId());
			dto.setValidDate(account.getExpiredDate());
			dto.setUpdateDate(account.getUpdateTime());
			if(account.getAccountType() == 1)
				dto.setUserType((byte) 0);
			else {
				if(vcProvider.countOrdersByAccountId(account.getId()) == 1)
					dto.setUserType((byte) 1);
				else {
					dto.setUserType((byte) 2);
				}
			}
			dto.setStatus(account.getStatus());
			
			if(now.after(account.getExpiredDate())) {
				dto.setValidFlag((byte) 0);
			}
			else if(addMonth(now, 1).after(account.getExpiredDate())) {
				dto.setValidFlag((byte) 1);
			}
			else {
				dto.setValidFlag((byte) 2);
			}
			ConfAccountCategories category = vcProvider.findAccountCategoriesById(account.getAccountCategoryId());
			if(category != null) {
				dto.setConfType(category.getConfType());
				
				ConfCategoryDTO confCategorydto = new ConfCategoryDTO();
				confCategorydto.setSingleAccountPrice(category.getSingleAccountPrice());
				confCategorydto.setMultipleAccountThreshold(category.getMultipleAccountThreshold());
				confCategorydto.setMultipleAccountPrice(category.getMultipleAccountPrice());
				confCategorydto.setMinPeriod(category.getMinPeriod());
				
				if(category.getConfType() == 0 || category.getConfType() == 1) {
					confCategorydto.setConfCapacity((byte) 0);
				}
				if(category.getConfType() == 2 || category.getConfType() == 3) {
					confCategorydto.setConfCapacity((byte) 1);
				}
				if(category.getConfType() == 4) {
					confCategorydto.setConfCapacity((byte) 2);
				}
				if(category.getConfType() == 5 || category.getConfType() == 6) {
					confCategorydto.setConfCapacity((byte) 3);
				}
				
				dto.setCategory(confCategorydto);
			}
			
			Organization org = organizationProvider.findOrganizationById(account.getEnterpriseId());
			if(org != null) {
//				OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(account.getOwnerId(), org.getId());
				dto.setEnterpriseName(org.getName());
				//分公司和总公司的问题 在分公司通讯录而不在总公司通讯录中 用总公司机构id 拿不到通讯录信息 暂只根据用户id取 by xiongying20170327
				List<OrganizationMember> members =  organizationProvider.listOrganizationMembersByUId(account.getOwnerId());
				if(members != null && members.size() > 0) {
//					if (member != null) {
						dto.setUserName(members.get(0).getContactName());
						dto.setMobile(members.get(0).getContactToken());
						Organization dept = organizationProvider.findOrganizationById(members.get(0).getGroupId());
						if (dept != null) {
							dto.setDepartment(dept.getName());
						}
//					}
				}

			}
			confAccounts.add(dto);
        }
        response.setConfAccounts(confAccounts);
        
        return response;
	}
	
	private Timestamp addMonth(Timestamp now, int months) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.MONTH, months);
		Timestamp newPeriod = new Timestamp(calendar.getTimeInMillis());
		
		return newPeriod;
	}

	@Override
	public String getIndexType() {
		return SearchUtils.CONFACCOUNTINDEXTYPE;
	}
	
	private XContentBuilder createDoc(ConfAccounts account){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("id", account.getId());
            b.field("updateTime", account.getUpdateTime());
            b.field("expiredDate", account.getExpiredDate());
            b.field("status", account.getStatus());
            b.field("enterpriseId", account.getEnterpriseId());
            b.field("deleteUid", account.getDeleteUid());

            if(account.getOwnerId() != null)
            	b.field("ownerId", account.getOwnerId());
            else {
            	b.field("ownerId", 0);
            }
//            b.field("userType", account.getId());
//            if(account.getAccountType() == 1)
//            	b.field("userType", 0);
//			else {
//				if(vcProvider.countOrdersByAccountId(account.getId()) == 1)
//					b.field("userType", 1);
//				else {
//					b.field("userType", 2);
//				}
//			}
//			OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(account.getOwnerId(), account.getEnterpriseId());
			//分公司和总公司的问题 在分公司通讯录而不在总公司通讯录中 用总公司机构id 拿不到通讯录信息 暂只根据用户id取 by xiongying20170327
			List<OrganizationMember> members =  organizationProvider.listOrganizationMembersByUId(account.getOwnerId());
			if(members != null && members.size() > 0) {
				b.field("userName", members.get(0).getContactName());
				Organization dept = organizationProvider.findOrganizationById(members.get(0).getGroupId());
				if (null != dept) {
					b.field("department", dept.getName());
				} else {
					b.field("department", "");
				}
				b.field("contact", members.get(0).getContactToken());
			} else {
                b.field("userName", "");
                b.field("department", "");
                b.field("contact", "");
            }
            
            ConfAccountCategories category = vcProvider.findAccountCategoriesById(account.getAccountCategoryId());
            if(null != category) {
//                b.field("accountType", category.getChannelType());
                b.field("confType", category.getConfType());
            } else {
                b.field("accountType", "");
                b.field("confType", "");
            }
            
            Organization org = organizationProvider.findOrganizationById(account.getEnterpriseId());
            if(null != org) {
                b.field("enterpriseName", org.getName());
            } else {
                b.field("enterpriseName", "");
            }
            
            
            
            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create account " + account.getId() + " error");
            return null;
        }
    }

}
