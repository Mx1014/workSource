package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>moduleId: 业务模块 ID</li>
 * <li>appOriginId: 业务应用 ID，因为 appId 被拿来干别的意思了，这个字段与服务广场的保持一致 </li>
 * <li>data: 业务自己的数据</li>
 * <li>items: 拓展字段</li>
 * <li>smartCardType: 支持一卡通的业务类型，支付使用 0x1，门禁业务使用 0x2。一般来说通过 AppOriginId 来对接业务。而这里的业务类型倾向与二维码实现的协议(type,length,content)</li>
 * </ul>
 * @author janson
 *
 */
public class SmartCardHandler {
    private Long moduleId;
    private Long appOriginId;
    private String data;
    private String title;
    private Byte smartCardType;
    
    @ItemType(SmartCardHandlerItem.class)
    private List<SmartCardHandlerItem> items;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getAppOriginId() {
        return appOriginId;
    }

    public void setAppOriginId(Long appOriginId) {
        this.appOriginId = appOriginId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<SmartCardHandlerItem> getItems() {
		return items;
	}

	public void setItems(List<SmartCardHandlerItem> items) {
		this.items = items;
	}

	public Byte getSmartCardType() {
		return smartCardType;
	}

	public void setSmartCardType(Byte smartCardType) {
		this.smartCardType = smartCardType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
