//
// EvhGetEntranceByPrivilegeResponse.m
//
#import "EvhGetEntranceByPrivilegeResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetEntranceByPrivilegeResponse
//

@implementation EvhGetEntranceByPrivilegeResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetEntranceByPrivilegeResponse* obj = [EvhGetEntranceByPrivilegeResponse new];
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
    if(self.entrancePrivilege)
        [jsonObject setObject: self.entrancePrivilege forKey: @"entrancePrivilege"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.entrancePrivilege = [jsonObject objectForKey: @"entrancePrivilege"];
        if(self.entrancePrivilege && [self.entrancePrivilege isEqual:[NSNull null]])
            self.entrancePrivilege = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
