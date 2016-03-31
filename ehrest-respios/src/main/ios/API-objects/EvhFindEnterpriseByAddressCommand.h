//
// EvhFindEnterpriseByAddressCommand.h
// generated at 2016-03-31 19:08:54 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindEnterpriseByAddressCommand
//
@interface EvhFindEnterpriseByAddressCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* addressId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

