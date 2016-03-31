//
// EvhDecodeWebTokenCommand.h
// generated at 2016-03-31 19:08:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDecodeWebTokenCommand
//
@interface EvhDecodeWebTokenCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* webToken;

@property(nonatomic, copy) NSString* tokenType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

