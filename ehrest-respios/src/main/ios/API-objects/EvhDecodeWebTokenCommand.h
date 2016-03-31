//
// EvhDecodeWebTokenCommand.h
// generated at 2016-03-31 10:18:19 
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

