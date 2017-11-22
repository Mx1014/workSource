package com.everhomes.rest.point;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>displayName: displayName</li>
 *     <li>pointName: pointName</li>
 *     <li>pointExchangeFlag: pointExchangeFlag</li>
 *     <li>exchangeRate: exchangeRate</li>
 *     <li>userAgreement: userAgreement</li>
 *     <li>status: status</li>
 * </ul>
 */
public class UpdatePointSystemCommand {

    private Long id;
    private String displayName;
    private String pointName;
    private Byte pointExchangeFlag;
    private Integer exchangeRate;
    private String userAgreement;
    private Byte status;
}
