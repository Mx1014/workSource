//
// EvhSetUserAccountInfoCommand.h
// generated at 2016-03-25 17:08:10 
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

