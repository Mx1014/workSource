//
// EvhLogonByTokenCommand.h
// generated at 2016-03-25 15:57:21 
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

