// @formatter:off
package com.everhomes.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SignupCommand {
    @Pattern(regexp = "mobile|email")
    @NotNull
    String type;
    
    @NotNull
    String token;
    
    public SignupCommand() {
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
