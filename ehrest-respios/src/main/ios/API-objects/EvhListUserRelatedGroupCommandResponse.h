//
// EvhListUserRelatedGroupCommandResponse.h
// generated at 2016-04-07 14:16:31 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhGroupDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListUserRelatedGroupCommandResponse
//
@interface EvhListUserRelatedGroupCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhGroupDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

