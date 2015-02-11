// @formatter:off
package com.everhomes.message;

public class GreetingMessage {
    String greeting;

    public GreetingMessage(String greeting) {
        this.greeting = greeting;
    }
    
    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}
