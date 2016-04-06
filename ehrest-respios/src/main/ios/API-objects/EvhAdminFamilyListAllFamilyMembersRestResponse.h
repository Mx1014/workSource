//
// EvhAdminFamilyListAllFamilyMembersRestResponse.h
// generated at 2016-04-06 19:10:43 
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
