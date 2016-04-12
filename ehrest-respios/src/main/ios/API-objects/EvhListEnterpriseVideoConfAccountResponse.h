//
// EvhListEnterpriseVideoConfAccountResponse.h
// generated at 2016-04-12 19:00:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhConfAccountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterpriseVideoConfAccountResponse
//
@interface EvhListEnterpriseVideoConfAccountResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhConfAccountDTO*
@property(nonatomic, strong) NSMutableArray* confAccounts;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

