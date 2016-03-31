//
// EvhQueryDoorMessageResponse.h
// generated at 2016-03-31 19:08:53 
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

