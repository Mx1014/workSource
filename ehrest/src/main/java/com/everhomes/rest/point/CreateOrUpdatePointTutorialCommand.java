package com.everhomes.rest.point;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>systemId: systemId</li>
 *     <li>displayName: displayName</li>
 *     <li>description: description</li>
 *     <li>posterUri: posterUri</li>
 *     <li>status: status</li>
 *     <li>mappings: mappings {@link com.everhomes.rest.point.PointTutorialMappingCommand}</li>
 * </ul>
 */
public class CreateOrUpdatePointTutorialCommand {

    private Long id;
    private Integer namespaceId;
    private Long systemId;
    private String displayName;
    private String description;
    private String posterUri;
    private Byte status;


    @ItemType(PointTutorialMappingCommand.class)
    private List<PointTutorialMappingCommand> mappings;

}
