//
// EvhListEvaluationsResponse.h
// generated at 2016-04-08 20:09:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhEvaluationDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEvaluationsResponse
//
@interface EvhListEvaluationsResponse
    : NSObject<EvhJsonSerializable>


// item type EvhEvaluationDTO*
@property(nonatomic, strong) NSMutableArray* performances;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

