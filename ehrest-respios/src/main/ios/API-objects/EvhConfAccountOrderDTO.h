//
// EvhConfAccountOrderDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfAccountOrderDTO
//
@interface EvhConfAccountOrderDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* billId;

@property(nonatomic, copy) NSNumber* amount;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* orderType;

@property(nonatomic, copy) NSString* appKey;

@property(nonatomic, copy) NSNumber* timestamp;

@property(nonatomic, copy) NSNumber* randomNum;

@property(nonatomic, copy) NSString* signature;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

