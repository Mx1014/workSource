//
// EvhAclinkConnectingCommand.h
// generated at 2016-04-12 15:02:19 
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

