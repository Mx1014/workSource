//
// EvhListSourceVideoConfAccountResponse.h
// generated at 2016-03-25 15:57:23 
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

