//
// EvhFindAutoAssignRentalSiteWeekStatusCommand.m
//
#import "EvhFindAutoAssignRentalSiteWeekStatusCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindAutoAssignRentalSiteWeekStatusCommand
//

@implementation EvhFindAutoAssignRentalSiteWeekStatusCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFindAutoAssignRentalSiteWeekStatusCommand* obj = [EvhFindAutoAssignRentalSiteWeekStatusCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.siteId)
        [jsonObject setObject: self.siteId forKey: @"siteId"];
    if(self.ruleDate)
        [jsonObject setObject: self.ruleDate forKey: @"ruleDate"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.siteId = [jsonObject objectForKey: @"siteId"];
        if(self.siteId && [self.siteId isEqual:[NSNull null]])
            self.siteId = nil;

        self.ruleDate = [jsonObject objectForKey: @"ruleDate"];
        if(self.ruleDate && [self.ruleDate isEqual:[NSNull null]])
            self.ruleDate = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
