//
// EvhQueryDoorMessageResponse.h
// generated at 2016-04-01 15:40:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhDoorMessage.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQueryDoorMessageResponse
//
@interface EvhQueryDoorMessageResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* doorId;

// item type EvhDoorMessage*
@property(nonatomic, strong) NSMutableArray* outputs;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

