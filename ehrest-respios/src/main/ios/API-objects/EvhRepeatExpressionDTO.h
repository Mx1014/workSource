//
// EvhRepeatExpressionDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:56 
>>>>>>> 3.3.x
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

