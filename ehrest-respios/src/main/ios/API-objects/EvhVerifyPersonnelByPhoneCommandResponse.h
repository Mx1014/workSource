//
// EvhVerifyPersonnelByPhoneCommandResponse.h
// generated at 2016-03-25 19:05:21 
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

