//
// EvhListConfOrderAccountResponse.h
// generated at 2016-04-26 18:22:54 
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

