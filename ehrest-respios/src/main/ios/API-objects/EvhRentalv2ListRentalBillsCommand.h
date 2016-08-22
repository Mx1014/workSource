//
// EvhRentalv2ListRentalBillsCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2ListRentalBillsCommand
//
@interface EvhRentalv2ListRentalBillsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSNumber* rentalSiteId;

@property(nonatomic, copy) NSNumber* resourceTypeId;

@property(nonatomic, copy) NSNumber* startTime;

@property(nonatomic, copy) NSNumber* endTime;

@property(nonatomic, copy) NSString* vendorType;

@property(nonatomic, copy) NSNumber* billStatus;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

