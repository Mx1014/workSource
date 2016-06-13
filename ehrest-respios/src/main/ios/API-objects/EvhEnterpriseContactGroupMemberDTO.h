//
// EvhEnterpriseContactGroupMemberDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseContactGroupMemberDTO
//
@interface EvhEnterpriseContactGroupMemberDTO
    : NSObject<EvhJsonSerializable>


-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

