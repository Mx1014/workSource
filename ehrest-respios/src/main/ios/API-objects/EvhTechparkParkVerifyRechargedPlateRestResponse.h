//
// EvhTechparkParkVerifyRechargedPlateRestResponse.h
// generated at 2016-04-07 10:47:33 
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
