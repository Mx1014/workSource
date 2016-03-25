//
// EvhListUsersWithoutVideoConfPrivilegeResponse.h
// generated at 2016-03-25 09:26:38 
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

