//
// EvhAclinkWebSocketMessage.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkWebSocketMessage
//
@interface EvhAclinkWebSocketMessage
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* seq;

@property(nonatomic, copy) NSNumber* type;

@property(nonatomic, copy) NSString* payload;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

