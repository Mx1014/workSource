//
// EvhCheckOfficalPrivilegeResponse.m
//
#import "EvhCheckOfficalPrivilegeResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCheckOfficalPrivilegeResponse
//

@implementation EvhCheckOfficalPrivilegeResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCheckOfficalPrivilegeResponse* obj = [EvhCheckOfficalPrivilegeResponse new];
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
    if(self.officialFlag)
        [jsonObject setObject: self.officialFlag forKey: @"officialFlag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.officialFlag = [jsonObject objectForKey: @"officialFlag"];
        if(self.officialFlag && [self.officialFlag isEqual:[NSNull null]])
            self.officialFlag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
