//
// EvhPhoneStatus.h
// generated at 2016-03-28 15:56:08 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPhoneStatus
//
@interface EvhPhoneStatus
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* phone;

@property(nonatomic, copy) NSString* status;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

