//
// EvhDoorMessage.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhAclinkMessage.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorMessage
//
@interface EvhDoorMessage
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* seq;

@property(nonatomic, copy) NSNumber* doorId;

@property(nonatomic, copy) NSNumber* messageType;

@property(nonatomic, strong) EvhAclinkMessage* body;

@property(nonatomic, copy) NSString* extra;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

