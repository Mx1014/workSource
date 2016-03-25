//
// EvhAdminTechparkParkSearchRechargeRecordListRestResponse.h
// generated at 2016-03-25 09:26:43 
//
#import "RestResponseBase.h"
#import "EvhRechargeRecordList.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminTechparkParkSearchRechargeRecordListRestResponse
//
@interface EvhAdminTechparkParkSearchRechargeRecordListRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRechargeRecordList* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
