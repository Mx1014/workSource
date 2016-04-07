//
// EvhGetEntranceByPrivilegeResponse.h
// generated at 2016-04-07 14:16:31 
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

