//
// EvhListConfOrderAccountResponse.h
// generated at 2016-04-01 15:40:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhConfOrderAccountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListConfOrderAccountResponse
//
@interface EvhListConfOrderAccountResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhConfOrderAccountDTO*
@property(nonatomic, strong) NSMutableArray* orderAccounts;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

