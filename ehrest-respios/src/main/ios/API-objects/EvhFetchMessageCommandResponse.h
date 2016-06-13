//
// EvhFetchMessageCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhMessageDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFetchMessageCommandResponse
//
@interface EvhFetchMessageCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhMessageDTO*
@property(nonatomic, strong) NSMutableArray* messages;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

