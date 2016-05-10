//
// EvhCorrectAddressCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCorrectAddressCommand
//
@interface EvhCorrectAddressCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* addressId;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSString* apartmentName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

