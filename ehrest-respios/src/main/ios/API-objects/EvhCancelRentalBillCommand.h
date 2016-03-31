//
// EvhCancelRentalBillCommand.h
// generated at 2016-03-31 13:49:14 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCancelRentalBillCommand
//
@interface EvhCancelRentalBillCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* siteType;

@property(nonatomic, copy) NSNumber* rentalBillId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

