//
// EvhDeleteItemAdminCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteItemAdminCommand
//
@interface EvhDeleteItemAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* itemId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

