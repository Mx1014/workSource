//
// EvhUpdateRentalSiteDiscountAdminCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateRentalSiteDiscountAdminCommand
//
@interface EvhUpdateRentalSiteDiscountAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalSiteId;

@property(nonatomic, copy) NSNumber* discountType;

@property(nonatomic, copy) NSNumber* fullPrice;

@property(nonatomic, copy) NSNumber* cutprice;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

