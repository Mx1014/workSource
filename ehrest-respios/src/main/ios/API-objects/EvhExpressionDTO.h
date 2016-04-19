//
// EvhExpressionDTO.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:50 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRepeatExpressionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhExpressionDTO
//
@interface EvhExpressionDTO
    : NSObject<EvhJsonSerializable>


// item type EvhRepeatExpressionDTO*
@property(nonatomic, strong) NSMutableArray* expression;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

