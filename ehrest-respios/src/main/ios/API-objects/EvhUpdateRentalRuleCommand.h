//
// EvhUpdateRentalRuleCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateRentalRuleCommand
//
@interface EvhUpdateRentalRuleCommand
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

