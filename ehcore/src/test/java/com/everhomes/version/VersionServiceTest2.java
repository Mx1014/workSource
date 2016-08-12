package com.everhomes.version;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.util.Version;
import com.everhomes.util.VersionRange;

public class VersionServiceTest2 extends CoreServerTestCase {

    @Autowired
    private VersionProvider versionProvider;
    
    @Test
    public void testBasics() {
        //
        // Test VersionRealm
        //
//        VersionRealm realmAndroid = new VersionRealm();
//        realmAndroid.setRealm("Andriod");
//        realmAndroid.setNamespaceId(nameSpaceId);
//        this.versionProvider.createVersionRealm(realmAndroid);
    	int namespaceId= 1000000;
//    	VersionRealm realmAndroid =this.versionProvider.findVersionRealmByName(namespaceId, "Andriod");
//    	VersionRealm realmIOS = this.versionProvider.findVersionRealmByName(namespaceId, "iOS");
    	VersionRange range = new VersionRange("[0.0.0,3.8.0)");
    	String targetVersion ="3.8.0";
        //
        // Test VersionUpgradeRule
        //
    	//android
        VersionUpgradeRule rule = new VersionUpgradeRule();
        //根据realm修改
        rule.setRealmId(1L);
        rule.setForceUpgrade((byte)0);
        rule.setTargetVersion(targetVersion);
        rule.setOrder(0);
        rule.setMatchingLowerBound(range.getLowerBound());
        rule.setMatchingUpperBound(range.getUpperBound());
//        rule.setNamespaceId(999990L);
        this.versionProvider.createVersionUpgradeRule(rule);
 
        VersionUrl ruleUrl = new VersionUrl();
        ruleUrl.setDownloadUrl("${homeurl}/web/download/apk/UFinePark-3.7.0.2016071202-release.apk");
        ruleUrl.setInfoUrl("${homeurl}/web/download/apk/andriod-UFinePark-3-7-0.html");
        ruleUrl.setRealmId(rule.getRealmId());
        ruleUrl.setTargetVersion(targetVersion);
        this.versionProvider.createVersionUrl(ruleUrl);
//        VersionRealm realmIOS = new VersionRealm();
//        realmIOS.setRealm("iOS");
//        realmIOS.setNamespaceId(nameSpaceId);
//        this.versionProvider.createVersionRealm(realmIOS);
        
        VersionUpgradeRule rule2 = new VersionUpgradeRule();
        //根据realm修改
        rule2.setRealmId(2L);
        rule2.setForceUpgrade((byte)0);
        rule2.setTargetVersion(targetVersion);
        rule2.setOrder(0); 
        rule2.setMatchingLowerBound(range.getLowerBound());
        rule2.setMatchingUpperBound(range.getUpperBound());
//        rule2.setNamespaceId(namespaceId);
        this.versionProvider.createVersionUpgradeRule(rule2);
        

        VersionUrl rule2Url = new VersionUrl();
//        rule2Url.setDownloadUrl("${homeurl}/web/download/apk/UFinePark-3.7.0.2016071202-release.apk");
        rule2Url.setInfoUrl("${homeurl}/web/download/apk/andriod-UFinePark-3-7-0.html");
        rule2Url.setRealmId(rule2.getRealmId());
        rule2Url.setTargetVersion(targetVersion);
        this.versionProvider.createVersionUrl(rule2Url);
    }
}
