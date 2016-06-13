//
// EvhSetParkingCardIssueFlagCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetParkingCardIssueFlagCommand
//
@interface EvhSetParkingCardIssueFlagCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* parkingLotId;

@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* issueFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

