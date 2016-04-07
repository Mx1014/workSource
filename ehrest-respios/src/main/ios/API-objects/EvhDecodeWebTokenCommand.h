//
// EvhDecodeWebTokenCommand.h
// generated at 2016-04-07 15:16:51 
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

