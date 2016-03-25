//
// EvhListFamilyRequestsCommandResponse.h
// generated at 2016-03-25 09:26:38 
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

