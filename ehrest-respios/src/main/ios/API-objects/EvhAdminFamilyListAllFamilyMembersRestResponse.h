//
// EvhAdminFamilyListAllFamilyMembersRestResponse.h
// generated at 2016-04-06 19:59:47 
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
