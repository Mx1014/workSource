//
// EvhSetUserAccountInfoCommand.h
// generated at 2016-03-25 09:26:39 
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

