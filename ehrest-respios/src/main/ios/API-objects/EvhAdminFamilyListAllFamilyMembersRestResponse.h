//
// EvhAdminFamilyListAllFamilyMembersRestResponse.h
// generated at 2016-04-07 10:47:32 
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
