//
// EvhPmGetFamilyStatisticRestResponse.h
// generated at 2016-04-07 17:57:44 
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
