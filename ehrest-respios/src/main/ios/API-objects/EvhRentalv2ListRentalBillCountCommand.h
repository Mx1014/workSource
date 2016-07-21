//
// EvhRentalv2ListRentalBillCountCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2ListRentalBillCountCommand
//
@interface EvhRentalv2ListRentalBillCountCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* siteType;

@property(nonatomic, copy) NSNumber* rentalSiteId;

@property(nonatomic, copy) NSNumber* beginDate;

@property(nonatomic, copy) NSNumber* endDate;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

