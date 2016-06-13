//
// EvhUserInfo.m
//
#import "EvhUserInfo.h"
#import "EvhUserCurrentEntity.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserInfo
//

@implementation EvhUserInfo

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUserInfo* obj = [EvhUserInfo new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _phones = [NSMutableArray new];
        _emails = [NSMutableArray new];
        _entityList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.accountName)
        [jsonObject setObject: self.accountName forKey: @"accountName"];
    if(self.nickName)
        [jsonObject setObject: self.nickName forKey: @"nickName"];
    if(self.avatarUrl)
        [jsonObject setObject: self.avatarUrl forKey: @"avatarUrl"];
    if(self.statusLine)
        [jsonObject setObject: self.statusLine forKey: @"statusLine"];
    if(self.gender)
        [jsonObject setObject: self.gender forKey: @"gender"];
    if(self.birthday)
        [jsonObject setObject: self.birthday forKey: @"birthday"];
    if(self.homeTown)
        [jsonObject setObject: self.homeTown forKey: @"homeTown"];
    if(self.hometownName)
        [jsonObject setObject: self.hometownName forKey: @"hometownName"];
    if(self.company)
        [jsonObject setObject: self.company forKey: @"company"];
    if(self.school)
        [jsonObject setObject: self.school forKey: @"school"];
    if(self.occupation)
        [jsonObject setObject: self.occupation forKey: @"occupation"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.communityName)
        [jsonObject setObject: self.communityName forKey: @"communityName"];
    if(self.addressId)
        [jsonObject setObject: self.addressId forKey: @"addressId"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.regionId)
        [jsonObject setObject: self.regionId forKey: @"regionId"];
    if(self.regionName)
        [jsonObject setObject: self.regionName forKey: @"regionName"];
    if(self.regionPath)
        [jsonObject setObject: self.regionPath forKey: @"regionPath"];
    if(self.avatarUri)
        [jsonObject setObject: self.avatarUri forKey: @"avatarUri"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.namespaceUserToken)
        [jsonObject setObject: self.namespaceUserToken forKey: @"namespaceUserToken"];
    if(self.uuid)
        [jsonObject setObject: self.uuid forKey: @"uuid"];
    if(self.phones) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.phones) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"phones"];
    }
    if(self.emails) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.emails) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"emails"];
    }
    if(self.communityType)
        [jsonObject setObject: self.communityType forKey: @"communityType"];
    if(self.entityList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhUserCurrentEntity* item in self.entityList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"entityList"];
    }
    if(self.sceneToken)
        [jsonObject setObject: self.sceneToken forKey: @"sceneToken"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.accountName = [jsonObject objectForKey: @"accountName"];
        if(self.accountName && [self.accountName isEqual:[NSNull null]])
            self.accountName = nil;

        self.nickName = [jsonObject objectForKey: @"nickName"];
        if(self.nickName && [self.nickName isEqual:[NSNull null]])
            self.nickName = nil;

        self.avatarUrl = [jsonObject objectForKey: @"avatarUrl"];
        if(self.avatarUrl && [self.avatarUrl isEqual:[NSNull null]])
            self.avatarUrl = nil;

        self.statusLine = [jsonObject objectForKey: @"statusLine"];
        if(self.statusLine && [self.statusLine isEqual:[NSNull null]])
            self.statusLine = nil;

        self.gender = [jsonObject objectForKey: @"gender"];
        if(self.gender && [self.gender isEqual:[NSNull null]])
            self.gender = nil;

        self.birthday = [jsonObject objectForKey: @"birthday"];
        if(self.birthday && [self.birthday isEqual:[NSNull null]])
            self.birthday = nil;

        self.homeTown = [jsonObject objectForKey: @"homeTown"];
        if(self.homeTown && [self.homeTown isEqual:[NSNull null]])
            self.homeTown = nil;

        self.hometownName = [jsonObject objectForKey: @"hometownName"];
        if(self.hometownName && [self.hometownName isEqual:[NSNull null]])
            self.hometownName = nil;

        self.company = [jsonObject objectForKey: @"company"];
        if(self.company && [self.company isEqual:[NSNull null]])
            self.company = nil;

        self.school = [jsonObject objectForKey: @"school"];
        if(self.school && [self.school isEqual:[NSNull null]])
            self.school = nil;

        self.occupation = [jsonObject objectForKey: @"occupation"];
        if(self.occupation && [self.occupation isEqual:[NSNull null]])
            self.occupation = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.communityName = [jsonObject objectForKey: @"communityName"];
        if(self.communityName && [self.communityName isEqual:[NSNull null]])
            self.communityName = nil;

        self.addressId = [jsonObject objectForKey: @"addressId"];
        if(self.addressId && [self.addressId isEqual:[NSNull null]])
            self.addressId = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.regionId = [jsonObject objectForKey: @"regionId"];
        if(self.regionId && [self.regionId isEqual:[NSNull null]])
            self.regionId = nil;

        self.regionName = [jsonObject objectForKey: @"regionName"];
        if(self.regionName && [self.regionName isEqual:[NSNull null]])
            self.regionName = nil;

        self.regionPath = [jsonObject objectForKey: @"regionPath"];
        if(self.regionPath && [self.regionPath isEqual:[NSNull null]])
            self.regionPath = nil;

        self.avatarUri = [jsonObject objectForKey: @"avatarUri"];
        if(self.avatarUri && [self.avatarUri isEqual:[NSNull null]])
            self.avatarUri = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.namespaceUserToken = [jsonObject objectForKey: @"namespaceUserToken"];
        if(self.namespaceUserToken && [self.namespaceUserToken isEqual:[NSNull null]])
            self.namespaceUserToken = nil;

        self.uuid = [jsonObject objectForKey: @"uuid"];
        if(self.uuid && [self.uuid isEqual:[NSNull null]])
            self.uuid = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"phones"];
            for(id itemJson in jsonArray) {
                [self.phones addObject: itemJson];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"emails"];
            for(id itemJson in jsonArray) {
                [self.emails addObject: itemJson];
            }
        }
        self.communityType = [jsonObject objectForKey: @"communityType"];
        if(self.communityType && [self.communityType isEqual:[NSNull null]])
            self.communityType = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"entityList"];
            for(id itemJson in jsonArray) {
                EvhUserCurrentEntity* item = [EvhUserCurrentEntity new];
                
                [item fromJson: itemJson];
                [self.entityList addObject: item];
            }
        }
        self.sceneToken = [jsonObject objectForKey: @"sceneToken"];
        if(self.sceneToken && [self.sceneToken isEqual:[NSNull null]])
            self.sceneToken = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
