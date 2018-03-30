package com.everhomes.rest.acl;

import com.everhomes.rest.module.AppLocationType;
import com.everhomes.rest.module.TerminalType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>entryName：应用入口名称</li>
 * <li>terminal：终端, 参考{@link TerminalType}</li>
 * <li>location: 位置, 参考{@link AppLocationType}</li>
 * </ul>
 */
public class AppEntryInfosDTO {
    private String entryName;
    private Byte terminal;
    private Byte location;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public Byte getTerminal() {
        return terminal;
    }

    public void setTerminal(Byte terminal) {
        this.terminal = terminal;
    }

    public Byte getLocation() {
        return location;
    }

    public void setLocation(Byte location) {
        this.location = location;
    }
}
