//
// EvhPmFindFamilyBillAndPaysByFamilyIdAndTimeRestResponse.h
// generated at 2016-04-01 15:40:24 
//
#import "RestResponseBase.h"
#import "EvhPmBillsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmFindFamilyBillAndPaysByFamilyIdAndTimeRestResponse
//
@interface EvhPmFindFamilyBillAndPaysByFamilyIdAndTimeRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPmBillsDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
