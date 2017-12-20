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
 * <li>lateFineStandardId:滞纳金id</li>
 * <li>lateFineStandardName：滞纳金标准名称</li>
 * <li>lateFineStandardFormula：滞纳金展示公式</li>
 *</ul>
 */
public class ListLateFineStandardsDTO {
    private long lateFineStandardId;
    private String lateFineStandardName;
    private String lateFineStandardFormula;

    public long getLateFineStandardId() {
        return lateFineStandardId;
    }

    public void setLateFineStandardId(long lateFineStandardId) {
        this.lateFineStandardId = lateFineStandardId;
    }

    public String getLateFineStandardName() {
        return lateFineStandardName;
    }

    public void setLateFineStandardName(String lateFineStandardName) {
        this.lateFineStandardName = lateFineStandardName;
    }

    public String getLateFineStandardFormula() {
        return lateFineStandardFormula;
    }

    public void setLateFineStandardFormula(String lateFineStandardFormula) {
        this.lateFineStandardFormula = lateFineStandardFormula;
    }
}
