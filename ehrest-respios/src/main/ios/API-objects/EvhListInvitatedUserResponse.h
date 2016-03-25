//
// EvhListInvitatedUserResponse.h
// generated at 2016-03-25 15:57:21 
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

