//
// EvhTechparkParkListRechargeRecordRestResponse.h
// generated at 2016-04-05 13:45:27 
//
#import "RestResponseBase.h"
#import "EvhRechargeRecordList.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkParkListRechargeRecordRestResponse
//
@interface EvhTechparkParkListRechargeRecordRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRechargeRecordList* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
