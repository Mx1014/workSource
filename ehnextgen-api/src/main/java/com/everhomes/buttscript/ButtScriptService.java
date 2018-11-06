package com.everhomes.buttscript;

import com.everhomes.rest.buttscript.*;

/**
 *
 * @author huanglm
 *
 */
public interface ButtScriptService {

    /**
     * 按域空间查询脚本信息接口
     * @param cmd
     * @return
     */
    ScriptDTO getScriptByNamespace(GetScriptCommand cmd );


    /**
     * 2)保存脚本信息接口(含保存并发布)
     * @param cmd
     * @return
     */
    ScriptVersionInfoDTO saveScript(SaveScriptCommand cmd );

    /**
     * 3)通过域空间查询所有版本脚本信息接口
     * @param cmd
     * @return
     */
    ScriptVersionInfoResponse findScriptVersionInfoByNamespaceId(FindScriptVersionInfoCommand  cmd );


    /**
     * 4)发布版本接口
     * @param cmd
     */
    void publishScriptVersion( PublishScriptVersionCommand  cmd );

    /**
     * 5)查询域空间下脚本分类接口
     * @param cmd
     * @return
     */
    ScriptnInfoTypeResponse findScriptInfoType( FindScriptInfoTypeCommand  cmd );

    /**
     * 6)取消版本发布接口
     * @param cmd
     */
    void publishScriptVersionCancel( PublishScriptVersionCancelCommand  cmd );
}
