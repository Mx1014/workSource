//
// EvhListMemberCommandResponse.h
// generated at 2016-04-29 18:56:01 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhGroupMemberDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListMemberCommandResponse
//
@interface EvhListMemberCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhGroupMemberDTO*
@property(nonatomic, strong) NSMutableArray* members;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

