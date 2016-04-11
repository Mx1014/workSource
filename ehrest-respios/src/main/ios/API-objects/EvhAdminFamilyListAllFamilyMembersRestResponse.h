//
// EvhAdminFamilyListAllFamilyMembersRestResponse.h
// generated at 2016-04-07 17:57:43 
//
#import "RestResponseBase.h"
#import "EvhListAllFamilyMembersCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminFamilyListAllFamilyMembersRestResponse
//
@interface EvhAdminFamilyListAllFamilyMembersRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListAllFamilyMembersCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
