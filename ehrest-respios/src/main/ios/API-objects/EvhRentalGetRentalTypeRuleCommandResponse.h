//
// EvhRentalGetRentalTypeRuleCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalGetRentalTypeRuleCommandResponse
//
@interface EvhRentalGetRentalTypeRuleCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* siteType;

@property(nonatomic, copy) NSNumber* rentalStartTime;

@property(nonatomic, copy) NSNumber* rentalEndTime;

@property(nonatomic, copy) NSNumber* payStartTime;

@property(nonatomic, copy) NSNumber* payEndTime;

@property(nonatomic, copy) NSNumber* payRatio;

@property(nonatomic, copy) NSNumber* refundFlag;

@property(nonatomic, copy) NSNumber* rentalType;

@property(nonatomic, copy) NSString* contactNum;

@property(nonatomic, copy) NSNumber* cancelTime;

@property(nonatomic, copy) NSNumber* overtimeTime;

@property(nonatomic, copy) NSString* contactAddress;

@property(nonatomic, copy) NSString* contactName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

