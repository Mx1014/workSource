//
// EvhPushJumpObject.m
//
#import "EvhPushJumpObject.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPushJumpObject
//

@implementation EvhPushJumpObject

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPushJumpObject* obj = [EvhPushJumpObject new];
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
    if(self.jump)
        [jsonObject setObject: self.jump forKey: @"jump"];
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.jump = [jsonObject objectForKey: @"jump"];
        if(self.jump && [self.jump isEqual:[NSNull null]])
            self.jump = nil;

        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
