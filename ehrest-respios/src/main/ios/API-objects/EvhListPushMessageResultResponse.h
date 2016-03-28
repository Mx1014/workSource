//
// EvhListPushMessageResultResponse.h
// generated at 2016-03-25 19:05:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPushMessageResultDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPushMessageResultResponse
//
@interface EvhListPushMessageResultResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhPushMessageResultDTO*
@property(nonatomic, strong) NSMutableArray* pushMessages;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

