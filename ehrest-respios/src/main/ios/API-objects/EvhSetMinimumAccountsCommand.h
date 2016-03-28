//
// EvhSetMinimumAccountsCommand.h
// generated at 2016-03-28 15:56:08 
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

