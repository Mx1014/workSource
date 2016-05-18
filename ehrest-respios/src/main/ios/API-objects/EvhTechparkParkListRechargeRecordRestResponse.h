//
// EvhTechparkParkListRechargeRecordRestResponse.h
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
