//
// EvhAclinkMessage.h
// generated at 2016-04-29 18:56:02 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkMessage
//
@interface EvhAclinkMessage
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* cmd;

@property(nonatomic, copy) NSNumber* secretVersion;

@property(nonatomic, copy) NSString* encrypted;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

