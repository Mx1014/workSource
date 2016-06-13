package com.everhomes.course;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.course.CourseDTO;
import com.everhomes.rest.course.CoursePostCommand;

@RestController
@RequestMapping("/course")
public class CourseController extends ControllerBase {

    @RequestMapping("post")
    @RestReturn(value=CourseDTO.class)
    public RestResponse post(@Valid CoursePostCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
