//
// EvhPmGetApartmentStatisticsRestResponse.h
// generated at 2016-04-22 13:56:50 
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
