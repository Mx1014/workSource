//
// EvhAclinkMessageMeta.h
// generated at 2016-04-12 15:02:20 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkMessageMeta
//
@interface EvhAclinkMessageMeta
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* doorId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

