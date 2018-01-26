package com.everhomes.rest.customer;

/**
 * <ul>
 *     <li>infoId: ebei门牌唯一标识</li>
 *     <li>state: 1-在租——已租状态,2-待租——未租状态, 其余为其他状态</li>
 * </ul>
 * Created by ying.xiong on 2018/1/18.
 */
public class EbeiApartment {
    private String infoId;
    private Byte state;

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }
}
