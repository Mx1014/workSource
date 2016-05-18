//
// EvhExtendedVideoConfAccountPeriodCommand.m
//
#import "EvhExtendedVideoConfAccountPeriodCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhExtendedVideoConfAccountPeriodCommand
//

@implementation EvhExtendedVideoConfAccountPeriodCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhExtendedVideoConfAccountPeriodCommand* obj = [EvhExtendedVideoConfAccountPeriodCommand new];
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
    if(self.accountId)
        [jsonObject setObject: self.accountId forKey: @"accountId"];
    if(self.validDate)
        [jsonObject setObject: self.validDate forKey: @"validDate"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.accountId = [jsonObject objectForKey: @"accountId"];
        if(self.accountId && [self.accountId isEqual:[NSNull null]])
            self.accountId = nil;

        self.validDate = [jsonObject objectForKey: @"validDate"];
        if(self.validDate && [self.validDate isEqual:[NSNull null]])
            self.validDate = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
