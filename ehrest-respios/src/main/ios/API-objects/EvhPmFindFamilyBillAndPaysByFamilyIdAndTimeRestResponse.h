//
// EvhPmFindFamilyBillAndPaysByFamilyIdAndTimeRestResponse.h
// generated at 2016-03-31 15:43:24 
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
