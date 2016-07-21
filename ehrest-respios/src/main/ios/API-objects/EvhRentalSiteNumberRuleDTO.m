//
// EvhRentalSiteNumberRuleDTO.m
//
#import "EvhRentalSiteNumberRuleDTO.h"
#import "EvhRentalv2RentalSiteRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalSiteNumberRuleDTO
//

@implementation EvhRentalSiteNumberRuleDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalSiteNumberRuleDTO* obj = [EvhRentalSiteNumberRuleDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _siteRules = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.siteNumber)
        [jsonObject setObject: self.siteNumber forKey: @"siteNumber"];
    if(self.siteRules) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalv2RentalSiteRulesDTO* item in self.siteRules) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"siteRules"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.siteNumber = [jsonObject objectForKey: @"siteNumber"];
        if(self.siteNumber && [self.siteNumber isEqual:[NSNull null]])
            self.siteNumber = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"siteRules"];
            for(id itemJson in jsonArray) {
                EvhRentalv2RentalSiteRulesDTO* item = [EvhRentalv2RentalSiteRulesDTO new];
                
                [item fromJson: itemJson];
                [self.siteRules addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
