//
// EvhTrustedAppCommand.h
// generated at 2016-04-19 12:41:53 
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

