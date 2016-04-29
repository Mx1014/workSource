//
// EvhQueryDoorMessageResponse.h
// generated at 2016-04-29 18:56:03 
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

