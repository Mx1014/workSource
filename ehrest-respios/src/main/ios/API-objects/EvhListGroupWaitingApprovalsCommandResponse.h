//
// EvhListGroupWaitingApprovalsCommandResponse.h
// generated at 2016-04-22 13:56:46 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhGroupMemberDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListGroupWaitingApprovalsCommandResponse
//
@interface EvhListGroupWaitingApprovalsCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhGroupMemberDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

