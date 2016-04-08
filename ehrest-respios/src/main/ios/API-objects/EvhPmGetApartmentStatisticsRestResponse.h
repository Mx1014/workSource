//
// EvhPmGetApartmentStatisticsRestResponse.h
// generated at 2016-04-07 17:57:44 
//
#import "RestResponseBase.h"
#import "EvhPropAptStatisticDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmGetApartmentStatisticsRestResponse
//
@interface EvhPmGetApartmentStatisticsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPropAptStatisticDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
