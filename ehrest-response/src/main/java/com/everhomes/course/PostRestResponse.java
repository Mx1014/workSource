// @formatter:off
// generated at 2015-12-04 14:52:02
package com.everhomes.course;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.course.CourseDTO;

public class PostRestResponse extends RestResponseBase {

    private CourseDTO response;

    public PostRestResponse () {
    }

    public CourseDTO getResponse() {
        return response;
    }

    public void setResponse(CourseDTO response) {
        this.response = response;
    }
}
