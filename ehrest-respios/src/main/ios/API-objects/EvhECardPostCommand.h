//
// EvhECardPostCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhECardPostCommand
//
@interface EvhECardPostCommand
    : NSObject<EvhJsonSerializable>


-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

