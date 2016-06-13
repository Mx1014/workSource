//
// EvhListFamilyRequestsCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhFamilyMembershipRequestDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListFamilyRequestsCommandResponse
//
@interface EvhListFamilyRequestsCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhFamilyMembershipRequestDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

