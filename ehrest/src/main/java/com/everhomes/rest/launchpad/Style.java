// @formatter:off
package com.everhomes.rest.launchpad;

/**
 * <ul>组内控件风格
 * <li>DEFAULT("Default")：默认</li>
 * <li>METRO("Metro"): Metro</li>
 * <li>LIGHT("Light"): Light</li>
 * <li>GALLERY("Gallery"): Gallery</li>
 * <li>COLLECTION("Collection"): Collection</li>
 * </ul>
 */
public enum Style {
    DEFAULT("Default"),METRO("Metro"),LIGHT("Light"),GALLERY("Gallery"),COLLECTION("Collection");
    
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
