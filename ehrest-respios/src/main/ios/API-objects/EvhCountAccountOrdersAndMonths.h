//
// EvhCountAccountOrdersAndMonths.h
// generated at 2016-03-25 17:08:12 
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

