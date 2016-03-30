//
// EvhTechparkEntryGetEnterpriseDetailByIdRestResponse.h
// generated at 2016-03-30 10:13:09 
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
