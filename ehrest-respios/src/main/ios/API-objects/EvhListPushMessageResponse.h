//
// EvhListPushMessageResponse.h
// generated at 2016-03-25 17:08:12 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPushMessageDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPushMessageResponse
//
@interface EvhListPushMessageResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhPushMessageDTO*
@property(nonatomic, strong) NSMutableArray* pushMessages;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

