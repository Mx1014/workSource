//
// EvhCommonOrderDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommonOrderDTO
//
@interface EvhCommonOrderDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* orderNo;

@property(nonatomic, copy) NSString* orderType;

@property(nonatomic, copy) NSNumber* totalFee;

@property(nonatomic, copy) NSString* subject;

@property(nonatomic, copy) NSString* body;

@property(nonatomic, copy) NSString* appKey;

@property(nonatomic, copy) NSString* signature;

@property(nonatomic, copy) NSNumber* timestamp;

@property(nonatomic, copy) NSNumber* randomNum;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

