//
// EvhListPropInvitedUserCommandResponse.h
// generated at 2016-04-19 12:41:54 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPropInvitedUserDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPropInvitedUserCommandResponse
//
@interface EvhListPropInvitedUserCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhPropInvitedUserDTO*
@property(nonatomic, strong) NSMutableArray* users;

@property(nonatomic, copy) NSNumber* pageCount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

