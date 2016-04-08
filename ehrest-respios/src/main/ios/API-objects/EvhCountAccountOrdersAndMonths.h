//
// EvhCountAccountOrdersAndMonths.h
// generated at 2016-04-08 20:09:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCountAccountOrdersAndMonths
//
@interface EvhCountAccountOrdersAndMonths
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* orders;

@property(nonatomic, copy) NSNumber* months;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

