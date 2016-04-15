//
// EvhCreateParkingChargeCommand.h
// generated at 2016-04-12 15:02:18 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateParkingChargeCommand
//
@interface EvhCreateParkingChargeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* months;

@property(nonatomic, copy) NSNumber* amount;

@property(nonatomic, copy) NSString* cardType;

@property(nonatomic, copy) NSNumber* communityId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

