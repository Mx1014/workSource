//
// EvhListEnterpriseWithoutVideoConfAccountResponse.h
// generated at 2016-04-08 20:09:21 
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

