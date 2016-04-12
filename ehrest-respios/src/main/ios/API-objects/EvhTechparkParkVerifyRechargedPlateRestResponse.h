//
// EvhTechparkParkVerifyRechargedPlateRestResponse.h
// generated at 2016-04-12 19:00:53 
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
