//
// EvhAdminContactListAprovingContactRestResponse.h
// generated at 2016-04-19 14:25:57 
//
#import "RestResponseBase.h"
#import "EvhListEnterpriseContactResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminContactListAprovingContactRestResponse
//
@interface EvhAdminContactListAprovingContactRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListEnterpriseContactResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
