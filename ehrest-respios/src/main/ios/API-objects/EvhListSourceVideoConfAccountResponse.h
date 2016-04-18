//
// EvhListSourceVideoConfAccountResponse.h
// generated at 2016-04-18 14:48:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhSourceVideoConfAccountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListSourceVideoConfAccountResponse
//
@interface EvhListSourceVideoConfAccountResponse
    : NSObject<EvhJsonSerializable>


// item type EvhSourceVideoConfAccountDTO*
@property(nonatomic, strong) NSMutableArray* sourceAccounts;

@property(nonatomic, copy) NSNumber* nextPageOffset;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

