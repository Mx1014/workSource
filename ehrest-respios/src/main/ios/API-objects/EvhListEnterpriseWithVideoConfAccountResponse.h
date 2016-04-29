//
// EvhListEnterpriseWithVideoConfAccountResponse.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
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

