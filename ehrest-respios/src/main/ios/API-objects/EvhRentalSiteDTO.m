//
// EvhRentalSiteDTO.m
//
#import "EvhRentalSiteDTO.h"
#import "EvhSiteItemDTO.h"
#import "EvhRentalSitePicDTO.h"
#import "EvhSiteOwnerDTO.h"
#import "EvhAttachmentDTO.h"

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
        _sitePics = [NSMutableArray new];
        _owners = [NSMutableArray new];
        _attachments = [NSMutableArray new];
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
    if(self.buildingName)
        [jsonObject setObject: self.buildingName forKey: @"buildingName"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.avgPrice)
        [jsonObject setObject: self.avgPrice forKey: @"avgPrice"];
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
    if(self.coverUri)
        [jsonObject setObject: self.coverUri forKey: @"coverUri"];
    if(self.coverUrl)
        [jsonObject setObject: self.coverUrl forKey: @"coverUrl"];
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
    if(self.dayBeginTime)
        [jsonObject setObject: self.dayBeginTime forKey: @"dayBeginTime"];
    if(self.dayEndTime)
        [jsonObject setObject: self.dayEndTime forKey: @"dayEndTime"];
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
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.siteItems) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhSiteItemDTO* item in self.siteItems) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"siteItems"];
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
    if(self.owners) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhSiteOwnerDTO* item in self.owners) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"owners"];
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

        self.buildingName = [jsonObject objectForKey: @"buildingName"];
        if(self.buildingName && [self.buildingName isEqual:[NSNull null]])
            self.buildingName = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.avgPrice = [jsonObject objectForKey: @"avgPrice"];
        if(self.avgPrice && [self.avgPrice isEqual:[NSNull null]])
            self.avgPrice = nil;

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

        self.coverUri = [jsonObject objectForKey: @"coverUri"];
        if(self.coverUri && [self.coverUri isEqual:[NSNull null]])
            self.coverUri = nil;

        self.coverUrl = [jsonObject objectForKey: @"coverUrl"];
        if(self.coverUrl && [self.coverUrl isEqual:[NSNull null]])
            self.coverUrl = nil;

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

        self.dayBeginTime = [jsonObject objectForKey: @"dayBeginTime"];
        if(self.dayBeginTime && [self.dayBeginTime isEqual:[NSNull null]])
            self.dayBeginTime = nil;

        self.dayEndTime = [jsonObject objectForKey: @"dayEndTime"];
        if(self.dayEndTime && [self.dayEndTime isEqual:[NSNull null]])
            self.dayEndTime = nil;

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

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"siteItems"];
            for(id itemJson in jsonArray) {
                EvhSiteItemDTO* item = [EvhSiteItemDTO new];
                
                [item fromJson: itemJson];
                [self.siteItems addObject: item];
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
            NSArray* jsonArray = [jsonObject objectForKey: @"owners"];
            for(id itemJson in jsonArray) {
                EvhSiteOwnerDTO* item = [EvhSiteOwnerDTO new];
                
                [item fromJson: itemJson];
                [self.owners addObject: item];
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
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
