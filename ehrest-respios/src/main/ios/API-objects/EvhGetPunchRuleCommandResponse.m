//
// EvhGetPunchRuleCommandResponse.m
//
#import "EvhGetPunchRuleCommandResponse.h"
#import "EvhPunchRuleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetPunchRuleCommandResponse
//

@implementation EvhGetPunchRuleCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetPunchRuleCommandResponse* obj = [EvhGetPunchRuleCommandResponse new];
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
    if(self.punchRuleDTO) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.punchRuleDTO toJson: dic];
        
        [jsonObject setObject: dic forKey: @"punchRuleDTO"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"punchRuleDTO"];

        self.punchRuleDTO = [EvhPunchRuleDTO new];
        self.punchRuleDTO = [self.punchRuleDTO fromJson: itemJson];
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
