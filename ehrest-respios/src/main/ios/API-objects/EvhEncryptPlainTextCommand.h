//
// EvhEncryptPlainTextCommand.h
// generated at 2016-03-25 09:26:39 
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

