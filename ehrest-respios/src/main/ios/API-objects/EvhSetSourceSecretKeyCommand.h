//
// EvhSetSourceSecretKeyCommand.h
// generated at 2016-04-18 14:48:52 
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

