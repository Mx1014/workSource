//
// EvhAddResourceAdminCommand.m
//
#import "EvhAddResourceAdminCommand.h"
#import "EvhSiteOwnerDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddResourceAdminCommand
//

@implementation EvhAddResourceAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAddResourceAdminCommand* obj = [EvhAddResourceAdminCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _detailUris = [NSMutableArray new];
        _owners = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.resourceTypeId)
        [jsonObject setObject: self.resourceTypeId forKey: @"resourceTypeId"];
    if(self.organizationId)
        [jsonObject setObject: self.organizationId forKey: @"organizationId"];
    if(self.siteName)
        [jsonObject setObject: self.siteName forKey: @"siteName"];
    if(self.spec)
        [jsonObject setObject: self.spec forKey: @"spec"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.longitude)
        [jsonObject setObject: self.longitude forKey: @"longitude"];
    if(self.latitude)
        [jsonObject setObject: self.latitude forKey: @"latitude"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.contactPhonenum)
        [jsonObject setObject: self.contactPhonenum forKey: @"contactPhonenum"];
    if(self.chargeUid)
        [jsonObject setObject: self.chargeUid forKey: @"chargeUid"];
    if(self.introduction)
        [jsonObject setObject: self.introduction forKey: @"introduction"];
    if(self.notice)
        [jsonObject setObject: self.notice forKey: @"notice"];
    if(self.coverUri)
        [jsonObject setObject: self.coverUri forKey: @"coverUri"];
    if(self.detailUris) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.detailUris) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"detailUris"];
    }
    if(self.owners) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhSiteOwnerDTO* item in self.owners) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"owners"];
    }
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.resourceTypeId = [jsonObject objectForKey: @"resourceTypeId"];
        if(self.resourceTypeId && [self.resourceTypeId isEqual:[NSNull null]])
            self.resourceTypeId = nil;

        self.organizationId = [jsonObject objectForKey: @"organizationId"];
        if(self.organizationId && [self.organizationId isEqual:[NSNull null]])
            self.organizationId = nil;

        self.siteName = [jsonObject objectForKey: @"siteName"];
        if(self.siteName && [self.siteName isEqual:[NSNull null]])
            self.siteName = nil;

        self.spec = [jsonObject objectForKey: @"spec"];
        if(self.spec && [self.spec isEqual:[NSNull null]])
            self.spec = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.longitude = [jsonObject objectForKey: @"longitude"];
        if(self.longitude && [self.longitude isEqual:[NSNull null]])
            self.longitude = nil;

        self.latitude = [jsonObject objectForKey: @"latitude"];
        if(self.latitude && [self.latitude isEqual:[NSNull null]])
            self.latitude = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.contactPhonenum = [jsonObject objectForKey: @"contactPhonenum"];
        if(self.contactPhonenum && [self.contactPhonenum isEqual:[NSNull null]])
            self.contactPhonenum = nil;

        self.chargeUid = [jsonObject objectForKey: @"chargeUid"];
        if(self.chargeUid && [self.chargeUid isEqual:[NSNull null]])
            self.chargeUid = nil;

        self.introduction = [jsonObject objectForKey: @"introduction"];
        if(self.introduction && [self.introduction isEqual:[NSNull null]])
            self.introduction = nil;

        self.notice = [jsonObject objectForKey: @"notice"];
        if(self.notice && [self.notice isEqual:[NSNull null]])
            self.notice = nil;

        self.coverUri = [jsonObject objectForKey: @"coverUri"];
        if(self.coverUri && [self.coverUri isEqual:[NSNull null]])
            self.coverUri = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"detailUris"];
            for(id itemJson in jsonArray) {
                [self.detailUris addObject: itemJson];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"owners"];
            for(id itemJson in jsonArray) {
                EvhSiteOwnerDTO* item = [EvhSiteOwnerDTO new];
                
                [item fromJson: itemJson];
                [self.owners addObject: item];
            }
        }
        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
