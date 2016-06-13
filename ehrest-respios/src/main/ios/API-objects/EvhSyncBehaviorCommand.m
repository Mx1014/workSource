//
// EvhSyncBehaviorCommand.m
//
#import "EvhSyncBehaviorCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSyncBehaviorCommand
//

@implementation EvhSyncBehaviorCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSyncBehaviorCommand* obj = [EvhSyncBehaviorCommand new];
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
    if(self.contentType)
        [jsonObject setObject: self.contentType forKey: @"contentType"];
    if(self.content)
        [jsonObject setObject: self.content forKey: @"content"];
    if(self.collectTimeMs)
        [jsonObject setObject: self.collectTimeMs forKey: @"collectTimeMs"];
    if(self.reportTimeMs)
        [jsonObject setObject: self.reportTimeMs forKey: @"reportTimeMs"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.contentType = [jsonObject objectForKey: @"contentType"];
        if(self.contentType && [self.contentType isEqual:[NSNull null]])
            self.contentType = nil;

        self.content = [jsonObject objectForKey: @"content"];
        if(self.content && [self.content isEqual:[NSNull null]])
            self.content = nil;

        self.collectTimeMs = [jsonObject objectForKey: @"collectTimeMs"];
        if(self.collectTimeMs && [self.collectTimeMs isEqual:[NSNull null]])
            self.collectTimeMs = nil;

        self.reportTimeMs = [jsonObject objectForKey: @"reportTimeMs"];
        if(self.reportTimeMs && [self.reportTimeMs isEqual:[NSNull null]])
            self.reportTimeMs = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
