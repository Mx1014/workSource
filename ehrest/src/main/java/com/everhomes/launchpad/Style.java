// @formatter:off
package com.everhomes.launchpad;

/**
 * <ul>组内控件风格
 * <li>DEFAULT：默认</li>
 * <li>METRO: Metro</li>
 * <li>LIGHT: Light</li>
 * </ul>
 */
public enum Style {
    DEFAULT("Default"),METRO("Metro"),LIGHT("Light");
    
    private String code;
       
       private Style(String code) {
           this.code = code;
       }
       
       public String getCode() {
           return this.code;
       }
       
       public static Style fromCode(String code) {
           Style[] values = Style.values();
           for(Style value : values) {
               if(value.code.equals(code)) {
                   return value;
               }
           }
           
           return null;
       }
}
