//
// EvhTechparkParkVerifyRechargedPlateRestResponse.h
// generated at 2016-03-25 09:26:44 
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
