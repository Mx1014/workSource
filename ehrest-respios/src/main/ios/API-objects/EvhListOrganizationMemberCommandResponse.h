//
// EvhListOrganizationMemberCommandResponse.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOrganizationMemberDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOrganizationMemberCommandResponse
//
@interface EvhListOrganizationMemberCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhOrganizationMemberDTO*
@property(nonatomic, strong) NSMutableArray* members;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

