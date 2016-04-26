//
// EvhTechparkEntryListApplyEntrysRestResponse.h
// generated at 2016-04-26 18:22:57 
//
#import "RestResponseBase.h"
#import "EvhEnterpriseApplyEntryResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkEntryListApplyEntrysRestResponse
//
@interface EvhTechparkEntryListApplyEntrysRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhEnterpriseApplyEntryResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
