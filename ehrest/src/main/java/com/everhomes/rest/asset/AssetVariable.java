package com.everhomes.rest.asset;

/**
 * Created by Wentian on 2018/5/16.
 */
public enum AssetVariable {
  UNIT_PRICE("dj");
  private String identifier;
  AssetVariable(String identifier){
      this.identifier = identifier;
  }
  public String getIdentifier(){
      return this.identifier;
  }
}
