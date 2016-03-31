//
// EvhTechparkEntryListApplyEntrysRestResponse.h
// generated at 2016-03-28 15:56:09 
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
