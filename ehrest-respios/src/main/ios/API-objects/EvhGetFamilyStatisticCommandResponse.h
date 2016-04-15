//
// EvhGetFamilyStatisticCommandResponse.h
// generated at 2016-04-12 15:02:20 
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

