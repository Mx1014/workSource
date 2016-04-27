//
// EvhTechparkEntryListEnterpriseDetailsRestResponse.h
// generated at 2016-04-26 18:22:57 
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
