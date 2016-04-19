//
// EvhFamilyServiceErrorCode.m
// generated at 2016-04-19 12:41:53 
//
#import "EvhFamilyServiceErrorCode.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyServiceErrorCode
//

@implementation EvhFamilyServiceErrorCode

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFamilyServiceErrorCode* obj = [EvhFamilyServiceErrorCode new];
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
