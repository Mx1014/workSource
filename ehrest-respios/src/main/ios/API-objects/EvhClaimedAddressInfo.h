//
// EvhClaimedAddressInfo.h
// generated at 2016-03-31 10:18:20 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhClaimedAddressInfo
//
@interface EvhClaimedAddressInfo
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* addressId;

@property(nonatomic, copy) NSString* fullAddress;

@property(nonatomic, copy) NSNumber* userCount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

