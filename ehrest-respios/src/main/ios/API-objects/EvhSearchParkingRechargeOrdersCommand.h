//
// EvhSearchParkingRechargeOrdersCommand.h
// generated at 2016-04-12 15:02:20 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchParkingRechargeOrdersCommand
//
@interface EvhSearchParkingRechargeOrdersCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* parkingLotId;

@property(nonatomic, copy) NSString* plateNumber;

@property(nonatomic, copy) NSString* plateOwnerName;

@property(nonatomic, copy) NSString* plateOwnerPhone;

@property(nonatomic, copy) NSString* payerName;

@property(nonatomic, copy) NSString* payerPhone;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

