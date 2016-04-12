//
// EvhTechparkEntryListApplyEntrysRestResponse.h
// generated at 2016-04-12 15:02:21 
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
