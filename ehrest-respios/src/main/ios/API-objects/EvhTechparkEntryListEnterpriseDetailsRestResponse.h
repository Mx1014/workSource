//
// EvhTechparkEntryListEnterpriseDetailsRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"
#import "EvhListEnterpriseDetailResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkEntryListEnterpriseDetailsRestResponse
//
@interface EvhTechparkEntryListEnterpriseDetailsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListEnterpriseDetailResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
