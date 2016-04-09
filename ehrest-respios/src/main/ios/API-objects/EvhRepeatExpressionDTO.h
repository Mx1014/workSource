//
// EvhRepeatExpressionDTO.h
// generated at 2016-04-08 20:09:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRepeatExpressionDTO
//
@interface EvhRepeatExpressionDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* year;

@property(nonatomic, copy) NSNumber* month;

@property(nonatomic, copy) NSNumber* day;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

