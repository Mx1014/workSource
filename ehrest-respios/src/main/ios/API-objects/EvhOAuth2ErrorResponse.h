//
// EvhOAuth2ErrorResponse.h
// generated at 2016-03-25 15:57:22 
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

