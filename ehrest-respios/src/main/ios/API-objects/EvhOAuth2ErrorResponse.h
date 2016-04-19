//
// EvhOAuth2ErrorResponse.h
// generated at 2016-04-19 14:25:56 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOAuth2ErrorResponse
//
@interface EvhOAuth2ErrorResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* error;

@property(nonatomic, copy) NSString* error_description;

@property(nonatomic, copy) NSString* error_uri;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

