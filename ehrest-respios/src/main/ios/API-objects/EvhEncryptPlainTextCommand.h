//
// EvhEncryptPlainTextCommand.h
// generated at 2016-03-25 15:57:21 
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

