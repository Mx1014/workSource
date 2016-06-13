//
// EvhRealEstatePostCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRealEstatePostCommand
//
@interface EvhRealEstatePostCommand
    : NSObject<EvhJsonSerializable>


-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

