//
// EvhListFamilyRequestsCommandResponse.h
// generated at 2016-04-26 18:22:55 
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

