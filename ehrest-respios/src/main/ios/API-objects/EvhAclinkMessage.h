//
// EvhAclinkMessage.h
// generated at 2016-04-22 13:56:48 
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

