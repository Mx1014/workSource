//
// EvhCommonRestResponse.h
// generated at 2016-03-31 15:43:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhObject.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommonRestResponse
//
@interface EvhCommonRestResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* version;

@property(nonatomic, copy) NSString* errorScope;

@property(nonatomic, copy) NSNumber* errorCode;

@property(nonatomic, copy) NSString* errorDescription;

@property(nonatomic, strong) id response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

