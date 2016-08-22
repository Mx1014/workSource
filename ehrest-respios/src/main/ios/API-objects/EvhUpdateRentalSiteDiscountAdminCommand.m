//
// EvhUpdateRentalSiteDiscountAdminCommand.m
//
#import "EvhUpdateRentalSiteDiscountAdminCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateRentalSiteDiscountAdminCommand
//

@implementation EvhUpdateRentalSiteDiscountAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateRentalSiteDiscountAdminCommand* obj = [EvhUpdateRentalSiteDiscountAdminCommand new];
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
    if(self.rentalSiteId)
        [jsonObject setObject: self.rentalSiteId forKey: @"rentalSiteId"];
    if(self.discountType)
        [jsonObject setObject: self.discountType forKey: @"discountType"];
    if(self.fullPrice)
        [jsonObject setObject: self.fullPrice forKey: @"fullPrice"];
    if(self.cutprice)
        [jsonObject setObject: self.cutprice forKey: @"cutprice"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.rentalSiteId = [jsonObject objectForKey: @"rentalSiteId"];
        if(self.rentalSiteId && [self.rentalSiteId isEqual:[NSNull null]])
            self.rentalSiteId = nil;

        self.discountType = [jsonObject objectForKey: @"discountType"];
        if(self.discountType && [self.discountType isEqual:[NSNull null]])
            self.discountType = nil;

        self.fullPrice = [jsonObject objectForKey: @"fullPrice"];
        if(self.fullPrice && [self.fullPrice isEqual:[NSNull null]])
            self.fullPrice = nil;

        self.cutprice = [jsonObject objectForKey: @"cutprice"];
        if(self.cutprice && [self.cutprice isEqual:[NSNull null]])
            self.cutprice = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
