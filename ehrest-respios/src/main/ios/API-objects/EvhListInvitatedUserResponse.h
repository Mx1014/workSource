//
// EvhListInvitatedUserResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhInvitatedUsers.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListInvitatedUserResponse
//
@interface EvhListInvitatedUserResponse
    : NSObject<EvhJsonSerializable>


// item type EvhInvitatedUsers*
@property(nonatomic, strong) NSMutableArray* invitatedUsers;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

