//
// EvhListOweFamilysByConditionsCommand.m
//
#import "EvhListOweFamilysByConditionsCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOweFamilysByConditionsCommand
//

@implementation EvhListOweFamilysByConditionsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListOweFamilysByConditionsCommand* obj = [EvhListOweFamilysByConditionsCommand new];
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
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.lastPayDate)
        [jsonObject setObject: self.lastPayDate forKey: @"lastPayDate"];
    if(self.organizationId)
        [jsonObject setObject: self.organizationId forKey: @"organizationId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.lastPayDate = [jsonObject objectForKey: @"lastPayDate"];
        if(self.lastPayDate && [self.lastPayDate isEqual:[NSNull null]])
            self.lastPayDate = nil;

        self.organizationId = [jsonObject objectForKey: @"organizationId"];
        if(self.organizationId && [self.organizationId isEqual:[NSNull null]])
            self.organizationId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
