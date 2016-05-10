//
// EvhListPropMemberCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPropertyMemberDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPropMemberCommandResponse
//
@interface EvhListPropMemberCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhPropertyMemberDTO*
@property(nonatomic, strong) NSMutableArray* members;

@property(nonatomic, copy) NSNumber* pageCount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

