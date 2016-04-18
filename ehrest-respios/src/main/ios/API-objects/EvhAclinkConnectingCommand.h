//
// EvhAclinkConnectingCommand.h
// generated at 2016-04-18 14:48:52 
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

