//
// EvhTrustedAppCommand.h
// generated at 2016-04-07 17:33:47 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTrustedAppCommand
//
@interface EvhTrustedAppCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* appKey;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

