//
// EvhDoorMessage.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
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

