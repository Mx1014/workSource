//
// EvhGetEnterpriseDetailByIdResponse.h
// generated at 2016-03-28 15:56:08 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhEnterpriseDetailDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetEnterpriseDetailByIdResponse
//
@interface EvhGetEnterpriseDetailByIdResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, strong) EvhEnterpriseDetailDTO* detail;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

