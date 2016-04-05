//
// EvhDeviceMessages.h
// generated at 2016-04-05 13:45:25 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhDeviceMessage.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeviceMessages
//
@interface EvhDeviceMessages
    : NSObject<EvhJsonSerializable>


// item type EvhDeviceMessage*
@property(nonatomic, strong) NSMutableArray* messages;

@property(nonatomic, copy) NSNumber* anchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

