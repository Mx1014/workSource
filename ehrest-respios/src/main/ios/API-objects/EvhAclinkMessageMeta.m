//
// EvhAclinkMessageMeta.m
//
#import "EvhAclinkMessageMeta.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkMessageMeta
//

@implementation EvhAclinkMessageMeta

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAclinkMessageMeta* obj = [EvhAclinkMessageMeta new];
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
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.doorId)
        [jsonObject setObject: self.doorId forKey: @"doorId"];
    if(self.doorType)
        [jsonObject setObject: self.doorType forKey: @"doorType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.doorId = [jsonObject objectForKey: @"doorId"];
        if(self.doorId && [self.doorId isEqual:[NSNull null]])
            self.doorId = nil;

        self.doorType = [jsonObject objectForKey: @"doorType"];
        if(self.doorType && [self.doorType isEqual:[NSNull null]])
            self.doorType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
