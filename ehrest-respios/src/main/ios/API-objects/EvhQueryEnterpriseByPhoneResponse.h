//
// EvhQueryEnterpriseByPhoneResponse.h
// generated at 2016-03-25 11:43:32 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhEnterpriseDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQueryEnterpriseByPhoneResponse
//
@interface EvhQueryEnterpriseByPhoneResponse
    : NSObject<EvhJsonSerializable>


// item type EvhEnterpriseDTO*
@property(nonatomic, strong) NSMutableArray* enterprises;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

