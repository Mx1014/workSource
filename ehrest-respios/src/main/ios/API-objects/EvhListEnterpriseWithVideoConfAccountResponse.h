//
// EvhListEnterpriseWithVideoConfAccountResponse.h
// generated at 2016-04-07 17:03:17 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhEnterpriseConfAccountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterpriseWithVideoConfAccountResponse
//
@interface EvhListEnterpriseWithVideoConfAccountResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhEnterpriseConfAccountDTO*
@property(nonatomic, strong) NSMutableArray* enterpriseConfAccounts;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

