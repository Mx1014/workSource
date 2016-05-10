//
// EvhListContactsRequestByEnterpriseIdCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhEnterpriseContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListContactsRequestByEnterpriseIdCommandResponse
//
@interface EvhListContactsRequestByEnterpriseIdCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhEnterpriseContactDTO*
@property(nonatomic, strong) NSMutableArray* contacts;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

