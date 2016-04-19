//
// EvhFetchMessageCommandResponse.h
// generated at 2016-04-19 13:40:01 
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

