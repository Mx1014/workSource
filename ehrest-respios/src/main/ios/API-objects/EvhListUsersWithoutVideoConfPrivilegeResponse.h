//
// EvhListUsersWithoutVideoConfPrivilegeResponse.h
// generated at 2016-04-18 14:48:50 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhEnterpriseUsersDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListUsersWithoutVideoConfPrivilegeResponse
//
@interface EvhListUsersWithoutVideoConfPrivilegeResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhEnterpriseUsersDTO*
@property(nonatomic, strong) NSMutableArray* enterpriseUsers;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

