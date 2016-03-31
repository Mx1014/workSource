//
// EvhGetPmPayStatisticsCommandResponse.h
// generated at 2016-03-31 11:07:26 
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

