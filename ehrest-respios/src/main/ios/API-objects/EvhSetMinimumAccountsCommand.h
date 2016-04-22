//
// EvhSetMinimumAccountsCommand.h
// generated at 2016-04-22 13:56:47 
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

