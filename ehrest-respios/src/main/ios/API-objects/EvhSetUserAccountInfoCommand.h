//
// EvhSetUserAccountInfoCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetUserAccountInfoCommand
//
@interface EvhSetUserAccountInfoCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* accountName;

@property(nonatomic, copy) NSString* password;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

