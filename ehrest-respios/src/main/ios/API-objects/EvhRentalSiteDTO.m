//
// EvhRentalSiteDTO.m
//
#import "EvhRentalSiteDTO.h"
#import "EvhSiteItemDTO.h"
#import "EvhRentalRentalSiteRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalSiteDTO
//

@implementation EvhRentalSiteDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalSiteDTO* obj = [EvhRentalSiteDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _siteItems = [NSMutableArray new];
        _siteRules = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.rentalSiteId)
        [jsonObject setObject: self.rentalSiteId forKey: @"rentalSiteId"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.siteType)
        [jsonObject setObject: self.siteType forKey: @"siteType"];
    if(self.siteName)
        [jsonObject setObject: self.siteName forKey: @"siteName"];
    if(self.buildingName)
        [jsonObject setObject: self.buildingName forKey: @"buildingName"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.spec)
        [jsonObject setObject: self.spec forKey: @"spec"];
    if(self.companyName)
        [jsonObject setObject: self.companyName forKey: @"companyName"];
    if(self.contactName)
        [jsonObject setObject: self.contactName forKey: @"contactName"];
    if(self.contactPhonenum)
        [jsonObject setObject: self.contactPhonenum forKey: @"contactPhonenum"];
    if(self.introduction)
        [jsonObject setObject: self.introduction forKey: @"introduction"];
    if(self.notice)
        [jsonObject setObject: self.notice forKey: @"notice"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.siteItems) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhSiteItemDTO* item in self.siteItems) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"siteItems"];
    }
    if(self.siteRules) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalRentalSiteRulesDTO* item in self.siteRules) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"siteRules"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.rentalSiteId = [jsonObject objectForKey: @"rentalSiteId"];
        if(self.rentalSiteId && [self.rentalSiteId isEqual:[NSNull null]])
            self.rentalSiteId = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.siteType = [jsonObject objectForKey: @"siteType"];
        if(self.siteType && [self.siteType isEqual:[NSNull null]])
            self.siteType = nil;

        self.siteName = [jsonObject objectForKey: @"siteName"];
        if(self.siteName && [self.siteName isEqual:[NSNull null]])
            self.siteName = nil;

        self.buildingName = [jsonObject objectForKey: @"buildingName"];
        if(self.buildingName && [self.buildingName isEqual:[NSNull null]])
            self.buildingName = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.spec = [jsonObject objectForKey: @"spec"];
        if(self.spec && [self.spec isEqual:[NSNull null]])
            self.spec = nil;

        self.companyName = [jsonObject objectForKey: @"companyName"];
        if(self.companyName && [self.companyName isEqual:[NSNull null]])
            self.companyName = nil;

        self.contactName = [jsonObject objectForKey: @"contactName"];
        if(self.contactName && [self.contactName isEqual:[NSNull null]])
            self.contactName = nil;

        self.contactPhonenum = [jsonObject objectForKey: @"contactPhonenum"];
        if(self.contactPhonenum && [self.contactPhonenum isEqual:[NSNull null]])
            self.contactPhonenum = nil;

        self.introduction = [jsonObject objectForKey: @"introduction"];
        if(self.introduction && [self.introduction isEqual:[NSNull null]])
            self.introduction = nil;

        self.notice = [jsonObject objectForKey: @"notice"];
        if(self.notice && [self.notice isEqual:[NSNull null]])
            self.notice = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"siteItems"];
            for(id itemJson in jsonArray) {
                EvhSiteItemDTO* item = [EvhSiteItemDTO new];
                
                [item fromJson: itemJson];
                [self.siteItems addObject: item];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"siteRules"];
            for(id itemJson in jsonArray) {
                EvhRentalRentalSiteRulesDTO* item = [EvhRentalRentalSiteRulesDTO new];
                
                [item fromJson: itemJson];
                [self.siteRules addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
