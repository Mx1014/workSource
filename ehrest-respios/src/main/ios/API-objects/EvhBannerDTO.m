//
// EvhBannerDTO.m
//
#import "EvhBannerDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBannerDTO
//

@implementation EvhBannerDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBannerDTO* obj = [EvhBannerDTO new];
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
    if(self.appid)
        [jsonObject setObject: self.appid forKey: @"appid"];
    if(self.scopeType)
        [jsonObject setObject: self.scopeType forKey: @"scopeType"];
    if(self.scopeId)
        [jsonObject setObject: self.scopeId forKey: @"scopeId"];
    if(self.bannerLocation)
        [jsonObject setObject: self.bannerLocation forKey: @"bannerLocation"];
    if(self.bannerGroup)
        [jsonObject setObject: self.bannerGroup forKey: @"bannerGroup"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.vendorTag)
        [jsonObject setObject: self.vendorTag forKey: @"vendorTag"];
    if(self.posterPath)
        [jsonObject setObject: self.posterPath forKey: @"posterPath"];
    if(self.actionType)
        [jsonObject setObject: self.actionType forKey: @"actionType"];
    if(self.actionData)
        [jsonObject setObject: self.actionData forKey: @"actionData"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.order)
        [jsonObject setObject: self.order forKey: @"order"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.deleteTime)
        [jsonObject setObject: self.deleteTime forKey: @"deleteTime"];
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

        self.appid = [jsonObject objectForKey: @"appid"];
        if(self.appid && [self.appid isEqual:[NSNull null]])
            self.appid = nil;

        self.scopeType = [jsonObject objectForKey: @"scopeType"];
        if(self.scopeType && [self.scopeType isEqual:[NSNull null]])
            self.scopeType = nil;

        self.scopeId = [jsonObject objectForKey: @"scopeId"];
        if(self.scopeId && [self.scopeId isEqual:[NSNull null]])
            self.scopeId = nil;

        self.bannerLocation = [jsonObject objectForKey: @"bannerLocation"];
        if(self.bannerLocation && [self.bannerLocation isEqual:[NSNull null]])
            self.bannerLocation = nil;

        self.bannerGroup = [jsonObject objectForKey: @"bannerGroup"];
        if(self.bannerGroup && [self.bannerGroup isEqual:[NSNull null]])
            self.bannerGroup = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.vendorTag = [jsonObject objectForKey: @"vendorTag"];
        if(self.vendorTag && [self.vendorTag isEqual:[NSNull null]])
            self.vendorTag = nil;

        self.posterPath = [jsonObject objectForKey: @"posterPath"];
        if(self.posterPath && [self.posterPath isEqual:[NSNull null]])
            self.posterPath = nil;

        self.actionType = [jsonObject objectForKey: @"actionType"];
        if(self.actionType && [self.actionType isEqual:[NSNull null]])
            self.actionType = nil;

        self.actionData = [jsonObject objectForKey: @"actionData"];
        if(self.actionData && [self.actionData isEqual:[NSNull null]])
            self.actionData = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.order = [jsonObject objectForKey: @"order"];
        if(self.order && [self.order isEqual:[NSNull null]])
            self.order = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.deleteTime = [jsonObject objectForKey: @"deleteTime"];
        if(self.deleteTime && [self.deleteTime isEqual:[NSNull null]])
            self.deleteTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
