//
// EvhParkingChargeDTO.h
// generated at 2016-03-25 19:05:19 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingChargeDTO
//
@interface EvhParkingChargeDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* months;

@property(nonatomic, copy) NSString* cardType;

@property(nonatomic, copy) NSNumber* amount;

@property(nonatomic, copy) NSNumber* enterpriseCommunityId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

