//
// EvhDeleteSourceVideoConfAccountCommand.h
// generated at 2016-03-25 19:05:20 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteSourceVideoConfAccountCommand
//
@interface EvhDeleteSourceVideoConfAccountCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* sourceAccountId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

