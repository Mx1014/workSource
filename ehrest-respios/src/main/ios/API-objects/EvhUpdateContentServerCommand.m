//
// EvhUpdateContentServerCommand.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
//
#import "EvhUpdateContentServerCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateContentServerCommand
//

@implementation EvhUpdateContentServerCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateContentServerCommand* obj = [EvhUpdateContentServerCommand new];
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
    [super toJson: jsonObject];
    if(self.serverId)
        [jsonObject setObject: self.serverId forKey: @"serverId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        [super fromJson: jsonObject];
        self.serverId = [jsonObject objectForKey: @"serverId"];
        if(self.serverId && [self.serverId isEqual:[NSNull null]])
            self.serverId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
