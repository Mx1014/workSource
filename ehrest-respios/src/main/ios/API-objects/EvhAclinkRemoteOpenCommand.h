//
// EvhAclinkRemoteOpenCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkRemoteOpenCommand
//
@interface EvhAclinkRemoteOpenCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* authId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

