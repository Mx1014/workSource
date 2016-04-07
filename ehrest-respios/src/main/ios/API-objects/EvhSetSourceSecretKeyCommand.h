//
// EvhSetSourceSecretKeyCommand.h
// generated at 2016-04-07 17:03:17 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetSourceSecretKeyCommand
//
@interface EvhSetSourceSecretKeyCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* secretKey;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

