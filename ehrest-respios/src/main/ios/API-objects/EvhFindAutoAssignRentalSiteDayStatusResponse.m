//
// EvhFindAutoAssignRentalSiteDayStatusResponse.m
//
#import "EvhFindAutoAssignRentalSiteDayStatusResponse.h"
#import "EvhRentalSiteNumberDayRulesDTO.h"
#import "EvhRentalSitePicDTO.h"
#import "EvhAttachmentDTO.h"
#import "EvhSiteItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindAutoAssignRentalSiteDayStatusResponse
//

@implementation EvhFindAutoAssignRentalSiteDayStatusResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFindAutoAssignRentalSiteDayStatusResponse* obj = [EvhFindAutoAssignRentalSiteDayStatusResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _siteNumbers = [NSMutableArray new];
        _sitePics = [NSMutableArray new];
        _attachments = [NSMutableArray new];
        _siteItems = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.rentalSiteId)
        [jsonObject setObject: self.rentalSiteId forKey: @"rentalSiteId"];
    if(self.siteName)
        [jsonObject setObject: self.siteName forKey: @"siteName"];
    if(self.introduction)
        [jsonObject setObject: self.introduction forKey: @"introduction"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.longitude)
        [jsonObject setObject: self.longitude forKey: @"longitude"];
    if(self.latitude)
        [jsonObject setObject: self.latitude forKey: @"latitude"];
    if(self.contactPhonenum)
        [jsonObject setObject: self.contactPhonenum forKey: @"contactPhonenum"];
    if(self.discountType)
        [jsonObject setObject: self.discountType forKey: @"discountType"];
    if(self.fullPrice)
        [jsonObject setObject: self.fullPrice forKey: @"fullPrice"];
    if(self.cutPrice)
        [jsonObject setObject: self.cutPrice forKey: @"cutPrice"];
    if(self.discountRatio)
        [jsonObject setObject: self.discountRatio forKey: @"discountRatio"];
    if(self.rentalType)
        [jsonObject setObject: self.rentalType forKey: @"rentalType"];
    if(self.rentalStep)
        [jsonObject setObject: self.rentalStep forKey: @"rentalStep"];
    if(self.exclusiveFlag)
        [jsonObject setObject: self.exclusiveFlag forKey: @"exclusiveFlag"];
    if(self.autoAssign)
        [jsonObject setObject: self.autoAssign forKey: @"autoAssign"];
    if(self.multiUnit)
        [jsonObject setObject: self.multiUnit forKey: @"multiUnit"];
    if(self.multiTimeInterval)
        [jsonObject setObject: self.multiTimeInterval forKey: @"multiTimeInterval"];
    if(self.cancelFlag)
        [jsonObject setObject: self.cancelFlag forKey: @"cancelFlag"];
    if(self.needPay)
        [jsonObject setObject: self.needPay forKey: @"needPay"];
    if(self.anchorTime)
        [jsonObject setObject: self.anchorTime forKey: @"anchorTime"];
    if(self.siteNumbers) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalSiteNumberDayRulesDTO* item in self.siteNumbers) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"siteNumbers"];
    }
    if(self.sitePics) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalSitePicDTO* item in self.sitePics) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"sitePics"];
    }
    if(self.attachments) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhAttachmentDTO* item in self.attachments) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"attachments"];
    }
    if(self.siteItems) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhSiteItemDTO* item in self.siteItems) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"siteItems"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.rentalSiteId = [jsonObject objectForKey: @"rentalSiteId"];
        if(self.rentalSiteId && [self.rentalSiteId isEqual:[NSNull null]])
            self.rentalSiteId = nil;

        self.siteName = [jsonObject objectForKey: @"siteName"];
        if(self.siteName && [self.siteName isEqual:[NSNull null]])
            self.siteName = nil;

        self.introduction = [jsonObject objectForKey: @"introduction"];
        if(self.introduction && [self.introduction isEqual:[NSNull null]])
            self.introduction = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.longitude = [jsonObject objectForKey: @"longitude"];
        if(self.longitude && [self.longitude isEqual:[NSNull null]])
            self.longitude = nil;

        self.latitude = [jsonObject objectForKey: @"latitude"];
        if(self.latitude && [self.latitude isEqual:[NSNull null]])
            self.latitude = nil;

        self.contactPhonenum = [jsonObject objectForKey: @"contactPhonenum"];
        if(self.contactPhonenum && [self.contactPhonenum isEqual:[NSNull null]])
            self.contactPhonenum = nil;

        self.discountType = [jsonObject objectForKey: @"discountType"];
        if(self.discountType && [self.discountType isEqual:[NSNull null]])
            self.discountType = nil;

        self.fullPrice = [jsonObject objectForKey: @"fullPrice"];
        if(self.fullPrice && [self.fullPrice isEqual:[NSNull null]])
            self.fullPrice = nil;

        self.cutPrice = [jsonObject objectForKey: @"cutPrice"];
        if(self.cutPrice && [self.cutPrice isEqual:[NSNull null]])
            self.cutPrice = nil;

        self.discountRatio = [jsonObject objectForKey: @"discountRatio"];
        if(self.discountRatio && [self.discountRatio isEqual:[NSNull null]])
            self.discountRatio = nil;

        self.rentalType = [jsonObject objectForKey: @"rentalType"];
        if(self.rentalType && [self.rentalType isEqual:[NSNull null]])
            self.rentalType = nil;

        self.rentalStep = [jsonObject objectForKey: @"rentalStep"];
        if(self.rentalStep && [self.rentalStep isEqual:[NSNull null]])
            self.rentalStep = nil;

        self.exclusiveFlag = [jsonObject objectForKey: @"exclusiveFlag"];
        if(self.exclusiveFlag && [self.exclusiveFlag isEqual:[NSNull null]])
            self.exclusiveFlag = nil;

        self.autoAssign = [jsonObject objectForKey: @"autoAssign"];
        if(self.autoAssign && [self.autoAssign isEqual:[NSNull null]])
            self.autoAssign = nil;

        self.multiUnit = [jsonObject objectForKey: @"multiUnit"];
        if(self.multiUnit && [self.multiUnit isEqual:[NSNull null]])
            self.multiUnit = nil;

        self.multiTimeInterval = [jsonObject objectForKey: @"multiTimeInterval"];
        if(self.multiTimeInterval && [self.multiTimeInterval isEqual:[NSNull null]])
            self.multiTimeInterval = nil;

        self.cancelFlag = [jsonObject objectForKey: @"cancelFlag"];
        if(self.cancelFlag && [self.cancelFlag isEqual:[NSNull null]])
            self.cancelFlag = nil;

        self.needPay = [jsonObject objectForKey: @"needPay"];
        if(self.needPay && [self.needPay isEqual:[NSNull null]])
            self.needPay = nil;

        self.anchorTime = [jsonObject objectForKey: @"anchorTime"];
        if(self.anchorTime && [self.anchorTime isEqual:[NSNull null]])
            self.anchorTime = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"siteNumbers"];
            for(id itemJson in jsonArray) {
                EvhRentalSiteNumberDayRulesDTO* item = [EvhRentalSiteNumberDayRulesDTO new];
                
                [item fromJson: itemJson];
                [self.siteNumbers addObject: item];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"sitePics"];
            for(id itemJson in jsonArray) {
                EvhRentalSitePicDTO* item = [EvhRentalSitePicDTO new];
                
                [item fromJson: itemJson];
                [self.sitePics addObject: item];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"attachments"];
            for(id itemJson in jsonArray) {
                EvhAttachmentDTO* item = [EvhAttachmentDTO new];
                
                [item fromJson: itemJson];
                [self.attachments addObject: item];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"siteItems"];
            for(id itemJson in jsonArray) {
                EvhSiteItemDTO* item = [EvhSiteItemDTO new];
                
                [item fromJson: itemJson];
                [self.siteItems addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
