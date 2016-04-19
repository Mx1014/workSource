//
// EvhDeleteParkingChargeCommand.h
// generated at 2016-04-19 14:25:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteParkingChargeCommand
//
@interface EvhDeleteParkingChargeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* months;

@property(nonatomic, copy) NSNumber* amount;

@property(nonatomic, copy) NSNumber* communityId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

