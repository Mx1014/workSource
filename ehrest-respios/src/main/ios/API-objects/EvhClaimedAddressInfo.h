//
// EvhClaimedAddressInfo.h
// generated at 2016-04-22 13:56:48 
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

