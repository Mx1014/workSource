package com.everhomes.contract;

import java.util.List;

import com.everhomes.contract.template.GetKeywordsUtils;

/**
 * @author Steve
 * @version 1.0
 * @description com.test.demo06.getKeyWords
 * @date 2018/12/3
 */
public class TestDemo {

    public static void main(String[] args) {
        String content = "<p>合同基本信息：</p><p>合同名称：${<span style=\"background:#B8B8B8\">合同名称<a style=\"display: none\">@@name##</a></span>}，&nbsp; 合同开始日期：${<span style=\"background:#B8B8B8\">开始日期<a style=\"display: none\">@@contractStartDate##</a></span>}&nbsp; 合同属性：${<span style=\"background:#B8B8B8\">合同属性<a style=\"display: none\">@@contractType##</a></span>}</p><p><br/></p><p><br/></p>";
        GetKeywordsUtils utils = new GetKeywordsUtils();
        List<String> results = utils.getKeywordsWithPattern(content,"${","}");
        for (String key : results) {
            System.out.println(key);
            List<String> values = utils.getKeywordsWithoutPattern(key,"@@","##");
            System.out.println(values.get(0));
            System.out.println();
        }
    }
}
