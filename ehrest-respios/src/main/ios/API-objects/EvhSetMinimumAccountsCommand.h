//
// EvhSetMinimumAccountsCommand.h
// generated at 2016-03-25 19:05:21 
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

