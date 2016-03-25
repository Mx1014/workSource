//
// EvhGetFamilyStatisticCommandResponse.h
// generated at 2016-03-25 15:57:22 
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

