//
// EvhEncryptPlainTextCommand.h
// generated at 2016-03-31 10:18:19 
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

