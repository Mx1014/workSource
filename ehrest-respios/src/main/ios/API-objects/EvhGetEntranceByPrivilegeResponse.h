//
// EvhGetEntranceByPrivilegeResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetEntranceByPrivilegeResponse
//
@interface EvhGetEntranceByPrivilegeResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* entrancePrivilege;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

