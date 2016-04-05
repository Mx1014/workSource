//
// EvhGetFamilyStatisticCommandResponse.h
// generated at 2016-04-05 13:45:26 
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

