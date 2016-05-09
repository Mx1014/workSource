//
// EvhListRecipientCommand.h
// generated at 2016-04-29 18:56:02 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListRecipientCommand
//
@interface EvhListRecipientCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* anchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

