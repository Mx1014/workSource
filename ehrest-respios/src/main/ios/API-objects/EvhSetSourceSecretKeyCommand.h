//
// EvhSetSourceSecretKeyCommand.h
// generated at 2016-04-01 15:40:22 
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

