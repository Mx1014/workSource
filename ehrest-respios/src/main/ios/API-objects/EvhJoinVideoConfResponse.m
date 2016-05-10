//
// EvhJoinVideoConfResponse.m
//
#import "EvhJoinVideoConfResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhJoinVideoConfResponse
//

@implementation EvhJoinVideoConfResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhJoinVideoConfResponse* obj = [EvhJoinVideoConfResponse new];
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
    if(self.joinUrl)
        [jsonObject setObject: self.joinUrl forKey: @"joinUrl"];
    if(self.condId)
        [jsonObject setObject: self.condId forKey: @"condId"];
    if(self.password)
        [jsonObject setObject: self.password forKey: @"password"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.joinUrl = [jsonObject objectForKey: @"joinUrl"];
        if(self.joinUrl && [self.joinUrl isEqual:[NSNull null]])
            self.joinUrl = nil;

        self.condId = [jsonObject objectForKey: @"condId"];
        if(self.condId && [self.condId isEqual:[NSNull null]])
            self.condId = nil;

        self.password = [jsonObject objectForKey: @"password"];
        if(self.password && [self.password isEqual:[NSNull null]])
            self.password = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
