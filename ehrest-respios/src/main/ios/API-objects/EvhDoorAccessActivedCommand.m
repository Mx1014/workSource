//
// EvhDoorAccessActivedCommand.m
//
#import "EvhDoorAccessActivedCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorAccessActivedCommand
//

@implementation EvhDoorAccessActivedCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDoorAccessActivedCommand* obj = [EvhDoorAccessActivedCommand new];
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
    if(self.doorId)
        [jsonObject setObject: self.doorId forKey: @"doorId"];
    if(self.hardwareId)
        [jsonObject setObject: self.hardwareId forKey: @"hardwareId"];
    if(self.time)
        [jsonObject setObject: self.time forKey: @"time"];
    if(self.content)
        [jsonObject setObject: self.content forKey: @"content"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.doorId = [jsonObject objectForKey: @"doorId"];
        if(self.doorId && [self.doorId isEqual:[NSNull null]])
            self.doorId = nil;

        self.hardwareId = [jsonObject objectForKey: @"hardwareId"];
        if(self.hardwareId && [self.hardwareId isEqual:[NSNull null]])
            self.hardwareId = nil;

        self.time = [jsonObject objectForKey: @"time"];
        if(self.time && [self.time isEqual:[NSNull null]])
            self.time = nil;

        self.content = [jsonObject objectForKey: @"content"];
        if(self.content && [self.content isEqual:[NSNull null]])
            self.content = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
