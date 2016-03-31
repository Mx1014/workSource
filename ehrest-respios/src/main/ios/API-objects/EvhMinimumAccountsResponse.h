//
// EvhMinimumAccountsResponse.h
// generated at 2016-03-31 15:43:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhMinimumAccountsResponse
//
@interface EvhMinimumAccountsResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* accounts;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

