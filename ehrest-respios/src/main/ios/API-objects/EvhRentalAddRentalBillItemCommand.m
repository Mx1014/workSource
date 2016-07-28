//
// EvhRentalAddRentalBillItemCommand.m
//
#import "EvhRentalAddRentalBillItemCommand.h"
#import "EvhSiteItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAddRentalBillItemCommand
//

@implementation EvhRentalAddRentalBillItemCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalAddRentalBillItemCommand* obj = [EvhRentalAddRentalBillItemCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _rentalItems = [NSMutableArray new];
        _rentalAttachments = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.rentalSiteId)
        [jsonObject setObject: self.rentalSiteId forKey: @"rentalSiteId"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.invoiceFlag)
        [jsonObject setObject: self.invoiceFlag forKey: @"invoiceFlag"];
    if(self.siteType)
        [jsonObject setObject: self.siteType forKey: @"siteType"];
    if(self.rentalBillId)
        [jsonObject setObject: self.rentalBillId forKey: @"rentalBillId"];
    if(self.rentalItems) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhSiteItemDTO* item in self.rentalItems) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"rentalItems"];
    }
    if(self.rentalAttachments) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.rentalAttachments) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"rentalAttachments"];
    }
    if(self.attachmentType)
        [jsonObject setObject: self.attachmentType forKey: @"attachmentType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.rentalSiteId = [jsonObject objectForKey: @"rentalSiteId"];
        if(self.rentalSiteId && [self.rentalSiteId isEqual:[NSNull null]])
            self.rentalSiteId = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.invoiceFlag = [jsonObject objectForKey: @"invoiceFlag"];
        if(self.invoiceFlag && [self.invoiceFlag isEqual:[NSNull null]])
            self.invoiceFlag = nil;

        self.siteType = [jsonObject objectForKey: @"siteType"];
        if(self.siteType && [self.siteType isEqual:[NSNull null]])
            self.siteType = nil;

        self.rentalBillId = [jsonObject objectForKey: @"rentalBillId"];
        if(self.rentalBillId && [self.rentalBillId isEqual:[NSNull null]])
            self.rentalBillId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"rentalItems"];
            for(id itemJson in jsonArray) {
                EvhSiteItemDTO* item = [EvhSiteItemDTO new];
                
                [item fromJson: itemJson];
                [self.rentalItems addObject: item];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"rentalAttachments"];
            for(id itemJson in jsonArray) {
                [self.rentalAttachments addObject: itemJson];
            }
        }
        self.attachmentType = [jsonObject objectForKey: @"attachmentType"];
        if(self.attachmentType && [self.attachmentType isEqual:[NSNull null]])
            self.attachmentType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
