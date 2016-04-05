//
// EvhDecodeWebTokenCommand.h
// generated at 2016-04-05 13:45:26 
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

