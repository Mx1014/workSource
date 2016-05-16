//
// EvhPollShowResultResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPollDTO.h"
#import "EvhPollItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPollShowResultResponse
//
@interface EvhPollShowResultResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, strong) EvhPollDTO* poll;

// item type EvhPollItemDTO*
@property(nonatomic, strong) NSMutableArray* items;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

