//
// EvhParkingCardRequestDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingCardRequestDTO
//
@interface EvhParkingCardRequestDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* parkingLotId;

@property(nonatomic, copy) NSNumber* requestorEnterpriseId;

@property(nonatomic, copy) NSNumber* requestorUid;

@property(nonatomic, copy) NSString* requestorName;

@property(nonatomic, copy) NSString* plateNumber;

@property(nonatomic, copy) NSString* plateOwnerEntperiseName;

@property(nonatomic, copy) NSString* plateOwnerName;

@property(nonatomic, copy) NSString* plateOwnerPhone;

@property(nonatomic, copy) NSNumber* ranking;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* issueFlag;

@property(nonatomic, copy) NSNumber* issueTime;

@property(nonatomic, copy) NSNumber* createTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

