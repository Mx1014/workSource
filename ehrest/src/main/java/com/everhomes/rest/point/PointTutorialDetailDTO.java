package com.everhomes.rest.point;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>systemId: systemId</li>
 *     <li>tutorialId: tutorialId</li>
 *     <li>ruleId: ruleId</li>
 *     <li>ruleName: ruleName</li>
 *     <li>operateType: operateType</li>
 *     <li>points: points</li>
 *     <li>description: description</li>
 * </ul>
 */
public class PointTutorialDetailDTO {
    private Long id;
    private Integer namespaceId;
    private Long systemId;
    private Long tutorialId;
    private Long ruleId;
    private String ruleName;
    private Byte operateType;
    private Integer points;
    private String description;
}