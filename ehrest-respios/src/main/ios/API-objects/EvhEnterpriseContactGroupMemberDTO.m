//
// EvhEnterpriseContactGroupMemberDTO.m
//
#import "EvhEnterpriseContactGroupMemberDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseContactGroupMemberDTO
//

@implementation EvhEnterpriseContactGroupMemberDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhEnterpriseContactGroupMemberDTO* obj = [EvhEnterpriseContactGroupMemberDTO new];
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
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
