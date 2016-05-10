//
// EvhVerifyPersonnelByPhoneCommandResponse.m
//
#import "EvhVerifyPersonnelByPhoneCommandResponse.h"
#import "EvhOrganizationMemberDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyPersonnelByPhoneCommandResponse
//

@implementation EvhVerifyPersonnelByPhoneCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhVerifyPersonnelByPhoneCommandResponse* obj = [EvhVerifyPersonnelByPhoneCommandResponse new];
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
    if(self.dto) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.dto toJson: dic];
        
        [jsonObject setObject: dic forKey: @"dto"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"dto"];

        self.dto = [EvhOrganizationMemberDTO new];
        self.dto = [self.dto fromJson: itemJson];
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
