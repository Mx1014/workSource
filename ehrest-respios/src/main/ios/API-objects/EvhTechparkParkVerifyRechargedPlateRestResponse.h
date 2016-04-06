//
// EvhTechparkParkVerifyRechargedPlateRestResponse.h
// generated at 2016-04-06 19:59:47 
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
