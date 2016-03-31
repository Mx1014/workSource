//
// EvhListPushMessageResponse.h
// generated at 2016-03-31 11:07:26 
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

