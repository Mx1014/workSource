//
// EvhAdminTechparkParkSearchRechargeRecordListRestResponse.h
// generated at 2016-04-19 13:40:01 
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
