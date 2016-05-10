//
// EvhFindFamilyByAddressIdCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindFamilyByAddressIdCommand
//
@interface EvhFindFamilyByAddressIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* addressId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

