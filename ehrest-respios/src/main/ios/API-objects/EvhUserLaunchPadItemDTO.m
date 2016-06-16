//
// EvhUserLaunchPadItemDTO.m
//
#import "EvhUserLaunchPadItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserLaunchPadItemDTO
//

@implementation EvhUserLaunchPadItemDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUserLaunchPadItemDTO* obj = [EvhUserLaunchPadItemDTO new];
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
    if(self.itemId)
        [jsonObject setObject: self.itemId forKey: @"itemId"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.applyPolicy)
        [jsonObject setObject: self.applyPolicy forKey: @"applyPolicy"];
    if(self.displayFlag)
        [jsonObject setObject: self.displayFlag forKey: @"displayFlag"];
    if(self.defaultOrder)
        [jsonObject setObject: self.defaultOrder forKey: @"defaultOrder"];
    if(self.sceneType)
        [jsonObject setObject: self.sceneType forKey: @"sceneType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.itemId = [jsonObject objectForKey: @"itemId"];
        if(self.itemId && [self.itemId isEqual:[NSNull null]])
            self.itemId = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.applyPolicy = [jsonObject objectForKey: @"applyPolicy"];
        if(self.applyPolicy && [self.applyPolicy isEqual:[NSNull null]])
            self.applyPolicy = nil;

        self.displayFlag = [jsonObject objectForKey: @"displayFlag"];
        if(self.displayFlag && [self.displayFlag isEqual:[NSNull null]])
            self.displayFlag = nil;

        self.defaultOrder = [jsonObject objectForKey: @"defaultOrder"];
        if(self.defaultOrder && [self.defaultOrder isEqual:[NSNull null]])
            self.defaultOrder = nil;

        self.sceneType = [jsonObject objectForKey: @"sceneType"];
        if(self.sceneType && [self.sceneType isEqual:[NSNull null]])
            self.sceneType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
