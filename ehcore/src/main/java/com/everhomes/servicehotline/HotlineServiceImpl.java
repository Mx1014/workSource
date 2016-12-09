package com.everhomes.servicehotline;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.hibernate.type.YesNoType;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.servicehotline.AddHotlineListCommand;
import com.everhomes.rest.servicehotline.DeleteHotlineListCommand;
import com.everhomes.rest.servicehotline.GetHotlineListCommand;
import com.everhomes.rest.servicehotline.GetHotlineListResponse;
import com.everhomes.rest.servicehotline.GetHotlineSubjectCommand;
import com.everhomes.rest.servicehotline.GetHotlineSubjectResponse;
import com.everhomes.rest.servicehotline.HotlineDTO;
import com.everhomes.rest.servicehotline.HotlineSubject;
import com.everhomes.rest.servicehotline.LayoutType;
import com.everhomes.rest.servicehotline.ServiceType;
import com.everhomes.rest.servicehotline.SetHotlineSubjectCommand;
import com.everhomes.rest.servicehotline.UpdateHotlineCommand;
import com.everhomes.rest.servicehotline.UpdateHotlinesCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.techpark.servicehotline.HotlineService;
import com.everhomes.techpark.servicehotline.ServiceConfiguration;
import com.everhomes.techpark.servicehotline.ServiceConfigurationsProvider;
import com.everhomes.techpark.servicehotline.ServiceHotline;
import com.everhomes.techpark.servicehotline.ServiceHotlinesProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class HotlineServiceImpl implements HotlineService {
	public static final String HOTLINE_SCOPE = "hotline";

	@Autowired
	private ServiceConfigurationsProvider serviceConfigurationsProvider;

	@Autowired
	private ServiceHotlinesProvider serviceHotlinesProvider;

	@Autowired
	private UserProvider userProvider;
	
	@Autowired
	private DbProvider dbProvider;

	@Override
	public GetHotlineSubjectResponse getHotlineSubject(
			GetHotlineSubjectCommand cmd) {
		List<ServiceConfiguration> sConfigurations = serviceConfigurationsProvider
				.queryServiceConfigurations(null, 1,
						new ListingQueryBuilderCallback() {

							@Override
							public SelectQuery<? extends Record> buildCondition(
									ListingLocator locator,
									SelectQuery<? extends Record> query) {
								query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.OWNER_TYPE
										.eq(cmd.getOwnerType()));
								query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.OWNER_ID
										.eq(cmd.getOwnerId()));
								query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.NAMESPACE_ID
										.eq(UserContext.getCurrentNamespaceId()));
								query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.NAME
										.eq(HOTLINE_SCOPE));
								return query;
							}
						});
		GetHotlineSubjectResponse response = new GetHotlineSubjectResponse();
		HotlineSubject baseHotline = new HotlineSubject();
		baseHotline.setLayoutType(LayoutType.SERVICE_HOTLINE.getCode());
		baseHotline.setServiceType(ServiceType.SERVICE_HOTLINE.getCode());
		baseHotline.setTitle("服务热线");
		response.setSubjects(new ArrayList<HotlineSubject>());
		response.getSubjects().add(baseHotline);
		for (ServiceConfiguration configuration : sConfigurations) {
			if (ServiceType.ZHUANSHU_SERVICE.getCode().byteValue() == (ServiceType.ZHUANSHU_SERVICE
					.getCode().byteValue() & Byte.valueOf(configuration
					.getValue()))) {
				HotlineSubject service1 = new HotlineSubject();
				service1.setLayoutType(LayoutType.ZHUANSHU_SERVICE.getCode());
				service1.setServiceType(ServiceType.ZHUANSHU_SERVICE.getCode());
				service1.setTitle(configuration.getDisplayName());
				response.getSubjects().add(service1);
			}
			// 新增的类型在后面加,仿照专属客服
		}
		return null;
	}

	@Override
	public GetHotlineListResponse getHotlineList(GetHotlineListCommand cmd) {
		GetHotlineListResponse resp = new GetHotlineListResponse();
		List<HotlineDTO> hotlines = new ArrayList<HotlineDTO>();
		this.serviceHotlinesProvider.queryServiceHotlines(null,
				Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {

					@Override
					public SelectQuery<? extends Record> buildCondition(
							ListingLocator locator,
							SelectQuery<? extends Record> query) {
						query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.OWNER_TYPE
								.eq(cmd.getOwnerType()));
						query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.OWNER_ID
								.eq(cmd.getOwnerId()));
						query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.NAMESPACE_ID
								.eq(UserContext.getCurrentNamespaceId()));
						query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.NAME
								.eq(HOTLINE_SCOPE));
						query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.VALUE
								.eq(cmd.getServiceType() + ""));
						return query;
					}
				}).forEach(r -> {
			HotlineDTO dto = ConvertHelper.convert(r, HotlineDTO.class);
			User user = this.userProvider.findUserById(r.getUserId());
			dto.setAvatar(user.getAvatar());
			hotlines.add(dto);
		});
		resp.setHotlines(hotlines);
		return resp;
	}

	@Override
	public void addHotlineList(AddHotlineListCommand cmd) {
		ServiceHotline hotline = ConvertHelper.convert(cmd,
				ServiceHotline.class);
		this.serviceHotlinesProvider.createServiceHotline(hotline);

	}

	@Override
	public void deleteHotline(DeleteHotlineListCommand cmd) {

		if (null == cmd
				|| null == this.serviceHotlinesProvider
						.getServiceHotlineById(cmd.getId())) {
			throw RuntimeErrorException
					.errorWith(ErrorCodes.SCOPE_GENERAL,
							ErrorCodes.ERROR_INVALID_PARAMETER,
							"Invalid paramter id can not be null or hotline can not found");
		}
		ServiceHotline hotline = ConvertHelper.convert(cmd,
				ServiceHotline.class);
		this.serviceHotlinesProvider.deleteServiceHotline(hotline);
	}

	@Override
	public void updateHotline(UpdateHotlineCommand cmd) {
		if (null == cmd
				|| null == this.serviceHotlinesProvider
						.getServiceHotlineById(cmd.getId())) {
			throw RuntimeErrorException
					.errorWith(ErrorCodes.SCOPE_GENERAL,
							ErrorCodes.ERROR_INVALID_PARAMETER,
							"Invalid paramter id can not be null or hotline can not found");
		}
		ServiceHotline hotline = ConvertHelper.convert(cmd,
				ServiceHotline.class);
		this.serviceHotlinesProvider.updateServiceHotline(hotline);
	}

	@Override
	public void updateHotlines(UpdateHotlinesCommand cmd) {
		this.dbProvider.execute((TransactionStatus status) -> {
			for (UpdateHotlineCommand cmd1 : cmd.getHotlines()) {
				this.updateHotline(cmd1);
			}
			return null;
		});
	}

	@Override
	public void setHotlineSubject(SetHotlineSubjectCommand cmd) {
		switch (NormalFlag.fromCode(cmd.getSwitchFlag())) {
		case NEED:
			ServiceConfiguration obj = new ServiceConfiguration();
			obj.setOwnerType(cmd.getOwnerType());
			obj.setOwnerId(cmd.getOwnerId());
			obj.setNamespaceId(UserContext.getCurrentNamespaceId());
			obj.setName(HOTLINE_SCOPE);
			obj.setValue(cmd.getServiceType() + "");
			serviceConfigurationsProvider.createServiceConfiguration(obj);
			break;
		case NONEED:
			ServiceConfiguration sConfiguration = serviceConfigurationsProvider
					.queryServiceConfigurations(null, 1,
							new ListingQueryBuilderCallback() {

								@Override
								public SelectQuery<? extends Record> buildCondition(
										ListingLocator locator,
										SelectQuery<? extends Record> query) {
									query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.OWNER_TYPE
											.eq(cmd.getOwnerType()));
									query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.OWNER_ID
											.eq(cmd.getOwnerId()));
									query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.NAMESPACE_ID
											.eq(UserContext
													.getCurrentNamespaceId()));
									query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.NAME
											.eq(HOTLINE_SCOPE));
									query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.VALUE
											.eq(cmd.getServiceType() + ""));
									return query;
								}
							}).get(0);
			serviceConfigurationsProvider
					.deleteServiceConfiguration(sConfiguration);
			break;

		}
	}

}
