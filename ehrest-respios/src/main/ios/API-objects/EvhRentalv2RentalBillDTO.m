//
// EvhRentalv2RentalBillDTO.m
//
#import "EvhRentalv2RentalBillDTO.h"
#import "EvhRentalv2SiteItemDTO.h"
#import "EvhRentalSiteRulesDTO.h"
#import "EvhRentalv2BillAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2RentalBillDTO
//

@implementation EvhRentalv2RentalBillDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalv2RentalBillDTO* obj = [EvhRentalv2RentalBillDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _siteItems = [NSMutableArray new];
        _rentalSiteRules = [NSMutableArray new];
        _billAttachments = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.rentalBillId)
        [jsonObject setObject: self.rentalBillId forKey: @"rentalBillId"];
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
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.userPhone)
        [jsonObject setObject: self.userPhone forKey: @"userPhone"];
    if(self.introduction)
        [jsonObject setObject: self.introduction forKey: @"introduction"];
    if(self.notice)
        [jsonObject setObject: self.notice forKey: @"notice"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
    if(self.reserveTime)
        [jsonObject setObject: self.reserveTime forKey: @"reserveTime"];
    if(self.payStartTime)
        [jsonObject setObject: self.payStartTime forKey: @"payStartTime"];
    if(self.payTime)
        [jsonObject setObject: self.payTime forKey: @"payTime"];
    if(self.cancelTime)
        [jsonObject setObject: self.cancelTime forKey: @"cancelTime"];
    if(self.payDeadLineTime)
        [jsonObject setObject: self.payDeadLineTime forKey: @"payDeadLineTime"];
    if(self.sitePrice)
        [jsonObject setObject: self.sitePrice forKey: @"sitePrice"];
    if(self.totalPrice)
        [jsonObject setObject: self.totalPrice forKey: @"totalPrice"];
    if(self.reservePrice)
        [jsonObject setObject: self.reservePrice forKey: @"reservePrice"];
    if(self.paidPrice)
        [jsonObject setObject: self.paidPrice forKey: @"paidPrice"];
    if(self.unPayPrice)
        [jsonObject setObject: self.unPayPrice forKey: @"unPayPrice"];
    if(self.invoiceFlag)
        [jsonObject setObject: self.invoiceFlag forKey: @"invoiceFlag"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.rentalCount)
        [jsonObject setObject: self.rentalCount forKey: @"rentalCount"];
    if(self.useDetail)
        [jsonObject setObject: self.useDetail forKey: @"useDetail"];
    if(self.vendorType)
        [jsonObject setObject: self.vendorType forKey: @"vendorType"];
    if(self.resourceTypeId)
        [jsonObject setObject: self.resourceTypeId forKey: @"resourceTypeId"];
    if(self.siteItems) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalv2SiteItemDTO* item in self.siteItems) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"siteItems"];
    }
    if(self.rentalSiteRules) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalSiteRulesDTO* item in self.rentalSiteRules) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"rentalSiteRules"];
    }
    if(self.billAttachments) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalv2BillAttachmentDTO* item in self.billAttachments) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"billAttachments"];
    }
    if(self.toastFlag)
        [jsonObject setObject: self.toastFlag forKey: @"toastFlag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.rentalBillId = [jsonObject objectForKey: @"rentalBillId"];
        if(self.rentalBillId && [self.rentalBillId isEqual:[NSNull null]])
            self.rentalBillId = nil;

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

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.userPhone = [jsonObject objectForKey: @"userPhone"];
        if(self.userPhone && [self.userPhone isEqual:[NSNull null]])
            self.userPhone = nil;

        self.introduction = [jsonObject objectForKey: @"introduction"];
        if(self.introduction && [self.introduction isEqual:[NSNull null]])
            self.introduction = nil;

        self.notice = [jsonObject objectForKey: @"notice"];
        if(self.notice && [self.notice isEqual:[NSNull null]])
            self.notice = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        self.reserveTime = [jsonObject objectForKey: @"reserveTime"];
        if(self.reserveTime && [self.reserveTime isEqual:[NSNull null]])
            self.reserveTime = nil;

        self.payStartTime = [jsonObject objectForKey: @"payStartTime"];
        if(self.payStartTime && [self.payStartTime isEqual:[NSNull null]])
            self.payStartTime = nil;

        self.payTime = [jsonObject objectForKey: @"payTime"];
        if(self.payTime && [self.payTime isEqual:[NSNull null]])
            self.payTime = nil;

        self.cancelTime = [jsonObject objectForKey: @"cancelTime"];
        if(self.cancelTime && [self.cancelTime isEqual:[NSNull null]])
            self.cancelTime = nil;

        self.payDeadLineTime = [jsonObject objectForKey: @"payDeadLineTime"];
        if(self.payDeadLineTime && [self.payDeadLineTime isEqual:[NSNull null]])
            self.payDeadLineTime = nil;

        self.sitePrice = [jsonObject objectForKey: @"sitePrice"];
        if(self.sitePrice && [self.sitePrice isEqual:[NSNull null]])
            self.sitePrice = nil;

        self.totalPrice = [jsonObject objectForKey: @"totalPrice"];
        if(self.totalPrice && [self.totalPrice isEqual:[NSNull null]])
            self.totalPrice = nil;

        self.reservePrice = [jsonObject objectForKey: @"reservePrice"];
        if(self.reservePrice && [self.reservePrice isEqual:[NSNull null]])
            self.reservePrice = nil;

        self.paidPrice = [jsonObject objectForKey: @"paidPrice"];
        if(self.paidPrice && [self.paidPrice isEqual:[NSNull null]])
            self.paidPrice = nil;

        self.unPayPrice = [jsonObject objectForKey: @"unPayPrice"];
        if(self.unPayPrice && [self.unPayPrice isEqual:[NSNull null]])
            self.unPayPrice = nil;

        self.invoiceFlag = [jsonObject objectForKey: @"invoiceFlag"];
        if(self.invoiceFlag && [self.invoiceFlag isEqual:[NSNull null]])
            self.invoiceFlag = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.rentalCount = [jsonObject objectForKey: @"rentalCount"];
        if(self.rentalCount && [self.rentalCount isEqual:[NSNull null]])
            self.rentalCount = nil;

        self.useDetail = [jsonObject objectForKey: @"useDetail"];
        if(self.useDetail && [self.useDetail isEqual:[NSNull null]])
            self.useDetail = nil;

        self.vendorType = [jsonObject objectForKey: @"vendorType"];
        if(self.vendorType && [self.vendorType isEqual:[NSNull null]])
            self.vendorType = nil;

        self.resourceTypeId = [jsonObject objectForKey: @"resourceTypeId"];
        if(self.resourceTypeId && [self.resourceTypeId isEqual:[NSNull null]])
            self.resourceTypeId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"siteItems"];
            for(id itemJson in jsonArray) {
                EvhRentalv2SiteItemDTO* item = [EvhRentalv2SiteItemDTO new];
                
                [item fromJson: itemJson];
                [self.siteItems addObject: item];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"rentalSiteRules"];
            for(id itemJson in jsonArray) {
                EvhRentalSiteRulesDTO* item = [EvhRentalSiteRulesDTO new];
                
                [item fromJson: itemJson];
                [self.rentalSiteRules addObject: item];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"billAttachments"];
            for(id itemJson in jsonArray) {
                EvhRentalv2BillAttachmentDTO* item = [EvhRentalv2BillAttachmentDTO new];
                
                [item fromJson: itemJson];
                [self.billAttachments addObject: item];
            }
        }
        self.toastFlag = [jsonObject objectForKey: @"toastFlag"];
        if(self.toastFlag && [self.toastFlag isEqual:[NSNull null]])
            self.toastFlag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
