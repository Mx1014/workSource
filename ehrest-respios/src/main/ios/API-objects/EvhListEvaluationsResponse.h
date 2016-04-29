//
// EvhListEvaluationsResponse.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
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

