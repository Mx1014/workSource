// @formatter:off
package com.everhomes.salary;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.salary.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/salary")
public class SalaryController extends ControllerBase {
	
	@Autowired
	private SalaryService salaryService;
	
	/**
	 * <p>1.查询基础项目设置</p>
	 * <b>URL: /salary/listSalaryDefaultEntities</b>
	 */
	@RequestMapping("listSalaryDefaultEntities")
	@RestReturn(ListSalaryDefaultEntitiesResponse.class)
	public RestResponse listSalaryDefaultEntities(){
		ListSalaryDefaultEntitiesResponse res = this.salaryService.listSalaryDefaultEntities();
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>2-1.新增、更改 薪酬组(包含的选项 + 更改组名)</p>
	 * <b>URL: /salary/addSalaryGroup</b>
	 */
	@RequestMapping("addSalaryGroup")
	@RestReturn(AddSalaryGroupResponse.class)
	public RestResponse addSalaryGroup(AddSalaryGroupCommand cmd){
	    AddSalaryGroupResponse res = this.salaryService.addSalaryGroup(cmd);
	    RestResponse response = new RestResponse(res);
	    response.setErrorCode(ErrorCodes.SUCCESS);
	    response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>2-2.更改薪酬组包含的选项+更改组名</p>
	 * <b>URL: /salary/updateSalaryGroup</b>
	 */
	@RequestMapping("updateSalaryGroup")
	@RestReturn(UpdateSalaryGroupResponse.class)
	public RestResponse updateSalaryGroup(UpdateSalaryGroupCommand cmd){
        AddSalaryGroupResponse res = this.salaryService.updateSalaryGroup(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}

    /**
     * <p>2-3.删除薪酬组</p>
     * <b>URL: /salary/deleteSalaryGroup</b>
     */
    @RequestMapping("deleteSalaryGroup")
    @RestReturn(String.class)
    public RestResponse deleteSalaryGroup(DeleteSalaryGroupCommand cmd) {
        this.salaryService.deleteSalaryGroup(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
	 * <p>2-4.复制薪酬组</p>
	 * <b>URL: /salary/copySalaryGroup</b>
	 */
	@RequestMapping("copySalaryGroup")
	@RestReturn(String.class)
	public RestResponse copySalaryGroup(CopySalaryGroupCommand cmd){
		this.salaryService.copySalaryGroup(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

    /**
     * <p>2-5.展示薪酬组详细批次</p>
     * <b>URL: /salary/getSalaryGroup</b>
     */
    @RequestMapping("getSalaryGroup")
    @RestReturn(GetSalaryGroupResponse.class)
    public RestResponse getSalaryGroup(GetSalaryGroupCommand cmd){
        GetSalaryGroupResponse res = this.salaryService.getSalaryGroup(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

	/**
	 * <p>2-6.展示所有薪酬组</p>
	 * <b>URL: /salary/listSalaryGroup</b>
	 */
	@RequestMapping("listSalaryGroup")
	@RestReturn(ListSalaryGroupResponse.class)
	public RestResponse listSalaryGroup(ListSalaryGroupCommand cmd){
        ListSalaryGroupResponse res = this.salaryService.listSalaryGroup(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}

	/**
	 * <p>3-1.根据组织架构/薪酬组/异常状态 查询人员</p>
	 * <b>URL: /salary/listSalaryEmployees</b>
	 */
	@RequestMapping("listSalaryEmployees")
	@RestReturn(ListSalaryEmployeesResponse.class)
	public RestResponse listSalaryEmployees(ListSalaryEmployeesCommand cmd){
        ListSalaryEmployeesResponse res = this.salaryService.listSalaryEmployees(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
		return response;
	}

    /**
     * <p>3-2.查询人员批次详情</p>
     * <b>URL: /salary/getSalaryEmployees</b>
     */
    @RequestMapping("getSalaryEmployees")
    @RestReturn(value = SalaryEmployeeOriginValDTO.class, collection = true)
    public RestResponse getSalaryEmployees(GetSalaryEmployeesCommand cmd){
        List<SalaryEmployeeOriginValDTO> res = this.salaryService.getSalaryEmployees(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>3-3.编辑人员批次详情,有增加没有更新</p>
     * <b>URL: /salary/updateSalaryEmployees</b>
     */
    @RequestMapping("updateSalaryEmployees")
    @RestReturn(value = String.class)
    public RestResponse updateSalaryEmployees(UpdateSalaryEmployeesCommand cmd){
        this.salaryService.updateSalaryEmployees(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /*
	*//**
	 * <p>3-4.设置人员薪酬字段值 有增加没有更新</p>
	 * <b>URL: /salary/saveSalaryEmployeeOriginVals</b>
	 *//*
	@RequestMapping("saveSalaryEmployeeOriginVals")
	@RestReturn(String.class)
	public RestResponse saveSalaryEmployeeOriginVals(SaveSalaryEmployeeOriginValsCommand cmd){
		salaryService.saveSalaryEmployeeOriginVals(cmd);
		return new RestResponse();
	}*/

	/**
	 * <p>4-1.导出某个薪酬组excel</p>
	 * <b>URL: /salary/exportSalaryGroup</b>
	 */
	@RequestMapping("exportSalaryGroup")
	@RestReturn(String.class)
	public RestResponse exportSalaryGroup(ExportSalaryGroupCommand cmd, HttpServletResponse httpResponse){
		salaryService.exportSalaryGroup(cmd,httpResponse);
		return new RestResponse();
	}

	/**
	 * <p>4-2.导入员工薪酬表</p>
	 * <b>URL: /salary/importSalaryGroup</b>
	 */
	@RequestMapping("importSalaryGroup")
	@RestReturn(ImportFileTaskDTO.class)
	public RestResponse importSalaryGroup(ImportSalaryGroupCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files){
        User user = UserContext.current().getUser();
//        Long userId = manaUser.getId();
/*        if (null == files || null == files[0]) {
            LOGGER.error("files is null, userId=" + userId);
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
                    "files is null");
        }*/
        RestResponse response = new RestResponse(this.salaryService.importSalaryGroup(files[0], user.getId(),cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}

	/**
	 * <p>5-1.根据批次导出核算表</p>
	 * <b>URL: /salary/exportPeriodSalary</b>
	 */
	@RequestMapping("exportPeriodSalary")
	@RestReturn(String.class)
	public RestResponse exportPeriodSalary(ExportPeriodSalaryCommand cmd, HttpServletResponse httpResponse){
		salaryService.exportPeriodSalary(cmd);
		return new RestResponse();
	}

	/**
	 * <p>5-2.根据批次导入核算表</p>
	 * <b>URL: /salary/importPeriodSalary</b>
	 */
	@RequestMapping("importPeriodSalary")
	@RestReturn(String.class)
	public RestResponse importPeriodSalary(ImportPeriodSalaryCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files){
		salaryService.importPeriodSalary(cmd);
		return new RestResponse();
	}

	/**
	 * <p>12.查询异常员工人数</p>
	 * <b>URL: /salary/getAbnormalEmployeeNumber</b>
	 */
	@RequestMapping("getAbnormalEmployeeNumber")
	@RestReturn(GetAbnormalEmployeeNumberResponse.class)
	public RestResponse getAbnormalEmployeeNumber(GetAbnormalEmployeeNumberCommand cmd){
		return new RestResponse(salaryService.getAbnormalEmployeeNumber(cmd));
	}

	/**
	 * <p>13.查询谋期的(审核/发放) 批次列表</p>
	 * <b>URL: /salary/listPeriodSalary</b>
	 */
	@RequestMapping("listPeriodSalary")
	@RestReturn(ListPeriodSalaryResponse.class)
	public RestResponse listPeriodSalary(ListPeriodSalaryCommand cmd){
		return new RestResponse(salaryService.listPeriodSalary(cmd));
	}

	/**
	 * <p>14.查询某个批次的核算列表</p>
	 * <b>URL: /salary/listPeriodSalaryEmployees</b>
	 */
	@RequestMapping("listPeriodSalaryEmployees")
	@RestReturn(ListPeriodSalaryEmployeesResponse.class)
	public RestResponse listPeriodSalaryEmployees(ListPeriodSalaryEmployeesCommand cmd){
		return new RestResponse(salaryService.listPeriodSalaryEmployees(cmd));
	}

	/**
	 * <p>15.保存某个批次的核算后数值和更改状态</p>
	 * <b>URL: /salary/updatePeriodSalaryEmployee</b>
	 */
	@RequestMapping("updatePeriodSalaryEmployee")
	@RestReturn(String.class)
	public RestResponse updatePeriodSalaryEmployee(UpdatePeriodSalaryEmployeeCommand cmd){
		salaryService.updatePeriodSalaryEmployee(cmd);
		return new RestResponse();
	}

	/**
	 * <p>15-2.保存某个批次的核算后数值和更改状态</p>
	 * <b>URL: /salary/batchSetEmployeeCheckFlag</b>
	 */
	@RequestMapping("batchSetEmployeeCheckFlag")
	@RestReturn(String.class)
	public RestResponse batchSetEmployeeCheckFlag(BatchSetEmployeeCheckFlagCommand cmd){
		salaryService.batchSetEmployeeCheckFlag(cmd);
		return new RestResponse();
	}
	/**
	 * <p>16.设置某期薪酬批次核算完成</p>
	 * <b>URL: /salary/checkPeriodSalary</b>
	 */
	@RequestMapping("checkPeriodSalary")
	@RestReturn(String.class)
	public RestResponse checkPeriodSalary(CheckPeriodSalaryCommand cmd){
		salaryService.checkPeriodSalary(cmd);
		return new RestResponse();
	}

	/**
	 * <p>17.查询某批次工资条内容(说明+字段)</p>
	 * <b>URL: /salary/getPeriodSalaryEmailContent</b>
	 */
	@RequestMapping("getPeriodSalaryEmailContent")
	@RestReturn(GetPeriodSalaryEmailContentResponse.class)
	public RestResponse getPeriodSalaryEmailContent(GetPeriodSalaryEmailContentCommand cmd){
		return new RestResponse(salaryService.getPeriodSalaryEmailContent(cmd));
	}

	/**
	 * <p>18.设置工资条邮件内容</p>
	 * <b>URL: /salary/setSalaryEmailContent</b>
	 */
	@RequestMapping("setSalaryEmailContent")
	@RestReturn(String.class)
	public RestResponse setSalaryEmailContent(SetSalaryEmailContentCommand cmd){
		salaryService.setSalaryEmailContent(cmd);
		return new RestResponse();
	}

	/**
	 * <p>19.设置工资条字段项显示状态</p>
	 * <b>URL: /salary/updateSalaryGroupEntitiesVisable</b>
	 */
	@RequestMapping("updateSalaryGroupEntitiesVisable")
	@RestReturn(String.class)
	public RestResponse updateSalaryGroupEntitiesVisable(UpdateSalaryGroupEntitiesVisableCommand cmd){
		salaryService.updateSalaryGroupEntitiesVisable(cmd);
		return new RestResponse();
	}

	/**
	 * <p>20.发送某期薪酬批次</p>
	 * <b>URL: /salary/sendPeriodSalary</b>
	 */
	@RequestMapping("sendPeriodSalary")
	@RestReturn(String.class)
	public RestResponse sendPeriodSalary(SendPeriodSalaryCommand cmd){
		salaryService.sendPeriodSalary(cmd);
		return new RestResponse();
	}
	/**
	 * <p>20.撤销发送某期薪酬批次</p>
	 * <b>URL: /salary/revokeSendPeriodSalary</b>
	 */
	@RequestMapping("revokeSendPeriodSalary")
	@RestReturn(String.class)
	public RestResponse revokeSendPeriodSalary(SendPeriodSalaryCommand cmd){
		salaryService.revokeSendPeriodSalary(cmd);
		return new RestResponse();
	}

	/**
	 * <p>21.查发放历史</p>
	 * <b>URL: /salary/listSalarySendHistory</b>
	 */
	@RequestMapping("listSalarySendHistory")
	@RestReturn(ListSalarySendHistoryResponse.class)
	public RestResponse listSalarySendHistory(ListSalarySendHistoryCommand cmd){
		return new RestResponse(salaryService.listSalarySendHistory(cmd));
	}

	/**
	 * <p>22.导出发放历史</p>
	 * <b>URL: /salary/exportSalarySendHistory</b>
	 */
	@RequestMapping("exportSalarySendHistory")
	@RestReturn(String.class)
	public RestResponse exportSalarySendHistory(ExportSalarySendHistoryCommand cmd, HttpServletResponse httpResponse){
		salaryService.exportSalarySendHistory(cmd,httpResponse);
		return new RestResponse();
	}

}