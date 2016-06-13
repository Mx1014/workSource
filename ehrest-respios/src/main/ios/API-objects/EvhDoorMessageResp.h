//
// EvhDoorMessageResp.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhAclinkMessage.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorMessageResp
//
@interface EvhDoorMessageResp
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* seq;

@property(nonatomic, copy) NSNumber* doorId;

@property(nonatomic, copy) NSNumber* messageType;

@property(nonatomic, strong) EvhAclinkMessage* body;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

