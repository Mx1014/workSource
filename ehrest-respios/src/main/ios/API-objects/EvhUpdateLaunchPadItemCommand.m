//
// EvhUpdateLaunchPadItemCommand.m
//
#import "EvhUpdateLaunchPadItemCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateLaunchPadItemCommand
//

@implementation EvhUpdateLaunchPadItemCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateLaunchPadItemCommand* obj = [EvhUpdateLaunchPadItemCommand new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.appId)
        [jsonObject setObject: self.appId forKey: @"appId"];
    if(self.scopeType)
        [jsonObject setObject: self.scopeType forKey: @"scopeType"];
    if(self.scopeId)
        [jsonObject setObject: self.scopeId forKey: @"scopeId"];
    if(self.defaultOrder)
        [jsonObject setObject: self.defaultOrder forKey: @"defaultOrder"];
    if(self.applyPolicy)
        [jsonObject setObject: self.applyPolicy forKey: @"applyPolicy"];
    if(self.itemName)
        [jsonObject setObject: self.itemName forKey: @"itemName"];
    if(self.itemName)
        [jsonObject setObject: self.itemName forKey: @"itemName"];
    if(self.itemGroup)
        [jsonObject setObject: self.itemGroup forKey: @"itemGroup"];
    if(self.itemLocation)
        [jsonObject setObject: self.itemLocation forKey: @"itemLocation"];
    if(self.itemWidth)
        [jsonObject setObject: self.itemWidth forKey: @"itemWidth"];
    if(self.itemHeight)
        [jsonObject setObject: self.itemHeight forKey: @"itemHeight"];
    if(self.iconUri)
        [jsonObject setObject: self.iconUri forKey: @"iconUri"];
    if(self.actionType)
        [jsonObject setObject: self.actionType forKey: @"actionType"];
    if(self.actionData)
        [jsonObject setObject: self.actionData forKey: @"actionData"];
    if(self.displayFlag)
        [jsonObject setObject: self.displayFlag forKey: @"displayFlag"];
    if(self.displayLayout)
        [jsonObject setObject: self.displayLayout forKey: @"displayLayout"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.appId = [jsonObject objectForKey: @"appId"];
        if(self.appId && [self.appId isEqual:[NSNull null]])
            self.appId = nil;

        self.scopeType = [jsonObject objectForKey: @"scopeType"];
        if(self.scopeType && [self.scopeType isEqual:[NSNull null]])
            self.scopeType = nil;

        self.scopeId = [jsonObject objectForKey: @"scopeId"];
        if(self.scopeId && [self.scopeId isEqual:[NSNull null]])
            self.scopeId = nil;

        self.defaultOrder = [jsonObject objectForKey: @"defaultOrder"];
        if(self.defaultOrder && [self.defaultOrder isEqual:[NSNull null]])
            self.defaultOrder = nil;

        self.applyPolicy = [jsonObject objectForKey: @"applyPolicy"];
        if(self.applyPolicy && [self.applyPolicy isEqual:[NSNull null]])
            self.applyPolicy = nil;

        self.itemName = [jsonObject objectForKey: @"itemName"];
        if(self.itemName && [self.itemName isEqual:[NSNull null]])
            self.itemName = nil;

        self.itemName = [jsonObject objectForKey: @"itemName"];
        if(self.itemName && [self.itemName isEqual:[NSNull null]])
            self.itemName = nil;

        self.itemGroup = [jsonObject objectForKey: @"itemGroup"];
        if(self.itemGroup && [self.itemGroup isEqual:[NSNull null]])
            self.itemGroup = nil;

        self.itemLocation = [jsonObject objectForKey: @"itemLocation"];
        if(self.itemLocation && [self.itemLocation isEqual:[NSNull null]])
            self.itemLocation = nil;

        self.itemWidth = [jsonObject objectForKey: @"itemWidth"];
        if(self.itemWidth && [self.itemWidth isEqual:[NSNull null]])
            self.itemWidth = nil;

        self.itemHeight = [jsonObject objectForKey: @"itemHeight"];
        if(self.itemHeight && [self.itemHeight isEqual:[NSNull null]])
            self.itemHeight = nil;

        self.iconUri = [jsonObject objectForKey: @"iconUri"];
        if(self.iconUri && [self.iconUri isEqual:[NSNull null]])
            self.iconUri = nil;

        self.actionType = [jsonObject objectForKey: @"actionType"];
        if(self.actionType && [self.actionType isEqual:[NSNull null]])
            self.actionType = nil;

        self.actionData = [jsonObject objectForKey: @"actionData"];
        if(self.actionData && [self.actionData isEqual:[NSNull null]])
            self.actionData = nil;

        self.displayFlag = [jsonObject objectForKey: @"displayFlag"];
        if(self.displayFlag && [self.displayFlag isEqual:[NSNull null]])
            self.displayFlag = nil;

        self.displayLayout = [jsonObject objectForKey: @"displayLayout"];
        if(self.displayLayout && [self.displayLayout isEqual:[NSNull null]])
            self.displayLayout = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
