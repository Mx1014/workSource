//
// EvhQueryEnterpriseByPhoneResponse.h
// generated at 2016-03-31 20:15:32 
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

