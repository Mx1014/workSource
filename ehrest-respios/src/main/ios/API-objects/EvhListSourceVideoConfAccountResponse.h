//
// EvhListSourceVideoConfAccountResponse.h
// generated at 2016-04-07 15:16:52 
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

