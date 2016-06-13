//
// EvhListContactsBySceneCommand.m
//
#import "EvhListContactsBySceneCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListContactsBySceneCommand
//

@implementation EvhListContactsBySceneCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListContactsBySceneCommand* obj = [EvhListContactsBySceneCommand new];
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
    if(self.sceneToken)
        [jsonObject setObject: self.sceneToken forKey: @"sceneToken"];
    if(self.isSignedup)
        [jsonObject setObject: self.isSignedup forKey: @"isSignedup"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.sceneToken = [jsonObject objectForKey: @"sceneToken"];
        if(self.sceneToken && [self.sceneToken isEqual:[NSNull null]])
            self.sceneToken = nil;

        self.isSignedup = [jsonObject objectForKey: @"isSignedup"];
        if(self.isSignedup && [self.isSignedup isEqual:[NSNull null]])
            self.isSignedup = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
