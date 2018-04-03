//
// EvhCreateLaunchPadItemAdminCommand.m
//
#import "EvhCreateLaunchPadItemAdminCommand.h"
#import "EvhItemScope.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateLaunchPadItemAdminCommand
//

@implementation EvhCreateLaunchPadItemAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateLaunchPadItemAdminCommand* obj = [EvhCreateLaunchPadItemAdminCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _itemScopes = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.appId)
        [jsonObject setObject: self.appId forKey: @"appId"];
    if(self.itemScopes) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhItemScope* item in self.itemScopes) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"itemScopes"];
    }
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
    if(self.bgcolor)
        [jsonObject setObject: self.bgcolor forKey: @"bgcolor"];
    if(self.tag)
        [jsonObject setObject: self.tag forKey: @"tag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.appId = [jsonObject objectForKey: @"appId"];
        if(self.appId && [self.appId isEqual:[NSNull null]])
            self.appId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"itemScopes"];
            for(id itemJson in jsonArray) {
                EvhItemScope* item = [EvhItemScope new];
                
                [item fromJson: itemJson];
                [self.itemScopes addObject: item];
            }
        }
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

        self.bgcolor = [jsonObject objectForKey: @"bgcolor"];
        if(self.bgcolor && [self.bgcolor isEqual:[NSNull null]])
            self.bgcolor = nil;

        self.tag = [jsonObject objectForKey: @"tag"];
        if(self.tag && [self.tag isEqual:[NSNull null]])
            self.tag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
