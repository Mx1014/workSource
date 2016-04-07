//
// EvhPollShowResultResponse.h
// generated at 2016-04-07 10:47:31 
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

