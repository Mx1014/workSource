//
// EvhTechparkEntryGetEnterpriseDetailByIdRestResponse.h
// generated at 2016-04-22 13:56:51 
//
#import "RestResponseBase.h"
#import "EvhGetEnterpriseDetailByIdResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkEntryGetEnterpriseDetailByIdRestResponse
//
@interface EvhTechparkEntryGetEnterpriseDetailByIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetEnterpriseDetailByIdResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
