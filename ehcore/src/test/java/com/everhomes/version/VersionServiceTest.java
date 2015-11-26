package com.everhomes.version;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.util.Version;
import com.everhomes.util.VersionRange;

public class VersionServiceTest extends CoreServerTestCase {

    @Autowired
    private VersionProvider versionProvider;
    
    @Test
    public void testBasics() {
        //
        // Test VersionRealm
        //
        VersionRealm realm = new VersionRealm();
        realm.setRealm("Andriod");
        
        this.versionProvider.createVersionRealm(realm);
        Assert.assertTrue(realm.getId() != null);
        
        VersionRealm realmFound = this.versionProvider.findVersionRealmByName(0, "Andriod");
        Assert.assertTrue(realmFound != null);
        Assert.assertTrue(realmFound.getId().longValue() == realm.getId().longValue());

        realmFound = this.versionProvider.findVersionRealmById(realm.getId().longValue());
        Assert.assertTrue(realmFound != null);
        
        List<VersionRealm> realms = this.versionProvider.listVersionRealm();
        Assert.assertTrue(realms.size() == 1);
        
        //
        // Test VersionUpgradeRule
        //
        VersionUpgradeRule rule = new VersionUpgradeRule();
        rule.setRealmId(realm.getId());
        rule.setForceUpgrade((byte)1);
        rule.setTargetVersion("2.0.0");
        rule.setOrder(0);
        VersionRange range = new VersionRange("[1.0.0,2.0.0)");
        rule.setMatchingLowerBound(range.getLowerBound());
        rule.setMatchingUpperBound(range.getUpperBound());
        this.versionProvider.createVersionUpgradeRule(rule);
        
        rule = new VersionUpgradeRule();
        rule.setRealmId(realm.getId());
        rule.setForceUpgrade((byte)1);
        rule.setTargetVersion("4.0.0");
        rule.setOrder(1);
        range = new VersionRange("[2.0.0,4.0.0)");
        rule.setMatchingLowerBound(range.getLowerBound());
        rule.setMatchingUpperBound(range.getUpperBound());
        this.versionProvider.createVersionUpgradeRule(rule);

        VersionUpgradeRule matchedRule = this.versionProvider.matchVersionUpgradeRule(0, realm.getId(), new Version(1, 0, 1));
        Assert.assertTrue(matchedRule != null);
        Version targetVersion = Version.fromVersionString(matchedRule.getTargetVersion());
        Assert.assertTrue(targetVersion != null);
        Assert.assertTrue(targetVersion.getMajor() == 2);
        
        matchedRule = this.versionProvider.matchVersionUpgradeRule(0, realm.getId(), new Version(2, 0, 0));
        Assert.assertTrue(matchedRule != null);
        targetVersion = Version.fromVersionString(matchedRule.getTargetVersion());
        Assert.assertTrue(targetVersion != null);
        Assert.assertTrue(targetVersion.getMajor() == 4);

        matchedRule = this.versionProvider.matchVersionUpgradeRule(0, realm.getId(), new Version(5, 0, 0));
        Assert.assertTrue(matchedRule == null);
        
        List<VersionUpgradeRule> rules = this.versionProvider.listVersionUpgradeRules(realm.getId());
        Assert.assertTrue(rules.size() == 2);
        
        //
        // Test VersionedContent
        //
        VersionedContent content = new VersionedContent();
        content.setRealmId(realm.getId());
        content.setOrder(0);
        content.setContent("Ver 1.x.x content");
        range = new VersionRange("[1.0.0,2.0.0)");
        content.setMatchingLowerBound(range.getLowerBound());
        content.setMatchingUpperBound(range.getUpperBound());
        this.versionProvider.createVersionedContent(content);
        
        content = new VersionedContent();
        content.setRealmId(realm.getId());
        content.setOrder(1);
        content.setContent("Ver 2.x.x - 4.x.x content");
        range = new VersionRange("[2.0.0,4.0.0)");
        content.setMatchingLowerBound(range.getLowerBound());
        content.setMatchingUpperBound(range.getUpperBound());
        this.versionProvider.createVersionedContent(content);

        VersionedContent matchedContent = this.versionProvider.matchVersionedContent(realm.getId(), new Version(1, 0, 1));
        Assert.assertTrue(matchedContent != null);
        Assert.assertTrue(matchedContent.getContent().equals("Ver 1.x.x content"));
        
        matchedContent = this.versionProvider.matchVersionedContent(realm.getId(), new Version(2, 0, 0));
        Assert.assertTrue(matchedContent != null);
        Assert.assertTrue(matchedContent.getContent().equals("Ver 2.x.x - 4.x.x content"));
        
        this.versionProvider.deleteVersionRealm(realm);
    }
}
