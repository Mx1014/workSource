//
// EvhSetMinimumAccountsCommand.h
// generated at 2016-04-19 13:40:01 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetMinimumAccountsCommand
//
@interface EvhSetMinimumAccountsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* accounts;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

