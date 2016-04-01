//
// EvhCountAccountOrdersAndMonths.h
// generated at 2016-03-31 20:15:31 
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

