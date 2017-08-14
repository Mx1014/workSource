package com.everhomes.varField;

import com.everhomes.rest.varField.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.SortOrder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/8/3.
 */
@Component
public class FieldServiceImpl implements FieldService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FieldServiceImpl.class);

    @Autowired
    private FieldProvider fieldProvider;
    @Override
    public List<FieldDTO> listFields(ListFieldCommand cmd) {
        List<FieldDTO> dtos = null;
        if(cmd.getNamespaceId() == null) {
            List<Field> fields = fieldProvider.listFields(cmd.getModuleName(), cmd.getGroupPath());
            if(fields != null && fields.size() > 0) {
                dtos = fields.stream().map(field -> {
                    FieldDTO dto = ConvertHelper.convert(field, FieldDTO.class);
                    dto.setFieldDisplayName(field.getDisplayName());
                    return dto;
                }).collect(Collectors.toList());
            }
        } else {
            dtos = listScopeFields(cmd);
        }

        return dtos;
    }

    private List<FieldDTO> listScopeFields(ListFieldCommand cmd) {
        List<ScopeField> scopeFields = fieldProvider.listScopeFields(cmd.getNamespaceId(), cmd.getModuleName(), cmd.getGroupPath());
        if(scopeFields != null && scopeFields.size() > 0) {
            List<Long> fieldIds = new ArrayList<>();
            Map<Long, FieldDTO> dtoMap = new HashMap<>();
            scopeFields.forEach(field -> {
                fieldIds.add(field.getFieldId());
                dtoMap.put(field.getFieldId(), ConvertHelper.convert(field, FieldDTO.class));
            });

            //一把取出scope field对应的所有系统的field 然后把对应信息塞进fielddto中
            //一把取出所有的scope field对应的scope items信息
            List<Field> fields = fieldProvider.listFields(fieldIds);
            List<ScopeFieldItem> fieldItems = fieldProvider.listScopeFieldItems(fieldIds);

            if(fields != null && fields.size() > 0) {
                List<FieldDTO> dtos = new ArrayList<>();
                fields.forEach(field -> {
                    FieldDTO dto = dtoMap.get(field.getId());
                    dto.setFieldType(field.getFieldType());
                    dto.setFieldName(field.getName());
                    if(fieldItems != null && fieldItems.size() > 0) {
                        List<FieldItemDTO> items = new ArrayList<FieldItemDTO>();
                        fieldItems.forEach(item -> {
                            if(field.getId().equals(item.getFieldId())) {
                                FieldItemDTO fieldItem = ConvertHelper.convert(item, FieldItemDTO.class);
                                items.add(fieldItem);
                            }
                        });
                        //按default order排序
                        Collections.sort(items, (a,b) -> {
                            return a.getDefaultOrder() - b.getDefaultOrder();
                        });
                        dto.setItems(items);
                    }
                    dtos.add(dto);
                });

                //按default order排序
                Collections.sort(dtos, (a,b) -> {
                    return a.getDefaultOrder() - b.getDefaultOrder();
                });
                return dtos;
            }
        }
        return null;
    }

    @Override
    public List<FieldGroupDTO> listFieldGroups(ListFieldGroupCommand cmd) {
        List<FieldGroupDTO> dtos = null;
        if(cmd.getNamespaceId() == null) {
            List<FieldGroup> groups = fieldProvider.listFieldGroups(cmd.getModuleName());
            if(groups != null && groups.size() > 0) {
                dtos = groups.stream().map(group -> {
                    FieldGroupDTO dto = ConvertHelper.convert(group, FieldGroupDTO.class);
                    dto.setGroupDisplayName(group.getTitle());
                    return dto;
                }).collect(Collectors.toList());
            }
        } else {
            dtos = listScopeFieldGroups(cmd);
        }

        return dtos;
    }

    private List<FieldGroupDTO> listScopeFieldGroups(ListFieldGroupCommand cmd) {
        List<ScopeFieldGroup> groups = fieldProvider.listScopeFieldGroups(cmd.getNamespaceId(), cmd.getModuleName());
        if(groups != null && groups.size() > 0) {
            List<Long> groupIds = new ArrayList<>();
            Map<Long, FieldGroupDTO> dtoMap = new HashMap<>();
            groups.forEach(group -> {
                groupIds.add(group.getGroupId());
                dtoMap.put(group.getGroupId(), ConvertHelper.convert(group, FieldGroupDTO.class));
            });

            //一把取出scope group对应的所有系统的group 然后把parentId塞回dto中
            List<FieldGroup> fieldGroups = fieldProvider.listFieldGroups(groupIds);
            List<FieldGroupDTO> dtos = new ArrayList<>();
            if(fieldGroups != null && fieldGroups.size() > 0) {
                fieldGroups.forEach(fieldGroup -> {
                    FieldGroupDTO dto = dtoMap.get(fieldGroup.getId());
                    dto.setParentId(fieldGroup.getParentId());
                    dto.setGroupPath(fieldGroup.getPath());
                    dtos.add(dto);
                });
            }

            //处理group的树状结构
            FieldGroupDTO fieldGroupDTO = processFieldGroupnTree(dtos, null);
            List<FieldGroupDTO> groupDTOs = fieldGroupDTO.getChildrenGroup();
            LOGGER.info("groupDTOs: {}", groupDTOs);
            //按default order排序
            Collections.sort(groupDTOs, (a,b) -> {
                return a.getDefaultOrder() - b.getDefaultOrder();
            });

            return groupDTOs;
        }
        return null;
    }

    /**
     * 树状结构
     * @param dtos
     * @param dto
     * @return
     */
    private FieldGroupDTO processFieldGroupnTree(List<FieldGroupDTO> dtos, FieldGroupDTO dto) {
        List<FieldGroupDTO> trees = new ArrayList<>();

        if(dto == null) {
            dto = new FieldGroupDTO();
            dto.setGroupId(0L);
        }
//        FieldGroupDTO allTreeDTO = ConvertHelper.convert(dto, FieldGroupDTO.class);
//        trees.add(allTreeDTO);
        for (FieldGroupDTO groupTreeDTO : dtos) {
            if (groupTreeDTO.getParentId().equals(dto.getGroupId())) {
                FieldGroupDTO organizationTreeDTO = processFieldGroupnTree(dtos, groupTreeDTO);
                trees.add(organizationTreeDTO);
            }
        }

        dto.setChildrenGroup(trees);
        return dto;
    }

}
