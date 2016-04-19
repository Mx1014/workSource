//
// EvhListUsersWithoutVideoConfPrivilegeResponse.h
// generated at 2016-04-19 13:39:59 
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

