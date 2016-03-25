//
// EvhVerifyPersonnelByPhoneCommandResponse.h
// generated at 2016-03-25 09:26:41 
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

