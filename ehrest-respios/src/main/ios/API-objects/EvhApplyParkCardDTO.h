//
// EvhApplyParkCardDTO.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhApplyParkCardDTO
//
@interface EvhApplyParkCardDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* applierName;

@property(nonatomic, copy) NSString* applierPhone;

@property(nonatomic, copy) NSString* companyName;

@property(nonatomic, copy) NSString* plateNumber;

@property(nonatomic, copy) NSNumber* applyTime;

@property(nonatomic, copy) NSNumber* applyStatus;

@property(nonatomic, copy) NSNumber* fetchStatus;

@property(nonatomic, copy) NSNumber* deadline;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

