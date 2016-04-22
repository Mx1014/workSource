//
// EvhGetEntranceByPrivilegeResponse.h
// generated at 2016-04-22 13:56:46 
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

