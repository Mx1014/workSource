//
// EvhApplyParkCardDTO.h
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

