//
// EvhInvitationCommandResponse.h
// generated at 2016-04-07 10:47:30 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhInvitationDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInvitationCommandResponse
//
@interface EvhInvitationCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhInvitationDTO*
@property(nonatomic, strong) NSMutableArray* recipientList;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

