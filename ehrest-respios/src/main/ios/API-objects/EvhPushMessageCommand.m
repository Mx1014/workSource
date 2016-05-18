//
// EvhPushMessageCommand.m
//
#import "EvhPushMessageCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPushMessageCommand
//

@implementation EvhPushMessageCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPushMessageCommand* obj = [EvhPushMessageCommand new];
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
    if(self.deviceId)
        [jsonObject setObject: self.deviceId forKey: @"deviceId"];
    if(self.message)
        [jsonObject setObject: self.message forKey: @"message"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.deviceId = [jsonObject objectForKey: @"deviceId"];
        if(self.deviceId && [self.deviceId isEqual:[NSNull null]])
            self.deviceId = nil;

        self.message = [jsonObject objectForKey: @"message"];
        if(self.message && [self.message isEqual:[NSNull null]])
            self.message = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
