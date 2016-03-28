//
// EvhSetSourceSecretKeyCommand.h
// generated at 2016-03-28 15:56:07 
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

