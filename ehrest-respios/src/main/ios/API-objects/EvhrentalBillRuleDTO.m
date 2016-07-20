//
// EvhrentalBillRuleDTO.m
//
#import "EvhrentalBillRuleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhrentalBillRuleDTO
//

@implementation EvhrentalBillRuleDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhrentalBillRuleDTO* obj = [EvhrentalBillRuleDTO new];
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
    if(self.ruleId)
        [jsonObject setObject: self.ruleId forKey: @"ruleId"];
    if(self.rentalCount)
        [jsonObject setObject: self.rentalCount forKey: @"rentalCount"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ruleId = [jsonObject objectForKey: @"ruleId"];
        if(self.ruleId && [self.ruleId isEqual:[NSNull null]])
            self.ruleId = nil;

        self.rentalCount = [jsonObject objectForKey: @"rentalCount"];
        if(self.rentalCount && [self.rentalCount isEqual:[NSNull null]])
            self.rentalCount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
