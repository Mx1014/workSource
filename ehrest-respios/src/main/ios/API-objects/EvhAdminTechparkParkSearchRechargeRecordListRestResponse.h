//
// EvhAdminTechparkParkSearchRechargeRecordListRestResponse.h
// generated at 2016-04-22 13:56:49 
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
