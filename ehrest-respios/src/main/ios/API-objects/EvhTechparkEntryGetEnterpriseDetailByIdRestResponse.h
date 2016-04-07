//
// EvhTechparkEntryGetEnterpriseDetailByIdRestResponse.h
// generated at 2016-04-07 14:16:31 
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
