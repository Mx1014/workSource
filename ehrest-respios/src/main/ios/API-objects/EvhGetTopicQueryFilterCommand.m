//
// EvhGetTopicQueryFilterCommand.m
//
#import "EvhGetTopicQueryFilterCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetTopicQueryFilterCommand
//

@implementation EvhGetTopicQueryFilterCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetTopicQueryFilterCommand* obj = [EvhGetTopicQueryFilterCommand new];
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
    if(self.filterType)
        [jsonObject setObject: self.filterType forKey: @"filterType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.sceneToken = [jsonObject objectForKey: @"sceneToken"];
        if(self.sceneToken && [self.sceneToken isEqual:[NSNull null]])
            self.sceneToken = nil;

        self.filterType = [jsonObject objectForKey: @"filterType"];
        if(self.filterType && [self.filterType isEqual:[NSNull null]])
            self.filterType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
