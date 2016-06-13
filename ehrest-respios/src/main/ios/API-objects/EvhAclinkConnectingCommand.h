//
// EvhAclinkConnectingCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkConnectingCommand
//
@interface EvhAclinkConnectingCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* uuid;

@property(nonatomic, copy) NSString* encryptBase64;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

