//
// EvhTechparkParkVerifyRechargedPlateRestResponse.h
// generated at 2016-04-01 15:40:24 
//
#import "RestResponseBase.h"
#import "EvhPlateInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkParkVerifyRechargedPlateRestResponse
//
@interface EvhTechparkParkVerifyRechargedPlateRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPlateInfo* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
