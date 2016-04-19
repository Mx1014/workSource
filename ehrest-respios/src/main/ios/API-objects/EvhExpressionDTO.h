//
// EvhExpressionDTO.h
// generated at 2016-04-19 14:25:56 
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

