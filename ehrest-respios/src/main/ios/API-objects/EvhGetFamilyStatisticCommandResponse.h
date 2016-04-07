//
// EvhGetFamilyStatisticCommandResponse.h
// generated at 2016-04-07 10:47:32 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetFamilyStatisticCommandResponse
//
@interface EvhGetFamilyStatisticCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* totalDueOweAmount;

@property(nonatomic, copy) NSNumber* totalPaidAmount;

@property(nonatomic, copy) NSNumber* nowWaitPayAmount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

