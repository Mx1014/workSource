//
// EvhLogonByTokenCommand.h
// generated at 2016-04-07 14:16:29 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLogonByTokenCommand
//
@interface EvhLogonByTokenCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* loginToken;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

