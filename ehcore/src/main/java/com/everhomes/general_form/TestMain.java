package com.everhomes.general_form;

import com.everhomes.rest.general_approval.GeneralFormMultiSelectDTO;
import com.everhomes.util.PinYinHelper;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class TestMain {
    public static void main(String [] args){
        String str = "{\"selectValue\":[\"蓝天\",\"白云\",\"猪\",\"豆豆\"]}";
       String str1 =  multiSelectTransferPingyin(str);
        System.out.println(str1);
    }


    /**
     * 初始化出拼音
     * @param selectValue
     * @return
     */
    static String multiSelectTransferPingyin(String selectValue){
        if(StringUtils.isBlank(selectValue)){
           // LOGGER.info("multiSelectTransferPingyin : selectValue is blank !");
            return selectValue ;
        }

        GeneralFormMultiSelectDTO dto = (GeneralFormMultiSelectDTO) StringHelper.fromJsonString(selectValue,GeneralFormMultiSelectDTO.class);
        if(CollectionUtils.isEmpty(dto.getSelectValue())){
           // LOGGER.info("multiSelectTransferPingyin : selectValue is empty !");
            return selectValue ;
        }
        List<String> pinyins = new ArrayList<String>();
        for(String str : dto.getSelectValue()){
            String pinyin = "";
            try{ //转换拼音出错不应该影响该功能的进行
                pinyin = PinYinHelper.getPinYin(str);
            }catch(Exception e ){
                //LOGGER.info("multiSelectTransferPingyin  : str:{} can not transfer pinyin !",str);
            }

            pinyins.add(pinyin);
        }

        dto.setPingYin(pinyins);
        selectValue = StringHelper.toJsonString(dto);

        return selectValue ;
    }
}
