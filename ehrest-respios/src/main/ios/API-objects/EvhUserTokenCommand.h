//
// EvhUserTokenCommand.h
// generated at 2016-04-22 13:56:49 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserTokenCommand
//
@interface EvhUserTokenCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* userIdentifier;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

