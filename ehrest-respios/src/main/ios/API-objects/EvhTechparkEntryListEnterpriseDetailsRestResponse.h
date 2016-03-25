//
// EvhTechparkEntryListEnterpriseDetailsRestResponse.h
// generated at 2016-03-25 11:43:35 
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
