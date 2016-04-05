//
// EvhSendPmPayMessageByAddressIdCommand.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendPmPayMessageByAddressIdCommand
//
@interface EvhSendPmPayMessageByAddressIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* addressId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

