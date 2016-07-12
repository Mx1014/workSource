//
// EvhDeleteNewsCommentBySceneCommand.m
//
#import "EvhDeleteNewsCommentBySceneCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteNewsCommentBySceneCommand
//

@implementation EvhDeleteNewsCommentBySceneCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteNewsCommentBySceneCommand* obj = [EvhDeleteNewsCommentBySceneCommand new];
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
    if(self.theNewsToken)
        [jsonObject setObject: self.theNewsToken forKey: @"newsToken"];
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.sceneToken = [jsonObject objectForKey: @"sceneToken"];
        if(self.sceneToken && [self.sceneToken isEqual:[NSNull null]])
            self.sceneToken = nil;

        self.theNewsToken = [jsonObject objectForKey: @"newsToken"];
        if(self.theNewsToken && [self.theNewsToken isEqual:[NSNull null]])
            self.theNewsToken = nil;

        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
