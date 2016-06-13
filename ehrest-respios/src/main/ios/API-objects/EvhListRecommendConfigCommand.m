//
// EvhListRecommendConfigCommand.m
//
#import "EvhListRecommendConfigCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListRecommendConfigCommand
//

@implementation EvhListRecommendConfigCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListRecommendConfigCommand* obj = [EvhListRecommendConfigCommand new];
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
    if(self.appId)
        [jsonObject setObject: self.appId forKey: @"appId"];
    if(self.suggestType)
        [jsonObject setObject: self.suggestType forKey: @"suggestType"];
    if(self.sourceType)
        [jsonObject setObject: self.sourceType forKey: @"sourceType"];
    if(self.targetType)
        [jsonObject setObject: self.targetType forKey: @"targetType"];
    if(self.sourceId)
        [jsonObject setObject: self.sourceId forKey: @"sourceId"];
    if(self.targetId)
        [jsonObject setObject: self.targetId forKey: @"targetId"];
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.appId = [jsonObject objectForKey: @"appId"];
        if(self.appId && [self.appId isEqual:[NSNull null]])
            self.appId = nil;

        self.suggestType = [jsonObject objectForKey: @"suggestType"];
        if(self.suggestType && [self.suggestType isEqual:[NSNull null]])
            self.suggestType = nil;

        self.sourceType = [jsonObject objectForKey: @"sourceType"];
        if(self.sourceType && [self.sourceType isEqual:[NSNull null]])
            self.sourceType = nil;

        self.targetType = [jsonObject objectForKey: @"targetType"];
        if(self.targetType && [self.targetType isEqual:[NSNull null]])
            self.targetType = nil;

        self.sourceId = [jsonObject objectForKey: @"sourceId"];
        if(self.sourceId && [self.sourceId isEqual:[NSNull null]])
            self.sourceId = nil;

        self.targetId = [jsonObject objectForKey: @"targetId"];
        if(self.targetId && [self.targetId isEqual:[NSNull null]])
            self.targetId = nil;

        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
