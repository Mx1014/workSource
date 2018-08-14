// @formatter:off
package com.everhomes.zhenzhihui;

import com.everhomes.user.UserLogin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ZhenZhiHuiService {
    String ssoService(HttpServletRequest request, HttpServletResponse response);
}
