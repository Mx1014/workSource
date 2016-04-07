//
// EvhPmGetApartmentStatisticsRestResponse.h
// generated at 2016-04-07 14:16:31 
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
