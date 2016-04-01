//
// EvhPollShowResultResponse.h
// generated at 2016-04-01 15:40:23 
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

