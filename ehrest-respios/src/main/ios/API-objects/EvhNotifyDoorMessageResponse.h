//
// EvhNotifyDoorMessageResponse.h
// generated at 2016-04-07 17:03:17 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPhoneStatus.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNotifyDoorMessageResponse
//
@interface EvhNotifyDoorMessageResponse
    : NSObject<EvhJsonSerializable>


// item type EvhPhoneStatus*
@property(nonatomic, strong) NSMutableArray* phoneStatus;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

