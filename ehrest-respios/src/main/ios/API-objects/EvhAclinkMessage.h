//
// EvhAclinkMessage.h
// generated at 2016-04-07 14:16:29 
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

