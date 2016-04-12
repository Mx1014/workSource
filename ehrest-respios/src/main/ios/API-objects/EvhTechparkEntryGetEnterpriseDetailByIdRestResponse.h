//
// EvhTechparkEntryGetEnterpriseDetailByIdRestResponse.h
// generated at 2016-04-12 19:00:53 
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
