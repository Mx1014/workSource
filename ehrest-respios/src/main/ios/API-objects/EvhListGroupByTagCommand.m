//
// EvhListGroupByTagCommand.m
//
#import "EvhListGroupByTagCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListGroupByTagCommand
//

@implementation EvhListGroupByTagCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListGroupByTagCommand* obj = [EvhListGroupByTagCommand new];
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
    if(self.searchVisibilityScope)
        [jsonObject setObject: self.searchVisibilityScope forKey: @"searchVisibilityScope"];
    if(self.searchVisibilityScopeId)
        [jsonObject setObject: self.searchVisibilityScopeId forKey: @"searchVisibilityScopeId"];
    if(self.tag)
        [jsonObject setObject: self.tag forKey: @"tag"];
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.searchVisibilityScope = [jsonObject objectForKey: @"searchVisibilityScope"];
        if(self.searchVisibilityScope && [self.searchVisibilityScope isEqual:[NSNull null]])
            self.searchVisibilityScope = nil;

        self.searchVisibilityScopeId = [jsonObject objectForKey: @"searchVisibilityScopeId"];
        if(self.searchVisibilityScopeId && [self.searchVisibilityScopeId isEqual:[NSNull null]])
            self.searchVisibilityScopeId = nil;

        self.tag = [jsonObject objectForKey: @"tag"];
        if(self.tag && [self.tag isEqual:[NSNull null]])
            self.tag = nil;

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
