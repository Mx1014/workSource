//
// EvhGetPmPayStatisticsCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetPmPayStatisticsCommandResponse
//
@interface EvhGetPmPayStatisticsCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* yearIncomeAmount;

@property(nonatomic, copy) NSNumber* unPayAmount;

@property(nonatomic, copy) NSNumber* oweFamilyCount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

