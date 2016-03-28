//
// EvhGetEntranceByPrivilegeResponse.h
// generated at 2016-03-25 19:05:21 
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

