//
// EvhPmGetFamilyStatisticRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhGetFamilyStatisticCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmGetFamilyStatisticRestResponse
//
@interface EvhPmGetFamilyStatisticRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetFamilyStatisticCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
