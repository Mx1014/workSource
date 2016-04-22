//
// EvhFindFamilyByAddressIdCommand.h
// generated at 2016-04-22 13:56:44 
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

