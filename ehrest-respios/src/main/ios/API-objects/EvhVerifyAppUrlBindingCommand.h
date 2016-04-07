//
// EvhVerifyAppUrlBindingCommand.h
// generated at 2016-04-07 17:57:41 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyAppUrlBindingCommand
//
@interface EvhVerifyAppUrlBindingCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* appKey;

@property(nonatomic, copy) NSString* url;

@property(nonatomic, copy) NSNumber* timestamp;

@property(nonatomic, copy) NSNumber* nonce;

@property(nonatomic, copy) NSString* signature;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

