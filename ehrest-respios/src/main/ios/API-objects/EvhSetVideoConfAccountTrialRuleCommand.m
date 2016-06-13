//
// EvhSetVideoConfAccountTrialRuleCommand.m
//
#import "EvhSetVideoConfAccountTrialRuleCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetVideoConfAccountTrialRuleCommand
//

@implementation EvhSetVideoConfAccountTrialRuleCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSetVideoConfAccountTrialRuleCommand* obj = [EvhSetVideoConfAccountTrialRuleCommand new];
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
    if(self.accounts)
        [jsonObject setObject: self.accounts forKey: @"accounts"];
    if(self.months)
        [jsonObject setObject: self.months forKey: @"months"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.accounts = [jsonObject objectForKey: @"accounts"];
        if(self.accounts && [self.accounts isEqual:[NSNull null]])
            self.accounts = nil;

        self.months = [jsonObject objectForKey: @"months"];
        if(self.months && [self.months isEqual:[NSNull null]])
            self.months = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
