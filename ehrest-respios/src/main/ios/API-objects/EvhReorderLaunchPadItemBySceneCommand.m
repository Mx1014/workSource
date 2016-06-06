//
// EvhReorderLaunchPadItemBySceneCommand.m
//
#import "EvhReorderLaunchPadItemBySceneCommand.h"
#import "EvhLaunchPadItemSort.h"

///////////////////////////////////////////////////////////////////////////////
// EvhReorderLaunchPadItemBySceneCommand
//

@implementation EvhReorderLaunchPadItemBySceneCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhReorderLaunchPadItemBySceneCommand* obj = [EvhReorderLaunchPadItemBySceneCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _sorts = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.sceneToken)
        [jsonObject setObject: self.sceneToken forKey: @"sceneToken"];
    if(self.sorts) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhLaunchPadItemSort* item in self.sorts) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"sorts"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.sceneToken = [jsonObject objectForKey: @"sceneToken"];
        if(self.sceneToken && [self.sceneToken isEqual:[NSNull null]])
            self.sceneToken = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"sorts"];
            for(id itemJson in jsonArray) {
                EvhLaunchPadItemSort* item = [EvhLaunchPadItemSort new];
                
                [item fromJson: itemJson];
                [self.sorts addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
