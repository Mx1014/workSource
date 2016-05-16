//
// EvhRepeatExpressionDTO.h
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

