//
// EvhPmGetFamilyStatisticRestResponse.h
// generated at 2016-03-25 17:08:13 
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
