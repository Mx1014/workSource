//
// EvhDeleteVideoConfAccountCommand.h
// generated at 2016-04-06 19:10:42 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteVideoConfAccountCommand
//
@interface EvhDeleteVideoConfAccountCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* accountId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

