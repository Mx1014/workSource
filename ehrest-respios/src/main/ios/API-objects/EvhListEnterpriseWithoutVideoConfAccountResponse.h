//
// EvhListEnterpriseWithoutVideoConfAccountResponse.h
// generated at 2016-04-26 18:22:54 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhEnterpriseWithoutVideoConfAccountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterpriseWithoutVideoConfAccountResponse
//
@interface EvhListEnterpriseWithoutVideoConfAccountResponse
    : NSObject<EvhJsonSerializable>


// item type EvhEnterpriseWithoutVideoConfAccountDTO*
@property(nonatomic, strong) NSMutableArray* enterprises;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

