//
// EvhQualityEvaluationFactorsDTO.h
// generated at 2016-04-26 18:22:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityEvaluationFactorsDTO
//
@interface EvhQualityEvaluationFactorsDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* categoryId;

@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSNumber* weight;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

