//
// EvhRentalv2ActionData.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2ActionData
//
@interface EvhRentalv2ActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSString* entityTag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

