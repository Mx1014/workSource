//
// EvhCreateRechargeOrderCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateRechargeOrderCommand
//
@interface EvhCreateRechargeOrderCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* amount;

@property(nonatomic, copy) NSNumber* months;

@property(nonatomic, copy) NSString* plateNumber;

@property(nonatomic, copy) NSString* ownerName;

@property(nonatomic, copy) NSString* validityPeriod;

@property(nonatomic, copy) NSString* cardType;

@property(nonatomic, copy) NSNumber* communityId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

