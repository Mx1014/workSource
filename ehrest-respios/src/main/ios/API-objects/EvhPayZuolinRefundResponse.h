//
// EvhPayZuolinRefundResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPayZuolinRefundResponse
//
@interface EvhPayZuolinRefundResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* version;

@property(nonatomic, copy) NSNumber* errorCode;

@property(nonatomic, copy) NSString* errorScope;

@property(nonatomic, copy) NSString* errorDescription;

@property(nonatomic, copy) NSString* errorDetails;

@property(nonatomic, copy) NSString* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

