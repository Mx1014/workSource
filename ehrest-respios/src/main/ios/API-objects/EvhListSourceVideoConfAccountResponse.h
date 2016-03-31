//
// EvhListSourceVideoConfAccountResponse.h
// generated at 2016-03-31 10:18:20 
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

