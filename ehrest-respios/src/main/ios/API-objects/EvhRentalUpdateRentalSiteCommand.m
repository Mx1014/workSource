//
// EvhRentalUpdateRentalSiteCommand.m
//
#import "EvhRentalUpdateRentalSiteCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalUpdateRentalSiteCommand
//

@implementation EvhRentalUpdateRentalSiteCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalUpdateRentalSiteCommand* obj = [EvhRentalUpdateRentalSiteCommand new];
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
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.siteType)
        [jsonObject setObject: self.siteType forKey: @"siteType"];
    if(self.rentalSiteId)
        [jsonObject setObject: self.rentalSiteId forKey: @"rentalSiteId"];
    if(self.siteName)
        [jsonObject setObject: self.siteName forKey: @"siteName"];
    if(self.buildingName)
        [jsonObject setObject: self.buildingName forKey: @"buildingName"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.spec)
        [jsonObject setObject: self.spec forKey: @"spec"];
    if(self.company)
        [jsonObject setObject: self.company forKey: @"company"];
    if(self.contactName)
        [jsonObject setObject: self.contactName forKey: @"contactName"];
    if(self.contactPhonenum)
        [jsonObject setObject: self.contactPhonenum forKey: @"contactPhonenum"];
    if(self.introduction)
        [jsonObject setObject: self.introduction forKey: @"introduction"];
    if(self.notice)
        [jsonObject setObject: self.notice forKey: @"notice"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.siteType = [jsonObject objectForKey: @"siteType"];
        if(self.siteType && [self.siteType isEqual:[NSNull null]])
            self.siteType = nil;

        self.rentalSiteId = [jsonObject objectForKey: @"rentalSiteId"];
        if(self.rentalSiteId && [self.rentalSiteId isEqual:[NSNull null]])
            self.rentalSiteId = nil;

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

        self.company = [jsonObject objectForKey: @"company"];
        if(self.company && [self.company isEqual:[NSNull null]])
            self.company = nil;

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

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
