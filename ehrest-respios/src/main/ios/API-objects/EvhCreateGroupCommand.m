//
// EvhCreateGroupCommand.m
//
#import "EvhCreateGroupCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateGroupCommand
//

@implementation EvhCreateGroupCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateGroupCommand* obj = [EvhCreateGroupCommand new];
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
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.avatar)
        [jsonObject setObject: self.avatar forKey: @"avatar"];
    if(self.visibilityScope)
        [jsonObject setObject: self.visibilityScope forKey: @"visibilityScope"];
    if(self.visibilityScopeId)
        [jsonObject setObject: self.visibilityScopeId forKey: @"visibilityScopeId"];
    if(self.privateFlag)
        [jsonObject setObject: self.privateFlag forKey: @"privateFlag"];
    if(self.categoryId)
        [jsonObject setObject: self.categoryId forKey: @"categoryId"];
    if(self.tag)
        [jsonObject setObject: self.tag forKey: @"tag"];
    if(self.postFlag)
        [jsonObject setObject: self.postFlag forKey: @"postFlag"];
    if(self.visibleRegionType)
        [jsonObject setObject: self.visibleRegionType forKey: @"visibleRegionType"];
    if(self.visibleRegionId)
        [jsonObject setObject: self.visibleRegionId forKey: @"visibleRegionId"];
    if(self.explicitRegionDescriptorsJson)
        [jsonObject setObject: self.explicitRegionDescriptorsJson forKey: @"explicitRegionDescriptorsJson"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.avatar = [jsonObject objectForKey: @"avatar"];
        if(self.avatar && [self.avatar isEqual:[NSNull null]])
            self.avatar = nil;

        self.visibilityScope = [jsonObject objectForKey: @"visibilityScope"];
        if(self.visibilityScope && [self.visibilityScope isEqual:[NSNull null]])
            self.visibilityScope = nil;

        self.visibilityScopeId = [jsonObject objectForKey: @"visibilityScopeId"];
        if(self.visibilityScopeId && [self.visibilityScopeId isEqual:[NSNull null]])
            self.visibilityScopeId = nil;

        self.privateFlag = [jsonObject objectForKey: @"privateFlag"];
        if(self.privateFlag && [self.privateFlag isEqual:[NSNull null]])
            self.privateFlag = nil;

        self.categoryId = [jsonObject objectForKey: @"categoryId"];
        if(self.categoryId && [self.categoryId isEqual:[NSNull null]])
            self.categoryId = nil;

        self.tag = [jsonObject objectForKey: @"tag"];
        if(self.tag && [self.tag isEqual:[NSNull null]])
            self.tag = nil;

        self.postFlag = [jsonObject objectForKey: @"postFlag"];
        if(self.postFlag && [self.postFlag isEqual:[NSNull null]])
            self.postFlag = nil;

        self.visibleRegionType = [jsonObject objectForKey: @"visibleRegionType"];
        if(self.visibleRegionType && [self.visibleRegionType isEqual:[NSNull null]])
            self.visibleRegionType = nil;

        self.visibleRegionId = [jsonObject objectForKey: @"visibleRegionId"];
        if(self.visibleRegionId && [self.visibleRegionId isEqual:[NSNull null]])
            self.visibleRegionId = nil;

        self.explicitRegionDescriptorsJson = [jsonObject objectForKey: @"explicitRegionDescriptorsJson"];
        if(self.explicitRegionDescriptorsJson && [self.explicitRegionDescriptorsJson isEqual:[NSNull null]])
            self.explicitRegionDescriptorsJson = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
