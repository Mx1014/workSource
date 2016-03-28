//
// EvhTechparkEntryListApplyEntrysRestResponse.h
// generated at 2016-03-25 19:05:21 
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
