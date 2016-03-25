//
// EvhTrustedAppCommand.h
// generated at 2016-03-25 11:43:32 
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

