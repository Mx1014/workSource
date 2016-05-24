//
// EvhAddressDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressDTO
//
@interface EvhAddressDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* projectId;

@property(nonatomic, copy) NSString* resourceId;

@property(nonatomic, copy) NSString* resourceName;

@property(nonatomic, copy) NSNumber* payerId;

@property(nonatomic, copy) NSString* customerId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

