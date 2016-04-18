//
// EvhTechparkEntryListEnterpriseDetailsRestResponse.h
// generated at 2016-04-18 14:48:52 
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
