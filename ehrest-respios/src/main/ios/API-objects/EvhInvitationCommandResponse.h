//
// EvhInvitationCommandResponse.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
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

