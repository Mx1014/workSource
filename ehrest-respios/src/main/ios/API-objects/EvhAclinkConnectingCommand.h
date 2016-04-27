//
// EvhAclinkConnectingCommand.h
// generated at 2016-04-26 18:22:53 
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

