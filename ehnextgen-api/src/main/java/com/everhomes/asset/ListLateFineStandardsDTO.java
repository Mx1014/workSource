//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian Wang on 2017/12/18.
 */
/**
 **************************************************************
 *                                                            *
 *   .=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-.       *
 *    |                     ______                     |      *
 *    |                  .-"      "-.                  |      *
 *    |                 /            \                 |      *
 *    |     _          |              |          _     |      *
 *    |    ( \         |,  .-.  .-.  ,|         / )    |      *
 *    |     > "=._     | )(__/  \__)( |     _.=" <     |      *
 *    |    (_/"=._"=._ |/     /\     \| _.="_.="\_)    |      *
 *    |           "=._"(_     ^^     _)"_.="           |      *
 *    |               "=\__|IIIIII|__/="               |      *
 *    |              _.="| \IIIIII/ |"=._              |      *
 *    |    _     _.="_.="\          /"=._"=._     _    |      *
 *    |   ( \_.="_.="     `--------`     "=._"=._/ )   |      *
 *    |    > _.="                            "=._ <    |      *
 *    |   (_/                                    \_)   |      *
 *    |                                                |      *
 *    '-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-='      *
 *                                                            *
 *           LASCIATE OGNI SPERANZA, VOI CH'ENTRATE           *
 **************************************************************
 */
/**
 *<ul>
 * <li>lateFeeStandardId:滞纳金id</li>
 * <li>lateFeeStandardName：滞纳金标准名称</li>
 * <li>lateFeeStandardFormula：滞纳金展示公式</li>
 *</ul>
 */
public class ListLateFineStandardsDTO {
    private long lateFeeStandardId;
    private String lateFeeStandardName;
    private String lateFeeStandardFormula;

    public long getLateFeeStandardId() {
        return lateFeeStandardId;
    }

    public void setLateFeeStandardId(long lateFeeStandardId) {
        this.lateFeeStandardId = lateFeeStandardId;
    }

    public String getLateFeeStandardName() {
        return lateFeeStandardName;
    }

    public void setLateFeeStandardName(String lateFeeStandardName) {
        this.lateFeeStandardName = lateFeeStandardName;
    }

    public String getLateFeeStandardFormula() {
        return lateFeeStandardFormula;
    }

    public void setLateFeeStandardFormula(String lateFeeStandardFormula) {
        this.lateFeeStandardFormula = lateFeeStandardFormula;
    }
}
