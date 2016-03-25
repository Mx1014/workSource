//
// EvhTrustedAppCommand.h
// generated at 2016-03-25 15:57:22 
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

