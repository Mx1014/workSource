//
// EvhVerifyPersonnelByPhoneCommandResponse.h
// generated at 2016-04-18 14:48:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOrganizationMemberDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyPersonnelByPhoneCommandResponse
//
@interface EvhVerifyPersonnelByPhoneCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, strong) EvhOrganizationMemberDTO* dto;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

