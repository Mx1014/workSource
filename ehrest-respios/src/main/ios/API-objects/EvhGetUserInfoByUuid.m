//
// EvhGetUserInfoByUuid.m
//
#import "EvhGetUserInfoByUuid.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUserInfoByUuid
//

@implementation EvhGetUserInfoByUuid

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetUserInfoByUuid* obj = [EvhGetUserInfoByUuid new];
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
    if(self.uuid)
        [jsonObject setObject: self.uuid forKey: @"uuid"];
    if(self.timestamp)
        [jsonObject setObject: self.timestamp forKey: @"timestamp"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.uuid = [jsonObject objectForKey: @"uuid"];
        if(self.uuid && [self.uuid isEqual:[NSNull null]])
            self.uuid = nil;

        self.timestamp = [jsonObject objectForKey: @"timestamp"];
        if(self.timestamp && [self.timestamp isEqual:[NSNull null]])
            self.timestamp = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
