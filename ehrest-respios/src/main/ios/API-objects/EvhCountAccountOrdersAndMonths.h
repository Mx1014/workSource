//
// EvhCountAccountOrdersAndMonths.h
// generated at 2016-04-05 13:45:26 
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

