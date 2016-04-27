//
// EvhDataUtil.h
// generated at 2016-04-26 18:22:53 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDataUtil
//
@interface EvhDataUtil
    : NSObject<EvhJsonSerializable>


-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

