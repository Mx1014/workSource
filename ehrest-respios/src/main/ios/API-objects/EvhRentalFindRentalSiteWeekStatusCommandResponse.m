//
// EvhRentalFindRentalSiteWeekStatusCommandResponse.m
//
#import "EvhRentalFindRentalSiteWeekStatusCommandResponse.h"
#import "EvhRentalSiteDayRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalFindRentalSiteWeekStatusCommandResponse
//

@implementation EvhRentalFindRentalSiteWeekStatusCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalFindRentalSiteWeekStatusCommandResponse* obj = [EvhRentalFindRentalSiteWeekStatusCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _siteDays = [NSMutableArray new];
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
    if(self.ownerName)
        [jsonObject setObject: self.ownerName forKey: @"ownerName"];
    if(self.siteType)
        [jsonObject setObject: self.siteType forKey: @"siteType"];
    if(self.contactNum)
        [jsonObject setObject: self.contactNum forKey: @"contactNum"];
    if(self.siteName)
        [jsonObject setObject: self.siteName forKey: @"siteName"];
    if(self.introduction)
        [jsonObject setObject: self.introduction forKey: @"introduction"];
    if(self.notice)
        [jsonObject setObject: self.notice forKey: @"notice"];
    if(self.anchorTime)
        [jsonObject setObject: self.anchorTime forKey: @"anchorTime"];
    if(self.siteDays) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalSiteDayRulesDTO* item in self.siteDays) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"siteDays"];
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

        self.ownerName = [jsonObject objectForKey: @"ownerName"];
        if(self.ownerName && [self.ownerName isEqual:[NSNull null]])
            self.ownerName = nil;

        self.siteType = [jsonObject objectForKey: @"siteType"];
        if(self.siteType && [self.siteType isEqual:[NSNull null]])
            self.siteType = nil;

        self.contactNum = [jsonObject objectForKey: @"contactNum"];
        if(self.contactNum && [self.contactNum isEqual:[NSNull null]])
            self.contactNum = nil;

        self.siteName = [jsonObject objectForKey: @"siteName"];
        if(self.siteName && [self.siteName isEqual:[NSNull null]])
            self.siteName = nil;

        self.introduction = [jsonObject objectForKey: @"introduction"];
        if(self.introduction && [self.introduction isEqual:[NSNull null]])
            self.introduction = nil;

        self.notice = [jsonObject objectForKey: @"notice"];
        if(self.notice && [self.notice isEqual:[NSNull null]])
            self.notice = nil;

        self.anchorTime = [jsonObject objectForKey: @"anchorTime"];
        if(self.anchorTime && [self.anchorTime isEqual:[NSNull null]])
            self.anchorTime = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"siteDays"];
            for(id itemJson in jsonArray) {
                EvhRentalSiteDayRulesDTO* item = [EvhRentalSiteDayRulesDTO new];
                
                [item fromJson: itemJson];
                [self.siteDays addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
