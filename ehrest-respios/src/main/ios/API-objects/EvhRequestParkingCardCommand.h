//
// EvhRequestParkingCardCommand.h
// generated at 2016-04-07 17:03:17 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRequestParkingCardCommand
//
@interface EvhRequestParkingCardCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* parkingLotId;

@property(nonatomic, copy) NSNumber* requestorEnterpriseId;

@property(nonatomic, copy) NSString* plateNumber;

@property(nonatomic, copy) NSString* plateOwnerEntperiseName;

@property(nonatomic, copy) NSString* plateOwnerName;

@property(nonatomic, copy) NSString* plateOwnerPhone;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

