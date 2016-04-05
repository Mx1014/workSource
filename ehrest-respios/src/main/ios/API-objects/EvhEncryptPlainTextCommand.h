//
// EvhEncryptPlainTextCommand.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEncryptPlainTextCommand
//
@interface EvhEncryptPlainTextCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* plainText;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

