//
// EvhOrganizationOrganizationType$1.m
//
#import "EvhOrganizationOrganizationType$1.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationOrganizationType$1
//

@implementation EvhOrganizationOrganizationType$1

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOrganizationOrganizationType$1* obj = [EvhOrganizationOrganizationType$1 new];
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
