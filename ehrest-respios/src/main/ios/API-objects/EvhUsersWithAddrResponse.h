//
// EvhUsersWithAddrResponse.h
// generated at 2016-04-07 17:57:41 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhListUsersWithAddrResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUsersWithAddrResponse
//
@interface EvhUsersWithAddrResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

// item type EvhListUsersWithAddrResponse*
@property(nonatomic, strong) NSMutableArray* users;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

