//
// EvhBusinessAdminDTO.m
//
#import "EvhBusinessAdminDTO.h"
#import "EvhCategoryDTO.h"
#import "EvhBusinessAssignedScopeDTO.h"
#import "EvhBusinessPromoteScopeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBusinessAdminDTO
//

@implementation EvhBusinessAdminDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBusinessAdminDTO* obj = [EvhBusinessAdminDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _categories = [NSMutableArray new];
        _assignedScopes = [NSMutableArray new];
        _promoteScopes = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.targetType)
        [jsonObject setObject: self.targetType forKey: @"targetType"];
    if(self.targetId)
        [jsonObject setObject: self.targetId forKey: @"targetId"];
    if(self.bizOwnerUid)
        [jsonObject setObject: self.bizOwnerUid forKey: @"bizOwnerUid"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.displayName)
        [jsonObject setObject: self.displayName forKey: @"displayName"];
    if(self.logoUri)
        [jsonObject setObject: self.logoUri forKey: @"logoUri"];
    if(self.logoUrl)
        [jsonObject setObject: self.logoUrl forKey: @"logoUrl"];
    if(self.url)
        [jsonObject setObject: self.url forKey: @"url"];
    if(self.contact)
        [jsonObject setObject: self.contact forKey: @"contact"];
    if(self.phone)
        [jsonObject setObject: self.phone forKey: @"phone"];
    if(self.longitude)
        [jsonObject setObject: self.longitude forKey: @"longitude"];
    if(self.latitude)
        [jsonObject setObject: self.latitude forKey: @"latitude"];
    if(self.geohash)
        [jsonObject setObject: self.geohash forKey: @"geohash"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.categories) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhCategoryDTO* item in self.categories) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"categories"];
    }
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.deleteTime)
        [jsonObject setObject: self.deleteTime forKey: @"deleteTime"];
    if(self.recommendStatus)
        [jsonObject setObject: self.recommendStatus forKey: @"recommendStatus"];
    if(self.favoriteStatus)
        [jsonObject setObject: self.favoriteStatus forKey: @"favoriteStatus"];
    if(self.distance)
        [jsonObject setObject: self.distance forKey: @"distance"];
    if(self.assignedScopes) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhBusinessAssignedScopeDTO* item in self.assignedScopes) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"assignedScopes"];
    }
    if(self.scaleType)
        [jsonObject setObject: self.scaleType forKey: @"scaleType"];
    if(self.promoteFlag)
        [jsonObject setObject: self.promoteFlag forKey: @"promoteFlag"];
    if(self.promoteScopes) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhBusinessPromoteScopeDTO* item in self.promoteScopes) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"promoteScopes"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.targetType = [jsonObject objectForKey: @"targetType"];
        if(self.targetType && [self.targetType isEqual:[NSNull null]])
            self.targetType = nil;

        self.targetId = [jsonObject objectForKey: @"targetId"];
        if(self.targetId && [self.targetId isEqual:[NSNull null]])
            self.targetId = nil;

        self.bizOwnerUid = [jsonObject objectForKey: @"bizOwnerUid"];
        if(self.bizOwnerUid && [self.bizOwnerUid isEqual:[NSNull null]])
            self.bizOwnerUid = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.displayName = [jsonObject objectForKey: @"displayName"];
        if(self.displayName && [self.displayName isEqual:[NSNull null]])
            self.displayName = nil;

        self.logoUri = [jsonObject objectForKey: @"logoUri"];
        if(self.logoUri && [self.logoUri isEqual:[NSNull null]])
            self.logoUri = nil;

        self.logoUrl = [jsonObject objectForKey: @"logoUrl"];
        if(self.logoUrl && [self.logoUrl isEqual:[NSNull null]])
            self.logoUrl = nil;

        self.url = [jsonObject objectForKey: @"url"];
        if(self.url && [self.url isEqual:[NSNull null]])
            self.url = nil;

        self.contact = [jsonObject objectForKey: @"contact"];
        if(self.contact && [self.contact isEqual:[NSNull null]])
            self.contact = nil;

        self.phone = [jsonObject objectForKey: @"phone"];
        if(self.phone && [self.phone isEqual:[NSNull null]])
            self.phone = nil;

        self.longitude = [jsonObject objectForKey: @"longitude"];
        if(self.longitude && [self.longitude isEqual:[NSNull null]])
            self.longitude = nil;

        self.latitude = [jsonObject objectForKey: @"latitude"];
        if(self.latitude && [self.latitude isEqual:[NSNull null]])
            self.latitude = nil;

        self.geohash = [jsonObject objectForKey: @"geohash"];
        if(self.geohash && [self.geohash isEqual:[NSNull null]])
            self.geohash = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"categories"];
            for(id itemJson in jsonArray) {
                EvhCategoryDTO* item = [EvhCategoryDTO new];
                
                [item fromJson: itemJson];
                [self.categories addObject: item];
            }
        }
        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        self.deleteTime = [jsonObject objectForKey: @"deleteTime"];
        if(self.deleteTime && [self.deleteTime isEqual:[NSNull null]])
            self.deleteTime = nil;

        self.recommendStatus = [jsonObject objectForKey: @"recommendStatus"];
        if(self.recommendStatus && [self.recommendStatus isEqual:[NSNull null]])
            self.recommendStatus = nil;

        self.favoriteStatus = [jsonObject objectForKey: @"favoriteStatus"];
        if(self.favoriteStatus && [self.favoriteStatus isEqual:[NSNull null]])
            self.favoriteStatus = nil;

        self.distance = [jsonObject objectForKey: @"distance"];
        if(self.distance && [self.distance isEqual:[NSNull null]])
            self.distance = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"assignedScopes"];
            for(id itemJson in jsonArray) {
                EvhBusinessAssignedScopeDTO* item = [EvhBusinessAssignedScopeDTO new];
                
                [item fromJson: itemJson];
                [self.assignedScopes addObject: item];
            }
        }
        self.scaleType = [jsonObject objectForKey: @"scaleType"];
        if(self.scaleType && [self.scaleType isEqual:[NSNull null]])
            self.scaleType = nil;

        self.promoteFlag = [jsonObject objectForKey: @"promoteFlag"];
        if(self.promoteFlag && [self.promoteFlag isEqual:[NSNull null]])
            self.promoteFlag = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"promoteScopes"];
            for(id itemJson in jsonArray) {
                EvhBusinessPromoteScopeDTO* item = [EvhBusinessPromoteScopeDTO new];
                
                [item fromJson: itemJson];
                [self.promoteScopes addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
